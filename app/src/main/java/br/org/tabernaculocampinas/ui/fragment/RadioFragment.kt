package br.org.tabernaculocampinas.ui.fragment

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.broadcastreceiver.ActionPlayingRadioFragmentReceiver
import br.org.tabernaculocampinas.broadcastreceiver.ActionStopRadioFragmentReceiver
import br.org.tabernaculocampinas.databinding.FragmentRadioBinding
import br.org.tabernaculocampinas.model.tabernacle.Streaming
import br.org.tabernaculocampinas.ui.service.WebRadioService
import com.google.gson.Gson

class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding

    private var playingWebRadio = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRadioBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        loadInformations()
        configureWebRadioButton()
        registerBroadcastReceivers()
    }

    private fun loadInformations() {
        with(binding) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureWebRadioButton() {
        with(binding) {
            layoutPlayerWebRadio.setOnClickListener {
                if (playingWebRadio) {
                    WebRadioService.stopRadioService(requireContext())
                } else {
                    val sharedPreference =
                        requireContext().getSharedPreferences(
                            "TABERNACULO_CAMPINAS", Context.MODE_PRIVATE
                        )

                    val streaming = Gson().fromJson(
                        sharedPreference.getString("streaming", ""),
                        Streaming::class.java
                    )

                    WebRadioService.startRadioService(requireContext(), streaming.radioUrl)
                }
            }
        }
    }

    private fun registerBroadcastReceivers() {
        requireContext().registerReceiver(
            ActionStopRadioFragmentReceiver(this),
            IntentFilter(br.org.tabernaculocampinas.extension.Constants.actionStopRadio)
        )

        requireContext().registerReceiver(
            ActionPlayingRadioFragmentReceiver(this),
            IntentFilter(br.org.tabernaculocampinas.extension.Constants.actionPlayingRadio)
        )
    }

    fun changeRadioStoppedControls() {
        playingWebRadio = false

        with(binding) {
            imgPlayerWebRadio.setImageResource(R.drawable.ic_play)
            layoutWebRadioPlaying.visibility = View.INVISIBLE
        }
    }

    fun changeRadioPlayingControls() {
        playingWebRadio = true

        with(binding) {
            imgPlayerWebRadio.setImageResource(R.drawable.ic_stop)
            layoutWebRadioPlaying.visibility = View.VISIBLE
        }
    }
}