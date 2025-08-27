plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.google.play.services)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kwdevs.hospitalsdashboard"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kwdevs.hospitalsdashboard"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.play.services.mlkit.text.recognition)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Retrofit Libraries
    implementation (libs.converter.gson)
    implementation (libs.converter.moshi)
    implementation(libs.okhttp)
    implementation (libs.retrofit)
    implementation (libs.logging.interceptor)
    implementation (libs.retrofit2.kotlin.coroutines.adapter)
    implementation (libs.moshi)
    //Material Library
    implementation (libs.androidx.material)

    //Moshi Library
    implementation(libs.moshi.kotlin)

    //Facebook Library
    implementation (libs.stetho.okhttp3)

    //Navigation Libraries
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    implementation (libs.androidx.navigation.compose)

    //Live Data Libraries
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.runtime.compose)
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.androidx.runtime)
    implementation (libs.androidx.runtime.livedata)

    //Hawk Library
    implementation(libs.hawk)

    //Animated Transition Library
    implementation (libs.accompanist.navigation.animation)
    implementation(libs.vision.common)
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation (libs.tesseract4android.openmp)

    //implementation(libs.text.recognition.arabic)
    //Firebase Cloud Messaging
    //implementation(platform(libs.google.firebase.bom))
    //implementation (libs.firebase.messaging)
    //implementation(libs.firebase.messaging.ktx)
    //implementation(libs.firebase.iid)

    //Pusher
    implementation (libs.pusher.java.client)

    //Google Ads
    //implementation(libs.play.services.ads)

    //Coil
    implementation (libs.coil)
    implementation (libs.coil.compose)
    implementation (libs.accompanist.coil)

    //Maps
    implementation(libs.maps.compose)      // Compose Maps library
    implementation(libs.osmdroid.android) // For OpenStreetMap support
    implementation(libs.osmbonuspack)

    //Chart 1
    implementation (libs.mpandroidchart)

    //Chart 2
    implementation (libs.charts)

    //Arabic Text Support
    //implementation (libs.tesseract4android)
    //implementation(libs.tess.two)

    //implementation (libs.text.recognition.arabic)
    implementation(libs.mlkit.common)

    //Capture Images Via Camera
    // CameraX Core
    implementation (libs.androidx.camera.core)

    // CameraX Lifecycle binding
    implementation (libs.androidx.camera.lifecycle)

    // CameraX Camera2 support
    implementation (libs.androidx.camera.camera2)

    // CameraX PreviewView
    implementation (libs.androidx.camera.view)

    implementation (libs.androidx.activity.ktx)

    //Image Cropping
    implementation(libs.ucrop)


}