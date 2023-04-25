package br.org.tabernaculocampinas.ui.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.session.MediaSessionManager
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.extension.Constants
import br.org.tabernaculocampinas.extension.LocalBinder
import br.org.tabernaculocampinas.ui.activity.MainActivity
import br.org.tabernaculocampinas.ui.extension.NotificationExtension
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.analytics.PlaybackStats
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class DailyReadingService : Service() {
    private val notificationId = 11

    var mediaPlayer: ExoPlayer? = null
    var dailyReadingUrl: String = ""

    var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    var mediaSessionManager: MediaSessionManager? = null
    var mediaSessionCompat: MediaSessionCompat? = null

    companion object {
        fun startDailyReadingService(context: Context, dailyReadingFile: String) {
            val intent = Intent(context, DailyReadingService::class.java)
            intent.action = Constants.actionPlayDailyReading

            val sharedPreference =
                context.getSharedPreferences("TABERNACULO_CAMPINAS", Context.MODE_PRIVATE)

            val editor = sharedPreference?.edit()
            editor?.putString("daily_reading_file", dailyReadingFile)
            editor?.apply()

            context.startService(intent)
        }

        fun stopDailyReadingService(context: Context) {
            val intent = Intent(context, DailyReadingService::class.java)

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
            stopPlayer()
            mediaPlayer!!.release()
        }
    }

    fun playPlayer() {
        if (mediaPlayer == null) {
            val sharedPreference =
                baseContext.getSharedPreferences("TABERNACULO_CAMPINAS", Context.MODE_PRIVATE)

            dailyReadingUrl = sharedPreference!!.getString("daily_reading_file", "")!!

            mediaPlayer = ExoPlayer.Builder(this).build()
        }

        if (mediaSessionManager == null)
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

    fun pausePlayer() {
        if (mediaPlayer == null)
            return

        if (mediaPlayer!!.playbackState != PlaybackStats.PLAYBACK_STATE_STOPPED)
            mediaPlayer!!.pause()

        stopService()
    }

    fun stopPlayer() {
        if (mediaPlayer == null)
            return

        if (mediaPlayer!!.playbackState != PlaybackStats.PLAYBACK_STATE_STOPPED)
            mediaPlayer!!.stop()

        stopService()
    }

    private fun buildMediaSource(): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(dailyReadingUrl))
    }

    private fun loadMediaSession() {
        if (mediaSessionManager != null)
            return

        mediaSessionManager = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager
        mediaSessionCompat = MediaSessionCompat(this, "AudioPlayer")

        val activity = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, activity, PendingIntent.FLAG_IMMUTABLE)

        mediaSessionCompat!!.isActive = true
        mediaSessionCompat!!.setSessionActivity(pendingIntent)
        mediaSessionCompat!!.setCallback(DailyReadingCallback(this))
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
            .setContentText("Leitura Diária da Bíblia")
            .setContentTitle("Tabernáculo Campinas")
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

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
                    R.drawable.ic_stop, "Parar", actionPlayback(1)
                )

                startForeground(notificationId, notificationBuilder!!.build())


                val intent = Intent(Constants.actionPlayingDailyReading)
                intent.putExtra("total_time", mediaPlayer!!.duration)

                sendBroadcast(intent)
            }
            PlaybackStats.PLAYBACK_STATE_STOPPED -> {
                notificationBuilder!!.addAction(
                    R.drawable.ic_play, "Iniciar", actionPlayback(0)
                )

                sendBroadcast(Intent(Constants.actionStopDailyReading))
            }
        }
    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_DETACH)
        configureNotification(PlaybackStats.PLAYBACK_STATE_STOPPED)
    }

    private fun actionPlayback(actionNumber: Int): PendingIntent? {
        val intent = Intent(this, DailyReadingService::class.java)

        when (actionNumber) {
            0 -> {
                intent.action = Constants.actionPlayDailyReading

                return PendingIntent.getService(
                    this, actionNumber, intent, PendingIntent.FLAG_IMMUTABLE
                )
            }
            1 -> {
                intent.action = Constants.actionStopDailyReading

                return PendingIntent.getService(
                    this, actionNumber, intent, PendingIntent.FLAG_IMMUTABLE
                )
            }
        }

        return null
    }

    private fun treatIntentions(intent: Intent) {
        if (intent.action.isNullOrEmpty())
            return

        val actionString = intent.action

        if (actionString.equals(Constants.actionPlayDailyReading))
            playPlayer()

        if (actionString.equals(Constants.actionStopDailyReading))
            stopPlayer()

        if (actionString.equals(Constants.actionStopDailyReading))
            stopPlayer()
    }
}

class DailyReadingCallback(private val dailyReadingService: DailyReadingService) :
    MediaSessionCompat.Callback() {

    override fun onPlay() {
        super.onPlay()

        dailyReadingService.playPlayer()
    }

    override fun onStop() {
        super.onStop()

        dailyReadingService.stopPlayer()
    }

    override fun onPause() {
        super.onPause()

        dailyReadingService.pausePlayer()
    }
}