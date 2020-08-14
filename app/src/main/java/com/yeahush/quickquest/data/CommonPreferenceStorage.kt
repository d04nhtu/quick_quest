package com.yeahush.quickquest.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.yeahush.quickquest.utilities.COMMON_PREFERENCE
import com.yeahush.quickquest.utilities.FIRST_LAUNCH
import com.yeahush.quickquest.utilities.MODE_REVIEW
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CommonPreferenceStorage @Inject constructor(context: Context) {
    //Create shared preference object on first use
    private val preference: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(COMMON_PREFERENCE, Context.MODE_PRIVATE)
    }

    var firstLaunch by BooleanPreference(preference, FIRST_LAUNCH, false)

    var modeReview by BooleanPreference(preference, MODE_REVIEW, false)
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