package br.org.tabernaculocampinas.ui.fragment

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.broadcastreceiver.PauseDailyReadingFragmentReceiver
import br.org.tabernaculocampinas.broadcastreceiver.PlayDailyReadingFragmentReceiver
import br.org.tabernaculocampinas.databinding.FragmentDailyReadingBinding
import br.org.tabernaculocampinas.extension.Constants
import br.org.tabernaculocampinas.model.tabernacle.Streaming
import br.org.tabernaculocampinas.ui.service.DailyReadingService
import com.google.gson.Gson

class DailyReadingFragment : Fragment() {
    lateinit var binding: FragmentDailyReadingBinding

    private var playingDailyReading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyReadingBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        loadInformations()
        configureDailyReadingButton()
        configureDailyReadingSeekBar()
        registerBroadcastReceivers()
    }

    private fun loadInformations() {
        with(binding) {

        }
    }

    private fun configureDailyReadingButton() {
        with(binding) {
            layoutPlayerDailyReading.setOnClickListener {
                if (playingDailyReading) {
                    changeRadioPausedControls()

                    DailyReadingService.stopDailyReadingService(requireContext())
                } else {
                    val sharedPreference =
                        requireContext().getSharedPreferences(
                            "TABERNACULO_CAMPINAS", Context.MODE_PRIVATE
                        )

                    val streaming = Gson().fromJson(
                        sharedPreference.getString("streaming", ""),
                        Streaming::class.java
                    )

                    changeRadioPlayingControls()

                    DailyReadingService.startDailyReadingService(
                        requireContext(),
                        streaming.dailyReadingFile
                    )
                }
            }
        }
    }

    private fun configureDailyReadingSeekBar() {
        with(binding) {
            seekDailyReading.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    // here, you react to the value being set in seekBar
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // you can probably leave this empty
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // you can probably leave this empty
                }
            })
        }
    }

    private fun registerBroadcastReceivers() {
        requireContext().registerReceiver(
            PlayDailyReadingFragmentReceiver(this),
            IntentFilter(Constants.actionPlayDailyReading)
        )

        requireContext().registerReceiver(
            PauseDailyReadingFragmentReceiver(this),
            IntentFilter(Constants.actionPauseDailyReading)
        )
    }

    fun changeRadioPausedControls() {
        playingDailyReading = false

        with(binding) {
            imgPlayerDailyReading.setImageResource(R.drawable.ic_play)
            layoutDailyReadingPlaying.visibility = View.INVISIBLE
        }
    }

    fun changeRadioPlayingControls() {
        playingDailyReading = true

        with(binding) {
            imgPlayerDailyReading.setImageResource(R.drawable.ic_pause)
            layoutDailyReadingPlaying.visibility = View.VISIBLE
        }
    }
}