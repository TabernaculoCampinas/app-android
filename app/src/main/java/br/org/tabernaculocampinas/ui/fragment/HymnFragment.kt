package br.org.tabernaculocampinas.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.databinding.FragmentHymnBinding

class HymnFragment : Fragment() {
    private lateinit var binding: FragmentHymnBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHymnBinding.inflate(layoutInflater)

        return binding.root
    }
}