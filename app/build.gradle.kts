import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.baselineprofile)
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
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "DEBUG", "false")
            buildConfigField("String", "WEATHER_API_KEY", "\"${weatherApiKey}\"")
            buildConfigField("String", "WEATHER_API_BASE_URL", "\"${weatherApiBaseUrl}\"")
            buildConfigField("String", "LOCATION_API_BASE_URL", "\"${locationApiBaseUrl}\"")
            buildConfigField("String", "LOCATION_API_KEY", "\"${locationApiKey}\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
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

    "baselineProfile"(project(":baselineprofile"))
    implementation(libs.bundles.profiler)

    implementation(libs.material)
    implementation(libs.bundles.network)
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.datastore)

    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.bundles.composeDebug)
}