# Cyanea

<img src="https://i.imgur.com/eC6d5WO.gif" align="left" hspace="10" vspace="10"></a>

**A theme engine for Android.**
<br>
<sub>Themes are immutable, possibilities are beautiful.</sub>

<a target="_blank" href="LICENSE"><img src="http://img.shields.io/:license-apache-blue.svg" alt="License" /></a>
<a target="_blank" href="https://travis-ci.org/jaredrummler/Cyanea"><img src="https://travis-ci.org/jaredrummler/Cyanea.svg?branch=master" alt="Build Status" /></a>
<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea"><img src="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/cyanea/badge.svg" alt="Maven Central" /></a>
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>

# About

A powerful, dynamic, and fun theme engine. Named after Octopus Cyanea which is adept at camouflage and not only can change color frequently, but also can change the patterns on and texture of its skin.

# Downloads

Download [the latest AAR](https://repo1.maven.org/maven2/com/jaredrummler/cyanea/1.0.2/cyanea-1.0.2.aar) or grab via Gradle:

```groovy
implementation 'com.jaredrummler:cyanea:1.0.2'
```

## Demo

You can download an [APK of the demo project](https://github.com/jaredrummler/Cyanea/blob/master/demo.apk?raw=true)

<br>

# Getting Started

#### Step 1

Getting the project setup to use Cyanea is simple. First, initialize Cyanea in your `Application` class:

```kotlin
class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    Cyanea.init(this, resources)
  }

}
```

Also ensure that your `MyApplication` class is registered in your `AndroidManifest.xml` file:

```xml
<manifest
  xmlns:android="http://schemas.android.com/apk/res/android"
    ...>
  <application
    android:name=".MyApplication"
  ...>
  </application>
</manifest>
```

Alternatively, you can use `com.jaredrummler.cyanea.CyaneaApp` as the `Application` class or have `MyApplication` class inherit from `CyaneaApp` (recommended).

#### Step 2

You must declare your activity to extend any of the activity classes that start with 'Cyanea' (e.g., CyaneaAppCompatActivity, CyaneaFragmentActivity).

```kotlin
class MyActivity : CyaneaAppCompatActivity() {

}
```

If you can't extend your activity class, create a `CyaneaDelegate` and add the appropriate methods. See [CyaneaAppCompatActivity.kt](https://github.com/jaredrummler/Cyanea/blob/master/library/src/main/java/com/jaredrummler/cyanea/app/CyaneaAppCompatActivity.kt) as an example.

#### Step 3

Each activity must use a `Theme.Cyanea` theme (or decendant). You should declare the theme in the `AndroidManifest`:

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

#### Step 4

Set a default primary and accent color for the app in `colors.xml`:

```xml
<resources>
  <color name="cyanea_primary_reference">#0288D1</color>
  <color name="cyanea_accent_reference">#FFA000</color>
</resources>
```

Note: Do *not* set `colorPrimary`, `colorPrimaryDark`, `colorAccent`, etc. in the theme in `styles.xml`.

# User Preferences

<img src="https://i.imgur.com/pjDEjWl.gif" align="left" hspace="10" vspace="10"></a>

Cyanea adds preferences for choosing the primary, accent and background colors of the app. A theme-picker with 50+ pre-defined themes is also included in the library.

#### Activities

The following activites are added to launch the preferences and theme picker: `CyaneaSettingsActivity`, `CyaneaThemePickerActivity`.

#### Pre-defined themes

To override and create your own pre-defined themes add the following file to your project: `assets/themes/cyanea_themes.json`. The file must be a JSON array with each theme as seen [here](https://github.com/jaredrummler/Cyanea/blob/master/library/src/main/assets/themes/cyanea_themes.json).

Minimal JSON required for a pre-defined theme:

```json
{
  "theme_name": "Vitamin Sea",
  "base_theme": "LIGHT",
  "primary": "#0359AE",
  "accent": "#14B09B",
  "background": "#EBE5D9"
}
```

<br>

# Dynamic Theming

Use the following code to change the primary, accent or background colors of the app:

```kotlin
cyanea.edit {
  primary(Color.BLUE)
  accent(Color.CYAN)
  backgroundResource(R.color.background_material_dark)
  // Many other theme modifications are available
}.recreate(activity)
```

The methods which end with Resource take a color resource. Remove Resource to pass a literal (hardcoded) color integer. Unlike several other open source libraries, Cyanea can use *any* color you specify for primary, accent and background; you don't need pre-defined styles.

You can get the current colors using `cyanea.primary` or using the default instance `Cyanea.instance.primary`.

# Using Colors

Most views will automatically be themed using the library. To use the primary, accent, background, etc. colors use attributes in your layouts:

Attributes:

- `?colorPrimary`
- `?colorPrimaryDark`
- `?colorAccent`
- `?backgroundColor`
- `?backgroundColorDark`
- `?backgroundColorLight`
- `?menuIconColor`
- `?subMenuIconColor`

Example: 

```xml
<com.example.MyCustomView
  android:background="?backgroundColor"
  android:textColor="?colorAccent"
  app:someOtherColor="?colorPrimary" />
```

You can also use the primary, accent, background colors using `@color/cyanea_primary_reference`.

# Advanced Usage

### Processing Views

You can modify a view before it is laid out using a `CyaneaViewProcessor`. Simple let your `Activity` or `Application` implement `CyaneaViewProcessor.Provider` and add your processors to the array.

Example:

```kotlin
class MyActivity : Activity(), CyaneaViewProcessor.Provider {

  override fun getViewProcessors(): Array<CyaneaViewProcessor<out View>> = arrayOf(
      // Add a view processors to manipulate a view when inflated.
      object : CyaneaViewProcessor<TextView>() {
        override fun getType(): Class<TextView> = TextView::class.java
        override fun process(view: TextView, attrs: AttributeSet?, cyanea: Cyanea) {
          view.text = "Hijacked!"
        }
      }
  )

}
```

### Decorators

You can inject custom attributes into layout files using `CyaneaDecorator`. The library ships with a `FontDecorator` which allows you to use `app:cyaneaFont="path/to/Font.ttf"` in any view. The font will automatically be set on the view. To implement your own decorator, let your `Activity` or `Application` implement `CyaneaDecorator.Provider` and return an array of your custom decorators.

Example:

```kotlin
class MyActivity : Activity(), CyaneaDecorator.Provider {

  override fun getDecorators(): Array<CyaneaDecorator> = arrayOf(
      // Add a decorator to apply custom attributes to any view
      FontDecorator()
  )

}
```

Please reference the [FontDecorator](https://github.com/jaredrummler/Cyanea/blob/master/library/src/main/java/com/jaredrummler/cyanea/inflator/decor) as an example.

### Inflation Delegate

You can add an inflation delegate to hook into when views are created and create the views yourself. 

Example:

```kotlin
Cyanea.setInflationDelegate(object : CyaneaInflationDelegate {
  override fun createView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
    var view: View? = null
    if (name == "androidx.appcompat.widget.Toolbar") {
      view = MyCustomToolbar(context, attrs)
    }
    return view
  }
})
```

# License

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
