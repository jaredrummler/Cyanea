# Cyanea

<img src="https://i.imgur.com/eC6d5WO.gif" align="left" hspace="10" vspace="10"></a>

## A theme engine for Android

<a target="_blank" href="LICENSE"><img src="http://img.shields.io/:license-apache-blue.svg" alt="License" /></a>
<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea"><img src="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea/badge.svg" alt="Maven Central" /></a>
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>
<a target="_blank" href="https://twitter.com/jaredrummler"><img src="https://img.shields.io/twitter/follow/jaredrummler.svg?style=social" /></a>

# About

A powerful, dynamic, and fun theme engine. Named after Octopus Cyanea which is adept at camouflage and not only can change color frequently, but also can change the patterns on and texture of its skin.

# Downloads

Download [the latest AAR](https://repo1.maven.org/maven2/com/jaredrummler/cyanea/1.0.0/cyanea-1.0.0.aar) or grab via Gradle:

```groovy
implementation 'com.jaredrummler:cyanea:1.0.0'
```

## Demo

You can download an APK of the sample project

<br>

# Setup

To integrate Cyanea, you must declare your activity to extend any of the activity classes that start with 'Cyanea' (e.g., CyaneaAppCompatActivity, CyaneaFragmentActivity). You must also provide a theme or a default theme provided by the library.

```kotlin
class MyActivity : CyaneaAppCompatActivity() {

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

}
```

If you can't extend your activity class, create a CyaneaDelegate and add the appropriate methods. See [CyaneaAppCompatActivity.kt](https://github.com/jaredrummler/Cyanea/blob/master/library-core/src/main/java/com/jaredrummler/cyanea/app/CyaneaAppCompatActivity.kt) as an example.

# Dynamic Theming

Use the following code to change the primary, accent, or background colors of the app:

```kotlin
cyanea.edit {
  // Color displayed most frequently across your app
  primaryResource(R.color.my_new_primary_color)
  // Color that accents select parts of the UI
  accentResource(R.color.my_new_accent_color)
  // The underlying color of the app's content
  background(R.color.my_new_background_color)
  // NOTE: you can also use a color int:
  primary(0xFF0099CC.toInt())
  // More options: 
  // The color of menu items in the ActionBar
  menuIconColor(Color.BLACK)
  // The color of icons in popup menus or in the overflow
  subMenuIconColor(Color.WHITE)
  // The color of the navigation bar
  navigationBar(Color.CYAN)
  // Whether or not to color the navigation bar
  shouldTintNavBar(true)
  // Whether or not to color the status bar
  shouldTintStatusBar(false)
}.recreate(activity)
```

The methods which end with Resource take a color resource. Remove Resource to pass a literal (hardcoded) color integer.

# User Preferences

<img src="https://i.imgur.com/pjDEjWl.gif" align="left" hspace="10" vspace="10"></a>

## Give your users the ability to change the apps look

<a target="_blank" href="LICENSE"><img src="http://img.shields.io/:license-apache-blue.svg" alt="License" /></a>
<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea-prefs"><img src="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea-prefs/badge.svg" alt="Maven Central" /></a>
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>

By integrating the following module you can add preferences for picking the primary, accent and background colors of the app as well as a theme picker for your users.

# Downloads

Download [the latest AAR](https://repo1.maven.org/maven2/com/jaredrummler/cyanea-prefs/1.0.0/cyanea-prefs-1.0.0.aar) or grab via Gradle:

```groovy
implementation 'com.jaredrummler:cyanea-prefs:1.0.0'
```

<br><br><br><br><br><br>

License
-------

    Copyright (C) 2018 Jared Rummler

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


