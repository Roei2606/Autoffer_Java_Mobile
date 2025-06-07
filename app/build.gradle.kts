plugins {
    alias(libs.plugins.android.application)
    kotlin("android")
    alias(libs.plugins.google.services)
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.autofferandroid"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.autofferandroid"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/INDEX.LIST")
    }

}


dependencies {
    implementation(project(":chatSDK"))
    implementation(project(":rsocketSDK"))
    implementation(project(":adsSDK"))
    implementation(project(":projectsSDK"))
    implementation(project(":localProjectSDK"))
    implementation(project(":usersSDK"))
    implementation(project(":coremodelsSDK"))

    // ✅ Firebase
    implementation(libs.firebase.auth)

    // ✅ UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.swiperefreshlayout)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.lottie)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // ✅ Networking
    implementation(libs.rsocket.core)
    implementation(libs.rsocket.transport.netty)
    implementation(libs.reactor.core)
    implementation(libs.netty.all)

    // ✅ JSON
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    // ✅ Logging
    implementation(libs.slf4j.simple)

    // ✅ Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // ✅ Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

