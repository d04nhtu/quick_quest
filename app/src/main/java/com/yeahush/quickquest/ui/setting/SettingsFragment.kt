package com.yeahush.quickquest.ui.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.yeahush.quickquest.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferenceTerm = findPreference<Preference>(getString(R.string.term_title))
        preferenceTerm?.setOnPreferenceClickListener {
            showDialogTerm()
            false
        }

        val preferenceAbout = findPreference<Preference>(getString(R.string.about_title))
        preferenceAbout?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settings_to_about)
            false
        }

        val preferenceContact = findPreference<Preference>(getString(R.string.contact_title))
        preferenceContact?.setOnPreferenceClickListener {
            val manager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText(
                getString(R.string.contact_title),
                getString(R.string.developer_email)
            )
            manager.setPrimaryClip(data)
            Toast.makeText(context, "Email Copied to Clipboard", Toast.LENGTH_SHORT).show();
            false
        }
    }

    private fun showDialogTerm() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.term_title))
        builder.setMessage(getString(R.string.term_content))
        builder.setPositiveButton(R.string.ok_button, null)
        builder.show()
    }
}