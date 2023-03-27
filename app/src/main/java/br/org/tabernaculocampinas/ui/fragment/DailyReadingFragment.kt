package br.org.tabernaculocampinas.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.databinding.FragmentDailyReadingBinding
import br.org.tabernaculocampinas.databinding.FragmentRadioBinding
import br.org.tabernaculocampinas.model.tabernacle.Streaming
import br.org.tabernaculocampinas.ui.bottomsheet.BottomSheetWebRadio
import br.org.tabernaculocampinas.ui.service.WebRadioService
import com.google.gson.Gson

class DailyReadingFragment : Fragment() {
    private lateinit var binding: FragmentDailyReadingBinding

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
    }

    private fun loadInformations() {
        with(binding){

        }
    }

    private fun configureDailyReadingButton() {
        with(binding) {
            layoutPlayerDailyReading.setOnClickListener {
                if (playingDailyReading) {
                    playingDailyReading = false

                    imgPlayerDailyReading.setImageResource(R.drawable.ic_play)

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

                    playingDailyReading = true

                    imgPlayerDailyReading.setImageResource(R.drawable.ic_pause)

                    WebRadioService.startRadioService(requireContext(), streaming.radioUrl)
                }
            }
        }
    }
}