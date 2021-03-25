plugins {
    id("com.android.library")
    id("kotlin-android")
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
        disable("MissingTranslation", "ObsoleteLintCustomCheck")
        isAbortOnError = true
        isCheckAllWarnings = true
        isWarningsAsErrors = true
        isCheckDependencies = false
        xmlOutput = file("$buildDir/reports/lint-report.xml")
    }
}

dependencies {
    api("androidx.navigation:navigation-compose:${Version.Jetpack.navigation}")
}
