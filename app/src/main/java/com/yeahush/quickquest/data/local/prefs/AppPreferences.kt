package com.yeahush.quickquest.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.yeahush.quickquest.R
import com.yeahush.quickquest.utilities.COMMON_PREFERENCE
import com.yeahush.quickquest.utilities.FIRST_LAUNCH
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AppPreferences @Inject constructor(private val context: Context) {
    //Create shared preference object on first use
    private val preference: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(COMMON_PREFERENCE, Context.MODE_PRIVATE)
    }

    private val settings: Lazy<SharedPreferences> = lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getSignature(): String {
        return settings.value.getString(context.getString(R.string.signature_title), "You")!!
    }

    fun isSoundEnable(): Boolean {
        return settings.value.getBoolean(context.getString(R.string.sound_title), true)
    }

    fun isBgMusicEnable(): Boolean {
        return settings.value.getBoolean(context.getString(R.string.bg_music_title), true)
    }

    fun isVibrateEnable(): Boolean {
        return settings.value.getBoolean(context.getString(R.string.vibrate_title), true)
    }

    var firstLaunch by BooleanPreference(
        preference,
        FIRST_LAUNCH,
        false
    )
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        preferences.value.getBoolean(name, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value).apply() }
    }
}