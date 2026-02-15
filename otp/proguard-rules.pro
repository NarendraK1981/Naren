# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.coroutines.android.HandlerContext {
    java.lang.String name;
}

# Jetpack Compose
-keepclassmembers class androidx.compose.ui.platform.AndroidComposeView {
    *** getCoroutineContext();
}

# Timber
-keep class timber.log.Timber { *; }
-keep class timber.log.Timber$Tree { *; }

# Keep data models (if any are added later in data package)
-keepclassmembers class com.auth.otpAuthApp.data.** { *; }
