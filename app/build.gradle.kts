import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
}

val weatherApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("WEATHER_API_KEY")
val weatherApiBaseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("WEATHER_API_BASE_URL")
val locationApiBaseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("LOCATION_API_BASE_URL")
val locationApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("LOCATION_API_KEY")

android {
    namespace = "com.contraomnese.weather"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.contraomnese.weather"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = gradleLocalProperties(rootDir, providers).getProperty("KEY_ALIAS")
            keyPassword = gradleLocalProperties(rootDir, providers).getProperty("KEY_PASSWORD")
            storeFile = file(gradleLocalProperties(rootDir, providers).getProperty("STORE_FILE"))
            storePassword = gradleLocalProperties(rootDir, providers).getProperty("STORE_PASSWORD")
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "WEATHER_API_KEY", "\"${weatherApiKey}\"")
            buildConfigField("String", "WEATHER_API_BASE_URL", "\"${weatherApiBaseUrl}\"")
            buildConfigField("String", "LOCATION_API_BASE_URL", "\"${locationApiBaseUrl}\"")
            buildConfigField("String", "LOCATION_API_KEY", "\"${locationApiKey}\"")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String", "WEATHER_API_KEY", "\"${weatherApiKey}\"")
            buildConfigField("String", "WEATHER_API_BASE_URL", "\"${weatherApiBaseUrl}\"")
            buildConfigField("String", "LOCATION_API_BASE_URL", "\"${locationApiBaseUrl}\"")
            buildConfigField("String", "LOCATION_API_KEY", "\"${locationApiKey}\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composecompiler.get()
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {

    // modules
    implementation(project(":core:ui"))
    implementation(project(":core:design"))
    implementation(project(":core:presentation"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(project(":features:home"))
    implementation(project(":features:weatherByLocation"))
    implementation(project(":features:appSettings"))

    implementation(libs.material)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.core)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.presentation)
    implementation(libs.bundles.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.ui)
    implementation(libs.bundles.network)
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.datastore)

    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.bundles.composeDebug)
}