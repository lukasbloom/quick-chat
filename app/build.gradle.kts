import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.quickwa"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.lukasbloom.quickchat"
        minSdk = 23
        targetSdk = 35
        versionCode = 2
        versionName = "0.1.0"
    }

    // Release signing credentials are read from ~/.gradle/gradle.properties
    // (or passed via env / -P at build time). They're absent in CI and on
    // fresh checkouts; the resolution below falls back to debug signing in
    // that case so `assembleDebug` / unit tests always work.
    val keystoreFile: String? = (project.findProperty("QUICKCHAT_KEYSTORE_PATH") as String?)
        ?: System.getenv("QUICKCHAT_KEYSTORE_PATH")
    val keystorePassword: String? = (project.findProperty("QUICKCHAT_KEYSTORE_PASSWORD") as String?)
        ?: System.getenv("QUICKCHAT_KEYSTORE_PASSWORD")
    val keyAlias: String? = (project.findProperty("QUICKCHAT_KEY_ALIAS") as String?)
        ?: System.getenv("QUICKCHAT_KEY_ALIAS") ?: "release"
    val keyPassword: String? = (project.findProperty("QUICKCHAT_KEY_PASSWORD") as String?)
        ?: System.getenv("QUICKCHAT_KEY_PASSWORD") ?: keystorePassword
    val hasReleaseSigning = keystoreFile != null && keystorePassword != null

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = file(keystoreFile!!)
                storePassword = keystorePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = if (hasReleaseSigning) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.libphonenumber)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
}
