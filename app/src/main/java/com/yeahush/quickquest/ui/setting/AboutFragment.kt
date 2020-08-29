package com.yeahush.quickquest.ui.setting

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.model.Library
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import com.yeahush.quickquest.utilities.playResourceSound
import com.yeahush.quickquest.utilities.vibrate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

@AndroidEntryPoint
class AboutFragment : Fragment() {
    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arch_content.text = HtmlCompat.fromHtml(
            resources.getString(R.string.arch_content),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        source.text = HtmlCompat.fromHtml(
            resources.getString(R.string.source),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        intro.text = HtmlCompat.fromHtml(
            resources.getString(R.string.intro),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        purpose.text = HtmlCompat.fromHtml(
            resources.getString(R.string.purpose), HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        arch_content.movementMethod = LinkMovementMethod.getInstance()
        source.movementMethod = LinkMovementMethod.getInstance()
        intro.movementMethod = LinkMovementMethod.getInstance()
        purpose.movementMethod = LinkMovementMethod.getInstance()

        val libraryAdapter = LibraryAdapter {
            if (appPreferences.isSoundEnable()) playResourceSound(context, R.raw.click)
            if (appPreferences.isVibrateEnable()) vibrate(context)
            startActivity(Intent(Intent.ACTION_VIEW, it.link.toUri()))
        }
        library_list.adapter = libraryAdapter
        library_list.layoutManager = object :
            LinearLayoutManager(context, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.width = (width * 3) / 4
                return true
            }
        }
        libraryAdapter.setLibraries(libraries)
    }

    private val libraries = listOf(
        Library(
            "Android Jetpack",
            "Android Jetpack offer a number of features that are " +
                    "not built into the framework.",
            "https://developer.android.com/jetpack/",
            "https://4.bp.blogspot.com/-NnAkV5vpYuw/XNMYF4RtLvI/AAAAAAAAI70/kdgLm3cnTO4FB4rUC0v9smscN3zHJPlLgCLcBGAs/s1600/Jetpack_logo%2B%25282%2529.png",
            false
        ),
        Library(
            "android-ktx",
            "A set of Kotlin extensions for Android app development.",
            "https://android.googlesource.com/platform/frameworks/support/",
            "https://avatars.githubusercontent.com/u/32689599",
            false
        ),
        Library(
            "Hilt",
            "Hilt provides a standard way to incorporate Dagger dependency injection " +
                    "into an Android application.",
            "https://dagger.dev/hilt/",
            "https://avatars.githubusercontent.com/u/1342004",
            true
        ),
        Library(
            "Glide",
            "An image loading and caching list_item_library for Android focused onsmooth " +
                    "scrolling.",
            "https://github.com/bumptech/glide",
            "https://avatars.githubusercontent.com/u/423539",
            false
        ),
        Library(
            "Mockito",
            "Tasty mocking framework for unit tests in Java",
            "http://site.mockito.org/",
            "https://avatars3.githubusercontent.com/u/2054056?s=200&v=4",
            false

        ),
        Library(
            "Mockito-Kotlin",
            "A small list_item_library that provides helper functions to work with Mockito in Kotlin.",
            "https://github.com/nhaarman/mockito-kotlin",
            "https://avatars.githubusercontent.com/u/3015152",
            true
        ),
        Library(
            "Moshi",
            "Modern JSON library for Android and Java.",
            "https://github.com/square/moshi",
            "https://avatars.githubusercontent.com/u/82592",
            false
        ),
        Library(
            "Retrofit",
            "A type-safe HTTP client for Android and Java.",
            "http://square.github.io/retrofit/",
            "https://avatars.githubusercontent.com/u/82592",
            false
        )
    )
}