plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.orangequality"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.orangequality"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Perbaikan tanda kurung & versi
    implementation("com.google.android.material:material:1.11.0")

    // Library grafik/chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // TensorFlow Lite (Pilih salah satu, JANGAN dua-duanya)
    implementation("org.tensorflow:tensorflow-lite:2.16.1")
    // Hapus jika tidak perlu versi nightly:
    // implementation("org.tensorflow:tensorflow-lite:0.0.0-nightly")

    // Dependensi dari libs.versions.toml
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
