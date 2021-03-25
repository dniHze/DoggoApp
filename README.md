## DoggoApp
This is sample project with multimodular architecture using
Jetpack Compose and UDF architecture powered by [Oolong](https://oolong-kt.org).

Project is using [Dog.ceo](https://dog.ceo/dog-api/) as a data provider service.

### Tech Stack
DoggoApp is build using next tech stack:
* Jetpack Compose for UI and screen controller.
* Retrofit + OkHttp for networking.
* UDF pattern - The Elm Architecture using [Oolong](https://oolong-kt.org).
* Kotlin Coroutines using StateFlows.
* Hilt for DI, Navigation Component for navigation.
* Gradle Kotlin DSL for Gradle build scripting.

Project code validations:
* Detekt - code style validation.
* Android Lint = general Android API usage.
* JUnit5 for Unit tests.

Project is not using any Mock libraries like [MockK](https://mockk.io) just to
showcase that test could be done in pure interfaces. However,
mocking libraries are not made for nothing and they help reduce
routine during code behavior testing.

### TODO
[] Nice UI
[] Better commit history
[] CI runners using GitHub Actions