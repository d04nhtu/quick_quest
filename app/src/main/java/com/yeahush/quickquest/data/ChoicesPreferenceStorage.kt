package com.yeahush.quickquest.data

import android.content.Context
import android.content.SharedPreferences
import com.yeahush.quickquest.utilities.CHOICES_PREFERENCE
import javax.inject.Inject

/**
 * [ChoicesPreferenceStorage] manages the choices of user for the app.
 */
class ChoicesPreferenceStorage @Inject constructor(context: Context) {

    //Create shared preference object on first use
    private val preference: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(CHOICES_PREFERENCE, Context.MODE_PRIVATE)
    }

    fun setChoice(key: String, value: String) {
        with(preference.value.edit()) { putString(key, value).apply() }
    }

    fun getChoice(key: String) = preference.value.getString(key, "")!!

    fun removeChoices() {
        with(preference.value.edit()) { clear().apply() }
    }
}


