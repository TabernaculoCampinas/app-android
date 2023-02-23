package br.org.tabernaculocampinas.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.org.tabernaculocampinas.databinding.BottomSheetWebRadioBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetWebRadio : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetWebRadioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetWebRadioBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {
        const val TAG = "BottomSheetWebRadio"
    }
}