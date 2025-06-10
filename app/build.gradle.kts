plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-parcelize")
//    id ("kotlin-kapt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 29
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0'")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.0")
    implementation ("io.insert-koin:koin-android:3.3.0")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation ("androidx.fragment:fragment-ktx:1.5.6")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    val room = "2.7.1"
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")

    ksp(libs.room.compiler)

}