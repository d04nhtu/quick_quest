package com.yeahush.quickquest.utilities

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

fun isDarkTheme(context: Context): Boolean {
    return (context.resources.configuration.uiMode
            and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

fun showToast(context: Context?, message: String) {
    context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
}

fun playResourceSound(context: Context?, resourceId: Int) {
    context?.let {
        var mediaPlayer: MediaPlayer?
        mediaPlayer = MediaPlayer.create(it, resourceId)
        mediaPlayer.setOnCompletionListener {
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }
}

fun vibrate(context: Context?) {
    context?.let {
        val vibrator = it.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(VIBRATION_TIME, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(VIBRATION_TIME)
        }
    }
}