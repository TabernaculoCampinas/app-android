package br.org.tabernaculocampinas.ui.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.session.MediaSessionManager
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.extension.Constants
import br.org.tabernaculocampinas.ui.activity.MainActivity
import br.org.tabernaculocampinas.ui.extension.NotificationExtension
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.analytics.PlaybackStats
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.gson.Gson

class WebRadioService : Service() {
    private val notificationId = 11

    var mediaPlayer: ExoPlayer? = null
    var radioUrl: String = ""

    var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    var mediaSessionManager: MediaSessionManager? = null
    var mediaSessionCompat: MediaSessionCompat? = null

    var transportControls: MediaControllerCompat.TransportControls? = null

    companion object {
        fun startRadioService(context: Context, radioUrl: String) {
            val intent = Intent(context, WebRadioService::class.java)
            intent.action = Constants.actionPlayRadio

            val sharedPreference =
                context.getSharedPreferences(
                    "TABERNACULO_CAMPINAS", Context.MODE_PRIVATE
                )

            val editor = sharedPreference?.edit()
            editor?.putString("radio_url", radioUrl)
            editor?.apply()

            context.startService(intent)
        }

        fun stopRadioService(context: Context) {
            val intent = Intent(context, WebRadioService::class.java)

            context.stopService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            treatIntentions(intent!!)
        } catch (ex: Exception) {
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mediaPlayer != null) {
            stopRadio()
            mediaPlayer!!.release()
        }
    }

    fun startRadio() {
        if (mediaPlayer == null) {
            val sharedPreference =
                baseContext.getSharedPreferences("TABERNACULO_CAMPINAS", Context.MODE_PRIVATE)

            radioUrl = sharedPreference!!.getString("radio_url", "")!!

            mediaPlayer = ExoPlayer.Builder(this).build()
        }

        if (mediaSessionManager == null || transportControls == null)
            loadMediaSession()

        if (notificationBuilder == null || notificationManager == null)
            loadNotification()

        if (mediaPlayer!!.playbackState != PlaybackStats.PLAYBACK_STATE_PLAYING){
            mediaPlayer!!.playWhenReady = true
            mediaPlayer!!.setMediaSource(buildMediaSource())
            mediaPlayer!!.prepare()
        }

        configureNotification(PlaybackStats.PLAYBACK_STATE_PLAYING)
    }

    fun stopRadio() {
        if (mediaPlayer == null)
            return

        if (mediaPlayer!!.playbackState != PlaybackStats.PLAYBACK_STATE_STOPPED)
            mediaPlayer!!.stop()

        stopService()
    }

    private fun loadMediaSession() {
        if (mediaSessionManager != null)
            return

        mediaSessionManager = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager
        mediaSessionCompat = MediaSessionCompat(this, "AudioPlayer")
        transportControls = mediaSessionCompat!!.controller.transportControls

        val activity = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, activity, PendingIntent.FLAG_IMMUTABLE)

        mediaSessionCompat!!.isActive = true
        mediaSessionCompat!!.setSessionActivity(pendingIntent)
        mediaSessionCompat!!.setCallback(WebRadioCallback(this))
    }

    private fun loadNotification() {
        notificationBuilder = NotificationCompat.Builder(
            this,
            resources.getString(R.string.radio_notification_channel_id)
        )
            .setShowWhen(false)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionCompat?.sessionToken)
                    .setShowActionsInCompactView(0)
            )
            .setColor(getColor(R.color.white))
            .setSmallIcon(R.drawable.app_logo)
            .setContentText("Web Rádio")
            .setContentTitle("Tabernáculo Campinas")

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun configureNotification(playbackStats: Int) {
        NotificationExtension.createRadioNotificationChannel(baseContext)

        if (notificationBuilder == null)
            loadNotification()

        notificationBuilder!!.clearActions()

        when (playbackStats) {
            PlaybackStats.PLAYBACK_STATE_PLAYING -> {
                notificationBuilder!!.addAction(
                    R.drawable.ic_stop_circle,
                    "stop",
                    actionPlayback(1)
                )

                startForeground(notificationId, notificationBuilder!!.build())

                sendBroadcast(Intent(Constants.actionPlayingRadio))
            }
            PlaybackStats.PLAYBACK_STATE_STOPPED -> {
                notificationBuilder!!.addAction(
                    R.drawable.ic_play_circle,
                    "play",
                    actionPlayback(0)
                )

                notificationManager!!.notify(notificationId, notificationBuilder!!.build())

                sendBroadcast(Intent(Constants.actionStopRadio))
            }
        }
    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_DETACH)
        configureNotification(PlaybackStats.PLAYBACK_STATE_STOPPED)
    }

    private fun actionPlayback(actionNumber: Int): PendingIntent? {
        val intent = Intent(this, WebRadioService::class.java)

        when (actionNumber) {
            0 -> {
                intent.action = Constants.actionPlayRadio

                return PendingIntent.getService(
                    this,
                    actionNumber,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
            1 -> {
                intent.action = Constants.actionStopRadio

                return PendingIntent.getService(
                    this,
                    actionNumber,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        }

        return null
    }

    private fun buildMediaSource(): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(radioUrl))
    }

    private fun treatIntentions(intent: Intent) {
        if (intent.action.isNullOrEmpty())
            return

        val actionString = intent.action

        if (actionString.equals(Constants.actionPlayRadio))
            startRadio()

        if (actionString.equals(Constants.actionStopRadio))
            stopRadio()
    }
}

data class LocalBinder(
    val service: WebRadioService
) : Binder()

class WebRadioCallback(private val webRadioService: WebRadioService) :
    MediaSessionCompat.Callback() {

    override fun onPlay() {
        super.onPlay()

        webRadioService.startRadio()
    }

    override fun onStop() {
        super.onStop()

        webRadioService.stopRadio()
    }
}