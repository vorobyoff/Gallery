plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        versionCode = 1
        versionName = "1.0"
        minSdkVersion(23)
        targetSdkVersion(30)
        applicationId = "com.vorobyoff.gallery"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }

    buildFeatures { viewBinding = true }
}

dependencies {

    implementation(Libs.kotlin)
    implementation(Libs.coroutines)
    implementation(Libs.coreKtx)
    implementation(Libs.coil)
    implementation(Libs.fragmentKtx)
    implementation(Libs.appCompat)
    implementation(Libs.paging)
    implementation(Libs.material)
    implementation(Libs.constraintLayout)
    implementation(Libs.loggingInterceptor)
    implementation(Libs.retrofit)
    moshi()
}