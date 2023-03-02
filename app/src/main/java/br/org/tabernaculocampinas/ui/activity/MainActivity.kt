package br.org.tabernaculocampinas.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.databinding.ActivityMainBinding
import br.org.tabernaculocampinas.model.tabernacle.Streaming
import br.org.tabernaculocampinas.ui.fragment.adapter.TabHomeAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onResume() {
        super.onResume()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        configureTab()
    }

    private fun configureTab() {
        val sharedPreference =
            baseContext.getSharedPreferences("TABERNACULO_CAMPINAS", Context.MODE_PRIVATE)

        val streamingString = sharedPreference?.getString("streaming", "")
        val streaming = Gson().fromJson(streamingString, Streaming::class.java)

        val adapter = TabHomeAdapter(this, streaming.live)

        with(binding) {
            viewPagerMain.adapter = adapter

            TabLayoutMediator(tabLayoutMain, viewPagerMain) { tab, position ->
                when (position) {
                    0 -> tab.icon =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_book)
                    1 ->
                        tab.icon =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_chat)
                    2 ->
                        tab.icon =
                            ContextCompat.getDrawable(
                                this@MainActivity,
                                R.drawable.ic_ondemand_video
                            )
                    3 ->
                        tab.icon =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_menu_book)
                    4 -> tab.icon =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_library_music)
                }
            }.attach()

            tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.icon?.setTint(baseContext.getColor(R.color.philippin_bronze))
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.icon?.setTint(baseContext.getColor(R.color.quick_silver))
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    tab?.icon?.setTint(baseContext.getColor(R.color.philippin_bronze))
                }
            })

            tabLayoutMain.getTabAt(2)?.select()
        }
    }
}