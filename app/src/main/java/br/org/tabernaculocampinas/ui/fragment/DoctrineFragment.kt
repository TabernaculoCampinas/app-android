package br.org.tabernaculocampinas.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.databinding.FragmentDoctrineBinding

class DoctrineFragment : Fragment() {
    private lateinit var binding: FragmentDoctrineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctrineBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        configureWebView()
    }

    private fun configureWebView() {
        binding.webDoctrine.loadUrl("https://www.tabernaculocampinas.org.br/doutrina/")
    }
}