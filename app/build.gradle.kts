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
        versionCode = 1
        versionName = "0.1.0-dev"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
