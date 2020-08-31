package com.yeahush.quickquest.ui.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.yeahush.quickquest.MainApplication
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import com.yeahush.quickquest.utilities.playResourceSound
import com.yeahush.quickquest.utilities.showToast
import com.yeahush.quickquest.utilities.vibrate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferenceBgMusic = findPreference<SwitchPreference>(getString(R.string.bg_music_title))
        preferenceBgMusic?.setOnPreferenceChangeListener { _, newValue ->
            val app = activity?.application as MainApplication
            if (newValue as Boolean) app.playBgMusic()
            else app.pauseBgMusic()
            true
        }

        val preferenceSound = findPreference<SwitchPreference>(getString(R.string.sound_title))
        preferenceSound?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) playResourceSound(context, R.raw.click)
            true
        }

        val preferenceVibrate = findPreference<SwitchPreference>(getString(R.string.vibrate_title))
        preferenceVibrate?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) vibrate(context)
            true
        }

        findPreference<Preference>(getString(R.string.signature_title))?.onPreferenceClickListener =
            this
        findPreference<Preference>(getString(R.string.term_title))?.onPreferenceClickListener = this
        findPreference<Preference>(getString(R.string.about_title))?.onPreferenceClickListener =
            this
        findPreference<Preference>(getString(R.string.contact_title))?.onPreferenceClickListener =
            this
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        if (appPreferences.isSoundEnable()) playResourceSound(context, R.raw.click)
        if (appPreferences.isVibrateEnable()) vibrate(context)
        when (preference?.key) {
            resources.getString(R.string.term_title) -> showDialogTerm()
            resources.getString(R.string.about_title) ->
                findNavController().navigate(R.id.action_settings_to_about)
            resources.getString(R.string.contact_title) -> {
                val manager =
                    context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val data = ClipData.newPlainText(
                    getString(R.string.contact_title),
                    getString(R.string.developer_email)
                )
                manager.setPrimaryClip(data)
                context?.let { showToast(it, "Email Copied to Clipboard") }
            }
        }
        return false
    }

    private fun showDialogTerm() {
        val builder = context?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setTitle(getString(R.string.term_title))
            builder.setMessage(getString(R.string.term_content))
            builder.setPositiveButton(R.string.ok_button, null)
            builder.show()
        }
    }
}