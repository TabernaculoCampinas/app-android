package br.org.tabernaculocampinas.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.org.tabernaculocampinas.R
import br.org.tabernaculocampinas.databinding.FragmentStreamingBinding
import br.org.tabernaculocampinas.model.tabernacle.Streaming
import br.org.tabernaculocampinas.ui.service.WebRadioService
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeIntents
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
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
        val appPackageName = "com.google.android.youtube"

        val isYouTubeInstalled = YouTubeIntents.canResolvePlayVideoIntent(requireContext())

        if (isYouTubeInstalled) {
            val playerStream = binding.playerStream
            playerStream.initialize("AIzaSyC3H9oJkU534cC83P4Nb94S-sGfT4u-iRI", object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(view: YouTubeThumbnailView, loader: YouTubeThumbnailLoader) {
                    loader.setVideo("FpL7xj4xjeA")
                    loader.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                        override fun onThumbnailLoaded(thumbnailView: YouTubeThumbnailView, thumbnail: String) {
                            binding.imagePlayerStream.setImageURI(Uri.parse(thumbnail))
                        }

                        override fun onThumbnailError(thumbnailView: YouTubeThumbnailView, errorReason: YouTubeThumbnailLoader.ErrorReason) {
                            Toast.makeText(context, errorReason.name, Toast.LENGTH_SHORT)
                        }
                    })
                }

                override fun onInitializationFailure(view: YouTubeThumbnailView, errorReason: YouTubeInitializationResult) {
                    Toast.makeText(context, errorReason.name, Toast.LENGTH_SHORT)
                }
            })
        } else {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        val packageManager = requireContext().packageManager
        try {
            packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
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