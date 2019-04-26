---
title: Database access
parent: Spring Boot & Kotlin
nav_order: 5
---

# Database access

## Install local Postgres
Follow the instructions on [how to install PostgreSQL](http://postgresguide.com/setup/install.html)

Verify your installation by typing `psql` in the terminal. (Type `exit` to quit.)

## DB Dependencies
Let's add the database dependencies to our `build.gradle`:

```groovy
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'org.postgresql:postgresql'
```

## Database access configuration
We need to tell Spring how to access our database. We need to edit our `application.properties`
file located under `src/main/resources` and add the following:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/{YOUR_USERNAME}
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username={YOUR_USERNAME}
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
```

## Apply these changes
It is time we start using a real `Repository`!
* Delete the `DummyTodoItemRepository`
* Annotate the `TodoItemRepository` interface with `@Repository`
* `TodoItemRepository` should extend `JpaRepository<TodoItem, String>`
* You can delete all the methods defined under `TodoItemRepository`

`TodoItemService` now can use `JPA` specific exception:
* For deletes/updates, you can use `javax.persistence.EntityNotFoundException`

We need to make changes to our `Entity` class too:
```kotlin
@Entity
data class TodoItem(
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        val id: String = "",

        val title: String,

        val completed: Boolean = false,

        @field:CreationTimestamp
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        val createdAt: OffsetDateTime? = null,

        @field:UpdateTimestamp
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        val updatedAt: OffsetDateTime? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        val completedAt: OffsetDateTime? = null)
```

Since we have a real DB wired in, the integration tests require some initial data
in the DB in order to be able to serve update/delete/getById requests.

This should suffice:
```kotlin
    @Autowired
    lateinit var todoItemRepository: TodoItemRepository

    lateinit var persistedEntities: List<TodoItem>

    @BeforeEach
    fun init() {
        persistedEntities = todoItemRepository.saveAll(listOf(
                TodoItem(title = "title1"),
                TodoItem(title = "title2"),
                TodoItem(title = "title3", completed = true)
        ))
    }

    @AfterEach
    fun tearDown() {
        todoItemRepository.deleteAll()
    }
```