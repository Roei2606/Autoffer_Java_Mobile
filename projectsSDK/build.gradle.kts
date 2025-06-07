plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.projectssdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(project(":rsocketSDK"))
    implementation(project(":coremodelsSDK"))

    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.15.3")
    implementation(libs.rsocket.core)
    implementation(libs.rsocket.transport.netty)
    implementation(libs.reactor.core)
    implementation(libs.netty.all)

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}