package br.org.tabernaculocampinas.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.databinding.FragmentStreamingBinding
import br.org.tabernaculocampinas.model.tabernacle.Streaming
import br.org.tabernaculocampinas.ui.service.WebRadioService
import com.google.gson.Gson

class StreamingFragment : Fragment() {
    private lateinit var binding: FragmentStreamingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamingBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        configureStreaming()
        configureOnlyAudioButton()
        configureOnlyAudioPlayingButton()
    }

    private fun configureStreaming() {

    }

    private fun configureOnlyAudioButton() {
        with(binding) {
            buttonStreamOnlyAudio.setOnClickListener {
                val sharedPreference =
                    requireContext().getSharedPreferences(
                        "TABERNACULO_CAMPINAS", Context.MODE_PRIVATE
                    )

                val streaming = Gson().fromJson(
                    sharedPreference.getString("streaming", ""),
                    Streaming::class.java
                )

                buttonStreamOnlyAudio.visibility = View.GONE
                buttonStreamOnlyAudioPlaying.visibility = View.VISIBLE

                WebRadioService.startRadioService(requireContext(), streaming.radioUrl)
            }
        }
    }

    private fun configureOnlyAudioPlayingButton() {
        with(binding) {
            buttonStreamOnlyAudioPlaying.setOnClickListener {
                buttonStreamOnlyAudio.visibility = View.VISIBLE
                buttonStreamOnlyAudioPlaying.visibility = View.GONE

                WebRadioService.stopRadioService(requireContext())
            }
        }
    }
}