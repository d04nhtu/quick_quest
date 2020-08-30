# Quick Quest
**Android app for multiple choice questions, consisting of two sources of questions. One is prepopulated from a local json file and the other is pulled from <a href="https://opentdb.com/api_config.php">Trivia API</a> over the Internet.**

<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" align="left" width="128" hspace="10" vspace="10">

Quick Quest is available for free on the Google Play Store.

<a href="https://play.google.com/store/apps/details?id=com.yeahush.quickquest">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a>

##  Screenshots
<p align="middle">
  <img width="120" src="https://lh3.googleusercontent.com/fxFUpddjuPFrJnOu5fsXzalaU03bTQ6KRavqqvjTQI1C7H6b2esKTW0ab4GXJZxSrlg=w1536-h671-rw">
  <img width="120" src="https://lh3.googleusercontent.com/mqXvDO8D3z3y-gSIcX1llqByIUCgWY6hPA6GYcWrn69loqdxU89lfTTAm1neRuXgIA=w1536-h671-rw">
  <img width="120" src="https://lh3.googleusercontent.com/k3cFQE7O6biSqqo5UOSa0HKpn_DX7Yr4Tn84bbd2-8a58GwSRQzVZ6eGz-ym-4chnKXh=w1536-h671-rw">
  <img width="120" src="https://lh3.googleusercontent.com/RH3Uler6KxtiYnftrPRcd3CTDsCYj5r0IvoE1TpPRPjUnDUqgTsQvdHHBtck0U8QWFc=w1536-h671-rw">
  <img width="120" src="https://lh3.googleusercontent.com/mamYMesMA53HxoDfzUZRF_aQHpmfTsWbVv_0t6hRN41Or_agu3gHbWCahdqDgQbyQzo=w1536-h671-rw">
  <img width="120" src="https://lh3.googleusercontent.com/9tyopjilMfqOatkBBMLw776kNXUbJRS853SRmobHCMXFVgZfOCBbWzesr2vIjaQ7pQ=w1536-h671-rw">
  <img width="120" src="https://lh3.googleusercontent.com/1OsAeVUWEN7U8eaMoaozsQ_AfJnyfA5mltI1Qt5m_oPZ8DNh71syGMks_NWM-ENEZJs=w1536-h671-rw">
  <img width="120" src="https://lh3.googleusercontent.com/glXmy2KM3jivBF5r-3Xtx_Yoana1XnHJF06hS4uSuNzih3v0FpdU4aZyUg7WtGosSyxL=w1536-h671-rw">
</p>

## 📃 Libraries Used

* [Architecture][1] - A collection of libraries that help you design robust, testable, and
  maintainable apps.
  * [Data Binding][2] - Declaratively bind observable data to UI elements.
  * [LiveData][3] - Build data objects that notify views when the underlying database changes.
  * [Room][4] - Access your app's SQLite database with in-app objects and compile-time checks.
  * [ViewModel][5] - Store UI-related data that isn't destroyed on app rotations. Easily schedule
     asynchronous tasks for optimal execution.
  * [Hilt][6] - Providing a standard way to incorporate Dagger dependency injection into an Android application.
  * [Navigation][12] - Handle everything needed for in-app navigation.
  
* Third party
  * [Glide][7] - An image loading and caching list_item_library for Android focused onsmooth scrolling.
  * [Kotlin Coroutines][8] - Managing background threads with simplified code and reducing needs for callbacks.
  * [Moshi][9] - Modern JSON library for Android and Java.
  * [Retrofit][10] - A type-safe HTTP client for Android and Java.

[1]: https://developer.android.com/jetpack/arch/
[2]: https://developer.android.com/topic/libraries/data-binding/
[3]: https://developer.android.com/topic/libraries/architecture/livedata
[4]: https://developer.android.com/topic/libraries/architecture/room
[5]: https://developer.android.com/topic/libraries/architecture/viewmodel
[6]: https://developer.android.com/training/dependency-injection/hilt-android
[7]: https://bumptech.github.io/glide/
[8]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[9]: https://github.com/square/moshi
[10]: https://github.com/square/retrofit
[11]: https://developer.android.com/training/dependency-injection/hilt-android
[12]: https://developer.android.com/topic/libraries/architecture/navigation/

## 📝 License
This project is released under the MIT license.
See [LICENSE](./LICENSE) for details.

```
MIT License

Copyright (c) 2020 d04nhtu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
