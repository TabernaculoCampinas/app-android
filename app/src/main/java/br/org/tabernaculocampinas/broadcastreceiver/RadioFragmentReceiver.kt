package br.org.tabernaculocampinas.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.org.tabernaculocampinas.ui.fragment.RadioFragment

class ActionStopRadioFragmentReceiver(private val radioFragment: RadioFragment) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        radioFragment.changeRadioStoppedControls()
    }
}

class ActionPlayingRadioFragmentReceiver(private val radioFragment: RadioFragment) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        radioFragment.changeRadioPlayingControls()
    }

}