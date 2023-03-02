package br.org.tabernaculocampinas.ui.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource


class WebRadioService : Service() {
    var mediaPlayer: ExoPlayer? = null

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)




    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun configurePlayer(webRadioUrl: String){
        mediaPlayer = ExoPlayer.Builder(this,).build()



    }
}

data class LocalBinder(
    val service: WebRadioService
) : Binder()

class WebRadioCallback : MediaSessionCompat.Callback(){
    override fun onPlay() {
        super.onPlay()
    }

    override fun onStop() {
        super.onStop()
    }
}