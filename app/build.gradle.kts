plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    setCompileSdkVersion(30)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "dev.kaitei.doggoapp"
        setMinSdkVersion(21)
        setTargetSdkVersion(30)
        versionCode = Version.App.code
        versionName = Version.App.name

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
        disable("MissingTranslation", "ObsoleteLintCustomCheck")
        isAbortOnError = true
        isCheckAllWarnings = true
        isWarningsAsErrors = true
        isCheckDependencies = false
        xmlOutput = file("$buildDir/reports/lint-report.xml")
    }
}

dependencies {
    implementation(project(":model"))
    implementation(project(":api"))
    implementation(project(":feature:list"))
    implementation(project(":feature:photo"))
    implementation(project(":ui"))
    implementation(project(":navigation"))

    implementation("androidx.appcompat:appcompat:${Version.Jetpack.appCompat}")
    implementation("androidx.core:core-ktx:${Version.Jetpack.core}")
    implementation("com.google.android.material:material:${Version.Jetpack.material}")
    implementation("androidx.compose.ui:ui:${Version.Jetpack.compose}")
    implementation("androidx.compose.material:material:${Version.Jetpack.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Version.Jetpack.compose}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Version.Jetpack.lifecycle}")
    implementation("androidx.activity:activity-compose:${Version.Jetpack.activityCompose}")

    implementation("com.squareup.okhttp3:logging-interceptor:${Version.Network.okhttp}")
    implementation("io.coil-kt:coil:${Version.Util.coil}")

    implementation("com.google.dagger:hilt-android:${Version.Plugin.hilt}")
    kapt("com.google.dagger:hilt-compiler:${Version.Plugin.hilt}")

    implementation("com.jakewharton.timber:timber:${Version.Util.timber}")

    testImplementation("junit:junit:${Version.Test.junit}")

    androidTestImplementation("androidx.test.ext:junit:${Version.Test.extensions}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Version.Test.espresso}")
}