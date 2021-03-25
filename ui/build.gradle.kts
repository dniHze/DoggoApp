plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    setCompileSdkVersion(30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        setMinSdkVersion(21)
        setTargetSdkVersion(30)
        versionCode = 1
        versionName = "1.0.0"

        setTestInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.Jetpack.compose
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        disable("MissingTranslation", "UnusedResources", "ObsoleteLintCustomCheck")
        isAbortOnError = true
        isCheckAllWarnings = true
        isWarningsAsErrors = true
        isCheckDependencies = false
        xmlOutput = file("$buildDir/reports/lint-report.xml")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:${Version.Jetpack.core}")

    implementation("androidx.compose.ui:ui:${Version.Jetpack.compose}")
    implementation("androidx.compose.material:material:${Version.Jetpack.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Version.Jetpack.compose}")

    api("com.airbnb.android:lottie-compose:${Version.UI.lottie}")

    testImplementation("junit:junit:${Version.Test.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Version.Test.extensions}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Version.Test.espresso}")
}