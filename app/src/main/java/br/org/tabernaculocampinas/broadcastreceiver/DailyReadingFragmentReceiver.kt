package br.org.tabernaculocampinas.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.org.tabernaculocampinas.ui.fragment.DailyReadingFragment

class PlayDailyReadingFragmentReceiver(private val dailyReadingFragment: DailyReadingFragment) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        dailyReadingFragment.changeRadioPlayingControls()

        val totalTime = intent!!.getIntExtra("total_time", 0)

        dailyReadingFragment.binding.seekDailyReading.max = totalTime
        dailyReadingFragment.binding.seekDailyReading.progress = 0
    }
}

class LoadingDailyReadingFragmentReceiver(private val dailyReadingFragment: DailyReadingFragment) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        dailyReadingFragment.changeRadioLoadingControls()
    }
}

class PauseDailyReadingFragmentReceiver(private val dailyReadingFragment: DailyReadingFragment) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        dailyReadingFragment.changeRadioPausedControls()
    }
}