import org.gradle.api.artifacts.dsl.DependencyHandler

object Versions {
    const val kotlin = "1.4.21"
    const val retrofit = "2.9.0"
    const val moshi = "1.11.0"
}

object Libs {
    const val paging = "androidx.paging:paging-runtime:3.0.0-alpha11"
    const val coil ="io.coil-kt:coil:1.1.0"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2"
    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.5"
    const val appCompat = "androidx.appcompat:appcompat:1.2.0"
    const val material = "com.google.android.material:material:1.2.1"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val kaptMoshi = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
}

fun DependencyHandler.moshi() {
    add("kapt", Libs.kaptMoshi)
    add("implementation", Libs.moshi)
    add("implementation", Libs.moshiConverter)
}