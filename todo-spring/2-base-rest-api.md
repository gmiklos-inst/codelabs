# Creating the rest API

As a first step, we need to plan how our REST API should look like. For this workshop, we will go 
with an API defined by this [Swagger file](https://app.swaggerhub.com/apis/henko/todo/1.0.0#/).

## RestController base class

We need a controller class in order to be able to handle REST API calls.

* Create a new package, called `controller` in the `codelabs` package.
* Create a new Kotlin class, called `TodoItemController` in the `com.instructure.bp.codelabs.controller` package.
* Annotate the class with `@RestController` to let Spring know that this class will handle REST calls.

## General workflow
A typical CRUD workflow consists of the following layers of components:
* We get a request, which is handled by the `RestController`.
* The `RestController` delegates the call to a `Service` that contains the business logic.
* The `Service` calls a `Repository` (or more) if necessary.
* The `Repository` gets data from a database. In our case, from PostgreSQL.
* It is also worth noting that a `RestController` works with [DTOs](https://martinfowler.com/eaaCatalog/dataTransferObject.html), while a `Repository` works on the `Entity` level.

## TodoItemService
In this section, we are not concerned with database access, thus our `Service` will return dummy in memory `TodoItemDto`s.
* Create a `TodoItemService` Kotlin class in the `service` package.
* Annotate the class with `@Service` to let Spring know that this class is a `Service` that can be injected into other components.
* Create a method called `getAllTodoItems` that returns a `List` of `TodoItemDto`s.

## GET
Let's create our first method, that receives a `GET` request and returns all the `TodoItemDto`s. 
Since this workshop is written in the spirit of TDD, take a brief moment to figure out what kinds of 
tests we need in order to implement this functionality.
We will need annotations like `RequestMapping` and `GetMapping`. 

A possible way to write the tests and the corresponding implementation is the following:
```kotlin
@ExtendWith(MockitoExtension::class)
class TodoItemControllerTest {

    @Mock
    private lateinit var todoItemService: TodoItemService

    @InjectMocks
    private lateinit var todoItemController: TodoItemController

    private lateinit var todoItems: List<TodoItemDto>

    @BeforeEach
    fun init() {
        todoItems = listOf(
                TodoItemDto("1", "title1", false),
                TodoItemDto("2", "title2", true),
                TodoItemDto("3", "title3", false)
        )
    }

    @Test
    fun `getTodoItems returns all the todoItems`() {
        `when`(todoItemService.getAllTodoItems()).thenReturn(todoItems)

        val actualTodoItems = todoItemController.getTodoItems()

        actualTodoItems shouldBe todoItems
    }
}
```

```kotlin
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoItemControllerIntegrationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `test GET all todoItems`() {
        testRestTemplate.getForEntity("/todos", Any::class.java).statusCodeValue shouldBe 200
    }
}
```

Please note, the integration test's purpose is to check if the controller is correctly wired in for `GET` requests.

```kotlin
@RestController
@RequestMapping("/todos")
class TodoItemController {

    @Autowired
    private lateinit var todoItemService: TodoItemService

    @GetMapping
    fun getTodoItems(): List<TodoItemDto> = todoItemService.getAllTodoItems()
}
```

## Jackson configuration
If we call our `GET /todos` endpoint with a rest client, we get the following response:
```json
[{
        "id": "1",
        "title": "title1",
        "completed": true,
        "createdAt": 1556091928.889,
        "updatedAt": null,
        "completedAt": null
}]
```

We have the following problems with the format of the response json:
* It is not in snake_case
* DateTimes are not serialized correctly
* Null values are sent

### Configuration to the rescue
In order to make adjustments to how Jackson serializes json output with an `ObjectMapper`, 
we need to provide our own, custom configured `ObjectMapperBuilder`. 
We can use a `@Configuration` annotated class with a `@Bean` annotated
method to define such a 3rd party dependency.

As a hint, the following code snippet gets the job done. Make sure to write your implementation
in TDD. If you need some inspiration, feel free to check the `WebConfigurationTest` test class.
```kotlin
package com.instructure.bp.codelabs.configuration

@Configuration
class WebConfiguration {

    @Bean
    fun jacksonBuilder() = Jackson2ObjectMapperBuilder().apply {
        propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        serializationInclusion(JsonInclude.Include.NON_NULL)
        featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        )
    }
}
```

## Other endpoints
Implement the other endpoints similarly to the `GET allTodoItems` implementation.

We will handle errors, authorization and filtering in following checkpoint.

## Swagger API
Wouldn't it be great if we could serve the Swagger documentation for our `RestController`? We can do it with SpringFox!

First, let's add the corresponding dependencis to our `build.gradle` file's `dependencies` block:
```groovy
	implementation "io.springfox:springfox-swagger2:2.9.2"
	implementation "io.springfox:springfox-swagger-ui:2.9.2"
```

### Enable SpringFox
As with the custom `Jackson2ObjectMapperBuilder`, we need to configure SpringFox as well. 
We can do it the same way, in a `Configuration` class, as described in the following snippet:
```kotlin
@Configuration
class SwaggerConfiguration {

    @Bean
    fun swaggerApi() = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.instructure.bp.codelabs"))
            .paths(PathSelectors.any())
            .build()

}
```

Finally, we need to enable Swagger in our `Application` class:
```kotlin
@EnableSwagger2
@SpringBootApplication
class CodelabsApplication
```

### Check out the self-served Swagger docs
* Start our server: `./gradlew bootRun`
* Wait for Spring to finish initialization
* Visit `http://localhost:8080/swagger-ui.html`
* Feel free to try the API endpoints from the UI

## Checkpoint acceptance criteria
Before advancing to the next checkpoint, please make sure that you:
* Implemented the basic REST API with a Spring `RestController`
* You have a `TodoItem` data transfer object that models a TodoItem
* You have a Service for business logic and it is injected into the `RestController`
* Configured Jackson serialization to conform to the Swagger documentation
* You have tests to make sure that current requirements are met
* We serve our Swagger API docs with the application