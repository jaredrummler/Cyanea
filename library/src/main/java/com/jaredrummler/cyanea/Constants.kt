/*
 * Copyright (C) 2018 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.cyanea

internal object Constants {
  internal const val NONE_TIMESTAMP = 0L
  internal const val LIGHT_ACTIONBAR_LUMINANCE_FACTOR = 0.75
}

internal object PrefKeys {
  internal const val PREF_FILE_NAME = "com.jaredrummler.cyanea"
  internal const val PREF_BASE_THEME = "base_theme"
  internal const val PREF_PRIMARY = "primary"
  internal const val PREF_PRIMARY_DARK = "primary_dark"
  internal const val PREF_PRIMARY_LIGHT = "primary_light"
  internal const val PREF_ACCENT = "accent"
  internal const val PREF_ACCENT_DARK = "accent_dark"
  internal const val PREF_ACCENT_LIGHT = "accent_light"
  internal const val PREF_BACKGROUND_LIGHT = "background_light"
  internal const val PREF_BACKGROUND_LIGHT_DARKER = "background_light_darker"
  internal const val PREF_BACKGROUND_LIGHT_LIGHTER = "background_light_lighter"
  internal const val PREF_BACKGROUND_DARK = "background_dark"
  internal const val PREF_BACKGROUND_DARK_DARKER = "background_dark_darker"
  internal const val PREF_BACKGROUND_DARK_LIGHTER = "background_dark_lighter"
  internal const val PREF_MENU_ICON_COLOR = "menu_icon_color"
  internal const val PREF_SUB_MENU_ICON_COLOR = "sub_menu_icon_color"
  internal const val PREF_NAVIGATION_BAR = "navigation_bar_color"
  internal const val PREF_SHOULD_TINT_STATUS_BAR = "should_tint_status_bar"
  internal const val PREF_SHOULD_TINT_NAV_BAR = "should_tint_nav_bar"
  internal const val PREF_TIMESTAMP = "timestamp"
}

internal object Defaults {
  internal const val DEFAULT_DARKER_FACTOR = 0.85f
  internal const val DEFAULT_LIGHTER_FACTOR = 0.15f
}

