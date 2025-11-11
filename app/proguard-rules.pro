# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class kotlinx.coroutines.** { *; }
-keep class kotlin.coroutines.Continuation { *; }

-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn org.xmlpull.v1.**
-dontwarn org.kxml2.io.**
-dontwarn android.content.res.**
-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }

-keep class retrofit2.Response { *; }
-keep class okhttp3.logging.HttpLoggingInterceptor { *; }

-keep class com.contraomnese.weather.design.theme.** { *; }
-keep class com.contraomnese.weather.design.icons.WeatherIcons
-keep class com.contraomnese.weather.design.DevicePreviews

-keep class com.contraomnese.weather.presentation.architecture.** { *; }
-keep class com.contraomnese.weather.presentation.utils.** { *; }

-keep class com.contraomnese.weather.core.ui.composition.** { *; }
-keep class com.contraomnese.weather.core.ui.widgets.** { *; }
-keep class com.contraomnese.weather.core.ui.utils.** { *; }

-keep class com.contraomnese.weather.data.network.** { *; }
-keep class com.contraomnese.weather.data.repository.** { *; }
-keep class com.contraomnese.weather.data.storage.db.** { *; }
-keep class com.contraomnese.weather.data.storage.memory.** { *; }
-keep class com.contraomnese.weather.data.parsers.** { *; }
-keep class com.contraomnese.weather.data.mappers.** { *; }
-keep class com.contraomnese.weather.data.utils.** { *; }

-keep class com.contraomnese.weather.domain.** { *; }

-keep class com.contraomnese.weather.navigation.** { *; }
-keep class com.contraomnese.weather.MainActivityAction { *; }
-keep class com.contraomnese.weather.MainActivityEffect { *; }
-keep class com.contraomnese.weather.MainActivityEvent { *; }
-keep class com.contraomnese.weather.MainActivityState { *; }
-keep class com.contraomnese.weather.MainActivityViewModel { *; }

-keep class com.contraomnese.weather.appsettings.navigation.** { *; }
-keep class com.contraomnese.weather.appsettings.presentation.AppSettingsAction { *; }
-keep class com.contraomnese.weather.appsettings.presentation.AppSettingsEffect { *; }
-keep class com.contraomnese.weather.appsettings.presentation.AppSettingsEvent { *; }
-keep class com.contraomnese.weather.appsettings.presentation.AppSettingsScreenState { *; }
-keep class com.contraomnese.weather.appsettings.presentation.AppSettingsViewModel { *; }

-keep class com.contraomnese.weather.home.navigation.** { *; }
-keep class com.contraomnese.weather.home.presentation.HomeScreenAction { *; }
-keep class com.contraomnese.weather.home.presentation.HomeScreenEffect { *; }
-keep class com.contraomnese.weather.home.presentation.HomeScreenEvent { *; }
-keep class com.contraomnese.weather.home.presentation.HomeScreenState { *; }
-keep class com.contraomnese.weather.home.presentation.HomeViewModel { *; }

-keep class com.contraomnese.weather.weatherByLocation.navigation.** { *; }
-keep class com.contraomnese.weather.weatherByLocation.presentation.WeatherScreenAction { *; }
-keep class com.contraomnese.weather.weatherByLocation.presentation.WeatherScreenEffect { *; }
-keep class com.contraomnese.weather.weatherByLocation.presentation.WeatherScreenEvent { *; }
-keep class com.contraomnese.weather.weatherByLocation.presentation.WeatherScreenState { *; }
-keep class com.contraomnese.weather.weatherByLocation.presentation.WeatherViewModel { *; }