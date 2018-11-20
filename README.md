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

# Usage

## Setup

To integrate Cyanea, you must declare your activity to extend any of the activity classes that start with 'Cyanea' (e.g., CyaneaAppCompatActivity, CyaneaFragmentActivity). You must also provide a theme or a default theme provided by the library.

```kotlin
class MyActivity : CyaneaAppCompatActivity() {

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

}
```

If you can't extend your activity class, create a CyaneaDelegate and add the appropriate methods. See [CyaneaAppCompatActivity.kt](https://github.com/jaredrummler/Cyanea/blob/master/library-core/src/main/java/com/jaredrummler/cyanea/app/CyaneaAppCompatActivity.kt) as an example.

## Dynamic Theming

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

## Styles

To use the library you will also need to use a `Theme.Cyanea` theme (or decendant) with each activity. You should declare the theme in the `AndroidManifest`:

Example:

```xml
<activity
  android:name=".MyActivity"
  android:theme="@style/Theme.Cyanea.Light.DarkActionBar"/>
```

The library provides core themes — one of which must be applied to each activity:

- `Theme.Cyanea.Dark`
- `Theme.Cyanea.Dark.LightActionBar`
- `Theme.Cyanea.Dark.NoActionBar`
- `Theme.Cyanea.Light`
- `Theme.Cyanea.Light.DarkActionBar`
- `Theme.Cyanea.Light.NoActionBar`

## Default colors

To set the primary and accent colors in a project **not** using Cyanea you would do the following:

```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
  <!-- Customize your theme here. -->
  <item name="colorPrimary">@color/colorPrimary</item>
  <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
  <item name="colorAccent">@color/colorAccent</item>
</style>
```

To set the default colors using this library you must define each color below in `colors.xml`:

```xml
  <!-- Primary colors -->
  <color name="color_primary_reference">#FF65185A</color> <!-- primary color -->
  <color name="color_primary_light_reference">#FF8c2D7F</color> <!-- primary light color -->
  <color name="color_primary_dark_reference">#FF57114D</color> <!-- primary dark color -->
  <!-- Accent colors -->
  <color name="color_accent_reference">#FFF29135</color> <!-- accent color -->
  <color name="color_accent_light_reference">#FFFDA34D</color> <!-- accent light color -->
  <color name="color_accent_dark_reference">#FFE68529</color> <!-- accent dark color -->
```

You can edit and get the colors using Cyanea:

```kotlin
cyanea.edit {
  primary(Color.RED) // Changed the app's primary color to red
}
val color = cyanea.primary // Now the primary color is red.
```

# User Preferences

<img src="https://i.imgur.com/pjDEjWl.gif" align="left" hspace="10" vspace="10"></a>

## Give your users the ability to change the apps look

<a target="_blank" href="LICENSE"><img src="http://img.shields.io/:license-apache-blue.svg" alt="License" /></a>
<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea-prefs"><img src="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea-prefs/badge.svg" alt="Maven Central" /></a>
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>

By integrating the following module you can add preferences for choosing the primary, accent and background colors of the app. A theme-picker with 50+ pre-defined themes is also included in the library.

# Downloads

Download [the latest AAR](https://repo1.maven.org/maven2/com/jaredrummler/cyanea-prefs/1.0.0/cyanea-prefs-1.0.0.aar) or grab via Gradle:

```groovy
implementation 'com.jaredrummler:cyanea-prefs:1.0.0'
```

### Activities

The following activites will be added to launch the preferences and theme picker: `CyaneaSettingsActivity`, `CyaneaThemePickerActivity`.

<br><br><br>

## Pre-defined themes

To override and create your own pre-defined themes add the following file to your project: `assets/themes/cyanea_themes.json`. The file must be a JSON array with each theme as seen [here](https://github.com/jaredrummler/Cyanea/blob/master/library-prefs/src/main/assets/themes/cyanea_themes.json). 

JSON format for a pre-defined theme:

```json
{
  "theme_name": "Vitamin Sea",
  "base_theme": "LIGHT",
  "primary": "#0359AE",
  "primary_dark": "#024B93",
  "primary_light": "#2871BA",
  "accent": "#14B09B",
  "accent_dark": "#119583",
  "accent_light": "#37BBA9",
  "background": "#EBE5D9",
  "background_dark": "#D4D4D4",
  "background_light": "#EEE8DE"
}
```

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


