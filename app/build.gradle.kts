plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    setCompileSdkVersion(30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "dev.kaitei.doggoapp"
        setMinSdkVersion(21)
        setTargetSdkVersion(30)
        versionCode = 1
        versionName = "1.0.0"

        setTestInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            debuggable(true)
            isMinifyEnabled = false
            applicationIdSuffix(".debug")
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
        disable("MissingTranslation")
        isAbortOnError = true
        isCheckAllWarnings = true
        isWarningsAsErrors = true
        isCheckDependencies = false
        xmlOutput = file("$buildDir/reports/lint-report.xml")
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:${Version.Jetpack.appCompat}")
    implementation("androidx.core:core-ktx:${Version.Jetpack.core}")
    implementation("com.google.android.material:material:${Version.Jetpack.material}")
    implementation("androidx.compose.ui:ui:${Version.Jetpack.compose}")
    implementation("androidx.compose.material:material:${Version.Jetpack.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Version.Jetpack.compose}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Version.Jetpack.lifecycle}")
    implementation("androidx.activity:activity-compose:${Version.Jetpack.activityCompose}")

    implementation("dev.chrisbanes.accompanist:accompanist-coil:${Version.Util.accompanist}")
    implementation("io.coil-kt:coil:${Version.Util.coil}")
    
    testImplementation("junit:junit:${Version.Test.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Version.Test.extensions}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Version.Test.espresso}")
}