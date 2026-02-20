plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.auth.otpAuthApp.core.domain"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    lint {
        disable.add("NullSafeMutableLiveData")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.jakarta.inject.api)
    implementation(libs.kotlinx.serialization.json)
}
