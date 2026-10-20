plugins {
    id("com.android.application")
}

android {
    namespace = "app.alternative.fourplay.tv"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.alternative.fourplay.tv"
        minSdk = 23
        targetSdk = 36
        versionCode = 2
        versionName = "0.2.0-dev"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.activity:activity-ktx:1.13.0")
}
