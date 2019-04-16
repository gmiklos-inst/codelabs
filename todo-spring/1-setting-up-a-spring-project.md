# Setting up your Spring project

## Spring Initializr
A great way to get started with a Spring boot application is by visiting the [https://start.spring.io/](Spring Initializr website) and choose the following options:
* Project: `Gradle Project`
* Language: `Kotlin`
* Spring Boot: `2.1.4`
* Group: `com.instructure.budapest`
* Artifact: `codelabs`
* Dependencies: `Web, JPA, PostgreSQL`

Click `Generate Project` and save/extract the downloaded zip file.

## JUnit5 and Mockito
Locate your `build.gradle` file and add the following dependencies:
```groovy
    testImplementation('org.junit.jupiter:junit-jupiter-api:5.2.0')
    testImplementation('org.junit.jupiter:junit-jupiter-params:5.2.0')
    testImplementation "org.mockito:mockito-core:2.+"
    testImplementation('org.mockito:mockito-junit-jupiter:2.18.3')
    testRuntime('org.junit.jupiter:junit-jupiter-engine:5.2.0')
```

We need to enable Gradle's JUnit5 support:
```groovy
test {
    useJUnitPlatform()
}
```

Now you have your base `SpringBootApplication` set up with all the required dependencies in your `build.gradle` file.