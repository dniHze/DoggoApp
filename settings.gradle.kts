dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        jcenter()
    }
}

rootProject.name = "DoggoApp"

include(":api")
include(":app")
include(":feature:list")
include(":feature:photo")
include(":model")
include(":navigation")
include(":ui")
