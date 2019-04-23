# Creating the rest API

As a first step, we need to plan how our REST API should look like. For this workshop, we will go with an API defined by this [Swagger file](swagger_api.md).
`TODO add swagger file`

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
Since this workshop is written in the spirit of TDD, take a brief moment to figure out what kinds of tests we need in order to implement this functionality.
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
    fun `test GET`() {
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

## Other endpoints
Implement the other endpoints (`POST`, `PUT`, `DELETE`), similarly to the `GET` implementation.

We will handle errors, authorization and filtering in following checkpoint.

## Checkpoint acceptance criteria
Before advancing to the next checkpoint, please make sure that you:
* Implemented the basic REST API with a Spring `RestController`
* You have a `TodoItem` data transfer object that models a TodoItem
* You have a Service for business logic and it is injected into the `RestController`
* You have tests to make sure that current requirements are met