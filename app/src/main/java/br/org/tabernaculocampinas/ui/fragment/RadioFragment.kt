package br.org.tabernaculocampinas.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.databinding.FragmentRadioBinding
import br.org.tabernaculocampinas.ui.bottomsheet.BottomSheetWebRadio

class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRadioBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        loadInformations()
        configureWebRadioButton()
        configureDailyReadingButton()
    }

    private fun loadInformations() {
        with(binding){

        }
    }

    private fun configureWebRadioButton() {
        with(binding) {
            layoutPlayerWebRadio.setOnClickListener {
                //layoutNowPlaying.visibility = View.VISIBLE
            }
        }
    }

    private fun configureDailyReadingButton() {
        with(binding) {
            layoutPlayerDailyReading.setOnClickListener {
                //layoutDailyReadingPlaying.visibility = View.VISIBLE
            }
        }
    }
}