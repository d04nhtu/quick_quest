package com.yeahush.quickquest.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.model.Library
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val libraryAdapter = LibraryAdapter {
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
            "Bypass",
            "Skip the HTML, Bypass takes markdown and renders it directly.",
            "https://github.com/Uncodin/bypass",
            "https://avatars.githubusercontent.com/u/1072254",
            true
        ),
        Library(
            "Firebase Crashlytics",
            "The most powerful, yet lightest weight crash reporting solution.",
            "https://firebase.google.com/products/crashlytics/",
            "https://www.gstatic.com/mobilesdk/160503_mobilesdk/logo/2x/firebase_96dp.png",
            false
        ),
        Library(
            "Dagger2",
            "Dagger is a fully static, compile-time dependency injection framework" +
                    "for both Java and Android.",
            "https://google.github.io/dagger/",
            "https://avatars.githubusercontent.com/u/1342004",
            true
        ),
        Library(
            "Firebase",
            "A comprehensive mobile development platform",
            "https://firebase.google.com/",
            "https://www.gstatic.com/mobilesdk/160503_mobilesdk/logo/2x/firebase_96dp.png",
            false
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
            "JSoup",
            "Java HTML Parser, with best of DOM, CSS, and jquery.",
            "https://github.com/jhy/jsoup/",
            "https://avatars.githubusercontent.com/u/76934",
            true
        ),
        Library(
            "ktlint",
            "An anti-bikeshedding Kotlin linter with built-in formatter",
            "https://github.com/shyiko/ktlint",
            "https://avatars.githubusercontent.com/u/370176",
            true
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
            "OkHttp",
            "An HTTP & HTTP/2 client for Android and Java applications.",
            "http://square.github.io/okhttp/",
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