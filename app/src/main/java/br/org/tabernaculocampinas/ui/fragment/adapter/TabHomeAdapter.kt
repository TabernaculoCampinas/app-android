package br.org.tabernaculocampinas.ui.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.org.tabernaculocampinas.ui.fragment.*

private const val NUM_TABS = 5

class TabHomeAdapter(fragment: FragmentActivity, live: Boolean) : FragmentStateAdapter(fragment) {
    val liveVal = live

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return DoctrineFragment()
            1 -> return PrayRequestFragment()
            3 -> return DailyReadingFragment()
            4 -> return HymnFragment()
        }

        return if (liveVal)
            StreamingFragment()
        else
            RadioFragment()

        //return StreamingFragment()
        //return RadioFragment()
    }
}