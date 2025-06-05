import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

val secretPropertiesFile = rootProject.file("secrets.properties")
val secrets = Properties()
if (secretPropertiesFile.exists()) {
    secrets.load(secretPropertiesFile.inputStream())
}

android {
    namespace = "com.example.pupilmeshassignment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pupilmeshassignment"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "WEB_CLIENT_ID", "\"${secrets.getProperty("WEB_CLIENT_ID")}\"")
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
    buildFeatures {
        compose = true
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Navigation framework
    implementation(libs.androidx.navigation.compose)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    kapt(libs.androidx.room.compiler)

    //data store
    implementation(libs.androidx.datastore.preferences)

    //lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.extensions)


    //Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    //Gson, Retrofit, Logging Interceptor
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)


    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    //coil library
    implementation(libs.coil.compose)

}