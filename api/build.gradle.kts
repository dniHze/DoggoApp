import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("kotlin")
    id("kotlin-kapt")

}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs += listOf("-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

dependencies {
    implementation(project(":model"))

    api("com.slack.eithernet:eithernet:${Version.Serialization.eitherNet}")

    implementation("com.google.dagger:dagger:${Version.Util.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Version.Util.dagger}")

    api("com.squareup.moshi:moshi-kotlin:${Version.Serialization.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Version.Serialization.moshi}")

    api(platform("com.squareup.okhttp3:okhttp-bom:${Version.Network.okhttp}"))
    api("com.squareup.okhttp3:okhttp")

    api("com.squareup.retrofit2:retrofit:${Version.Network.retrofit}")
    api("com.squareup.retrofit2:converter-moshi:${Version.Network.retrofit}")

    testImplementation(platform("org.junit:junit-bom:${Version.Test.junit5}"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.Kotlin.coroutines}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.Kotlin.coroutines}")

    testImplementation("com.squareup.retrofit2:converter-moshi:${Version.Network.retrofit}")

    testImplementation("com.squareup.okhttp3:mockwebserver")
}