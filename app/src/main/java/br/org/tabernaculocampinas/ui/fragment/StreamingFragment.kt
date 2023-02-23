package br.org.tabernaculocampinas.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.databinding.FragmentStreamingBinding

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
    }

    private fun configureStreaming() {

    }
}