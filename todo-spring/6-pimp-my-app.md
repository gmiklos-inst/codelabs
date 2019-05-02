---
title: Pimp my APP
parent: Spring Boot & Kotlin
nav_order: 7
---

# Pimp my App

## Paging
In order to make our API pageable, we need to make the following changes:
* Add a `Pageable` parameter to our `GET allTodoItems` endpoint.
* Make sure this `Pageable` object is sent all the way down to the repository.
* Call the `TodoItemRepository`'s `findAll` method with the `Pageable` instance.
* You can use the following query parameters in an API call: `page`, `size`, `sort`
    * For example: `?page=0&size=3&sort=createdAt,DESC`
* Make sure to update the tests

One thing to note:
When adding a `Pageable` parameter to a `Controller`'s method, you get a default `Pageable` even if it was not
defined in the API call itself as query params. In order to still support the 'query all' functionality
as a fallback, we need to customize how a `Pageable` is resolved from a request. We can customize it in our `WebConfiguration`
the following way:
```kotlin
@Bean
fun pageableResolverCustomizer() = PageableHandlerMethodArgumentResolverCustomizer { pageableResolver ->
    pageableResolver.setFallbackPageable(Pageable.unpaged())
}
``` 

## Filtering

To enable arbitrary filtering on `Entity` fields, the easiest way to go is with `Specification`s.

* As a first step, add `implementation 'net.kaczmarzyk:specification-arg-resolver:2.1.1'` as implementation dependency.

* Then add the following argument to the `Controller`'s `getAllTodoItems` method, next to the `Pageable` parameter:

```kotlin
@And(value = [
     Spec(path = "title", spec = Like::class),
     Spec(path = "completed", spec = Equal::class)])
     todoItemSpec: Specification<TodoItem>?
```

* Define the following function in `TodoItemRepository`:

```kotlin
fun findAll(todoItemSpec: Specification<TodoItem>?, pageable: Pageable): Page<TodoItem>
```

* Make sure to send the `Specification` all the way down to the `Repository`
* Update your tests, because they will start failing
* Make the following changes in `WebConfiguration`
    * extend from `WebMvcConfigurer`
    * add the following method that will resolve our `Specification`s


```kotlin
override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
  argumentResolvers.add(SpecificationArgumentResolver())
}
```

Btw, [interesting reading](https://blog.tratif.com/2017/11/23/effective-restful-search-api-in-spring/) on this topic.

## Error handling

We would love to handle `Exception`s that occur in our `Controller`s or in other components they call
and have a unified/controlled Error response to that. Fortunately, we can do so with the `@ControllerAdvice` and `@ExceptionHandler` annotations!

* Our error message format is going to look like this:

```kotlin
data class ErrorResponseDto(val errors: List<ErrorDto>)

data class ErrorDto(val code: Int, val title: String)
```

* Create a new class that will look something like this:

```kotlin
@ControllerAdvice
class ControllerExceptionHandler {

   @ExceptionHandler(Exception::class)
   fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponseDto> = 
           TODO("implement me")
}
```

* Think of what kind of exceptions can occur
* Discuss what tests should be written
* For inspiration, have a look at the `ControllerExceptionHandlerTest` and `ControllerExceptionHandler` classes

## Securing with an API key
In our scenario, we would like to limit our API endpoints only to those clients who know our secret API key.
In Spring terminologies, this is called a [Pre-authentication scenario](https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#preauth).

* As a first step, we need to add `Spring Security` to our project

```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
```

* For the time being, we will store our single API key in our `application.properties` file.
    * Create a new property there: `codelabs.security.apikey=such-secret-much-wow`
    * Don't forget to add it to your test `application.properties` file too!
* Additionally you can add `codelabs.security.apikey=${API_KEY}` to the `application-prod.properties` file and
define an `API_KEY` env variable in Heroku.
* We will need a custom `AuthenticationProvider` that will decide if an `Authentication` is valid or not.
In the valid case, it returns an `AuthenticationToken` with the `CAN_ACCESS_TODO` authority.

```kotlin
@Component
class ApiKeyAuthenticationProvider : AuthenticationProvider {

    @Autowired
    private lateinit var apiKeyService: ApiKeyService

    override fun authenticate(authentication: Authentication): Authentication? {

        val token = authentication.credentials.toString()

        return if (apiKeyService.isApiKeyValid(token)) {
            PreAuthenticatedAuthenticationToken(null, null, listOf(SimpleGrantedAuthority("CAN_ACCESS_TODO"))).apply {
                isAuthenticated = true
            }
        } else {
            null
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == PreAuthenticatedAuthenticationToken::class.java
    }
}
```

The corresponding `Service`

```kotlin
@Service
class ApiKeyService {

    @Value("\${codelabs.security.apikey}")
    private lateinit var correctApiKey: String

    fun isApiKeyValid(apiKey: String) = (apiKey == correctApiKey)
}
```

* Now we need to wire things together with a Security configuration component. The key points are:
    * We make all endpoints require authorization with a `CAN_ACCESS_TODO` authority.
    * We add a `preAuthenticationFilter` to the Spring Security Auth chain that will check the 
    http headers and look for an API key under `"x-api-key"`
    * We switch off sessions and go stateless.

```kotlin
@Configuration
@EnableWebSecurity
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var apiKeyAuthenticationProvider: ApiKeyAuthenticationProvider

    override fun configure(http: HttpSecurity) {
        http.antMatcher("/**")
                .addFilter(preAuthenticationFilter())
                .authorizeRequests().anyRequest().hasAuthority("CAN_ACCESS_TODO")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
    }

    override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(listOf(apiKeyAuthenticationProvider))
    }

    @Bean
    fun preAuthenticationFilter() = RequestHeaderAuthenticationFilter().apply {
        setPrincipalRequestHeader("x-api-key")
        setCredentialsRequestHeader("x-api-key")
        setAuthenticationManager(authenticationManager())
        setExceptionIfHeaderMissing(false)
    }
}
```

* Update our integration tests, since they are not yet configured to use the API key.

## CORS support

Enabling [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) is pretty easy in Spring:
* If you use Spring Security, enable it when you configure your `HttpSecurity`

```kotlin
@Configuration
@EnableWebSecurity
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var apiKeyAuthenticationProvider: ApiKeyAuthenticationProvider

    override fun configure(http: HttpSecurity) {
        http.cors()
    }
}
```

* Annotate your `Controoler`s with `@CrossOrigin`.

```kotlin
@CrossOrigin
@RestController
@RequestMapping("/todos")
class TodoItemController {
    ...
}
```