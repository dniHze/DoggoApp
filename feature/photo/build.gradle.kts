plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("de.mannodermaus.android-junit5")
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    kotlinOptions.freeCompilerArgs += listOf("-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":model"))

    implementation(project(":ui"))
    implementation(project(":navigation"))

    implementation("androidx.appcompat:appcompat:${Version.Jetpack.appCompat}")
    implementation("androidx.core:core-ktx:${Version.Jetpack.core}")

    implementation("androidx.compose.ui:ui:${Version.Jetpack.compose}")
    implementation("androidx.compose.material:material:${Version.Jetpack.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Version.Jetpack.compose}")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Version.Jetpack.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.Jetpack.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Version.Jetpack.viewModelCompose}")
    implementation("androidx.hilt:hilt-navigation-compose:${Version.Jetpack.hiltNavigation}")

    implementation("dev.chrisbanes.accompanist:accompanist-coil:${Version.Util.accompanist}")
    implementation("io.coil-kt:coil:${Version.Util.coil}")

    implementation("com.jakewharton.timber:timber:${Version.Util.timber}")

    implementation("org.oolong-kt:oolong:${Version.Arch.oolong}")

    implementation("com.google.dagger:hilt-android:${Version.Plugin.hilt}")
    kapt("com.google.dagger:hilt-compiler:${Version.Plugin.hilt}")

    testImplementation(platform("org.junit:junit-bom:${Version.Test.junit5}"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.Kotlin.coroutines}")

    androidTestImplementation("androidx.test.ext:junit:${Version.Test.extensions}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Version.Test.espresso}")
}