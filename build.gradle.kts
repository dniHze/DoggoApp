// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Version.Plugin.agp}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.Kotlin.stdlib}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Version.Plugin.detekt}")
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        "detekt"("io.gitlab.arturbosch.detekt:detekt-formatting:${Version.Plugin.detekt}")
        "detekt"("io.gitlab.arturbosch.detekt:detekt-cli:${Version.Plugin.detekt}")
    }

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        toolVersion = Version.Plugin.detekt

        config = rootProject.files("config/detekt.yml")
        ignoredBuildTypes = listOf("release")
        ignoredFlavors = listOf("release")

        reports {
            html {
                enabled = true
            }

            xml {
                enabled = false
            }

            txt {
                enabled = false
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
