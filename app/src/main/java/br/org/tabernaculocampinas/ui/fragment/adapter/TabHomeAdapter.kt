package br.org.tabernaculocampinas.ui.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.org.tabernaculocampinas.ui.fragment.DoctrineFragment
import br.org.tabernaculocampinas.ui.fragment.RadioFragment
import br.org.tabernaculocampinas.ui.fragment.HymnFragment
import br.org.tabernaculocampinas.ui.fragment.StreamingFragment

private const val NUM_TABS = 3

class TabHomeAdapter(fragment: FragmentActivity, live: Boolean) : FragmentStateAdapter(fragment) {
    val liveVal = live

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return DoctrineFragment()
            2 -> return HymnFragment()
        }

        return if (liveVal)
            StreamingFragment()
        else
            RadioFragment()
    }
}