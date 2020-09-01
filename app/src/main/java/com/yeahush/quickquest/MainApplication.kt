package com.yeahush.quickquest

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.ads.*
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private lateinit var player: MediaPlayer
    private var interstitialAd: InterstitialAd? = null

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initMediaPlayer()
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) {}
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        interstitialAd = newInterstitialAd()
        loadInterstitial()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun initMediaPlayer() {
        Timber.d("initMediaPlayer")
        try {
            player = MediaPlayer.create(applicationContext, R.raw.bgm).apply {
                setAudioAttributes(
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = true
                setVolume(1f, 1f)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun playBgMusic() {
        try {
            if (!player.isPlaying) {
                player.start()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            initMediaPlayer()
            player.start()
        }
    }

    fun pauseBgMusic() {
        if (player.isPlaying) {
            player.pause()
        }
    }

    private fun newInterstitialAd(): InterstitialAd {
        return InterstitialAd(this).apply {
            adUnitId = getString(R.string.interstitial_ad_unit_id)
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    val error = "domain: ${loadAdError.domain}, code: ${loadAdError.code}, " +
                            "message: ${loadAdError.message}"
                    Timber.d("onAdFailedToLoad() with error $error")
                }

                override fun onAdClosed() {
                    reloadAd()
                }
            }
        }
    }

    fun showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (interstitialAd?.isLoaded == true) {
            interstitialAd?.show()
        } else {
            reloadAd()
        }
    }

    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder()
            .setRequestAgent("android_studio:ad_template")
            .build()
        interstitialAd?.loadAd(adRequest)
    }

    private fun reloadAd() {
        interstitialAd = newInterstitialAd()
        loadInterstitial()
    }
}