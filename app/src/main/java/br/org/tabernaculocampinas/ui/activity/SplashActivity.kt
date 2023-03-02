package br.org.tabernaculocampinas.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.org.tabernaculocampinas.databinding.ActivitySplashBinding
import br.org.tabernaculocampinas.model.tabernacle.Streaming
import br.org.tabernaculocampinas.service.RetrofitHelper
import br.org.tabernaculocampinas.service.tabernacle.StreamService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onResume() {
        super.onResume()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivitySplashBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val retrofitHelper = RetrofitHelper.createRetrofitInstanceTabernacle()

        val endpoint = retrofitHelper.create(StreamService::class.java)
        val callback = endpoint.getStream()

        callback?.enqueue(object : Callback<Streaming> {
            override fun onFailure(call: Call<Streaming>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Streaming>, response: Response<Streaming>
            ) {
                if (response.isSuccessful) {
                    val sharedPreference =
                        baseContext.getSharedPreferences(
                            "TABERNACULO_CAMPINAS", Context.MODE_PRIVATE
                        )

                    val editor = sharedPreference?.edit()
                    editor?.putString("streaming", Gson().toJson(response.body()))
                    editor?.apply()
                } else {
                    Toast.makeText(baseContext, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}