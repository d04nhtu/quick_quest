package com.yeahush.quickquest

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    private lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        Timber.d("initMediaPlayer")
        try {
            player = MediaPlayer.create(applicationContext, R.raw.lights).apply {
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
}