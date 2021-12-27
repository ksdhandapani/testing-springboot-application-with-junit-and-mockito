# Testing Spring Boot Application with JUnit and Mockito

### @DataJpaTest annotation

- Spring Boot provides the @DataJpaTest annotation to test the persistence layer components that will autoconfigure in-memory embedded database for testing purposes.
- The @DataJpaTest annotation does not load other Spring beans (@Component, @Controller, @Service, and annotated beans) into ApplicationContext.
- By default, it scans for @Entity classes and configures Spring Data JPA repositories annotated with @Repository annotation.
- By default, tests annotated with @DataJpaTest are transactional and roll back at the end of each test.

### Mocking dependencies using Mockito

**Mocktio's mock() method**

- We can use Mockito class's mock() method to create a mock object of a given class or interface. This is the simplest way to mock an object.

**Mockito's @Mock annotation**

- We can mock an object using @Mock annotation too. It's useful when we want to use the mocked object at multiple places because we avoid calling the mock() method multiple times. The code becomes more readable and we can specify mock object names that will be useful in case of errors.

**Mockito's @InjectMocks annotation**

- When we want to inject a mocked object into another mocked object, we can use @InjectMocks annotation.
- @InjectMock creates the mock object of the class and injects the mocks that are marked with annotations @Mock into it.

**BDDMockito class**

- The Mockito library is shipped with a BDDMockito class which introduces BDD-friendly APIs.
- Example: BDD style writing tests use given/when/then comments

### Hamcrest Library

- Hamcrest is the well-known framework used for unit testing in the Java ecosystem. It is bundled in JUnit and simply put, it uses existing predicates called matcher classes for making assertions.
- Hamcrest is commonly used with JUnit and other testing frameworks for making assertions. Specifically, instead of using JUnit's numerous assert methods, we only use the API's single assertThat statement with appropriate matchers.

**Hamcrest's is() method:**

If we want to verify that the expected value (or object) is equal to the actual value (or object), we have to create our Hamcrest matcher by invoking the is() method of the Matchers class.

Syntax: assertThat(ACTUAL, is(EXPECTED));

### JsonPath Library

- JsonPath expressions always refer to a JSON structure in the same way as XPath expressions are used in combination with an XML document. 
- The "root member object" in JsonPath is always referred to as $ regardless of whether it is an object or array.


### @WebMvcTest Annotation

- Spring Boot provides @WebMvcTest annotation to test Spring MVC Controllers. Also, @WebMvcTest based tests run faster as It will load only the specified controller and its dependencies only without loading the entire application.
- Spring Boot instantiates only the web layer rather than the whole application context. In an application with multiple controllers, you can even ask for only one to be instantiated by using, for example, @WebMvcTest(HomeController.class).


### @SpringBootTest Annotation

- Spring Boot provides @SpringBootTest annotation for integration testing. This annotation creates an application context and loads the full application context.
- It starts the embedded server, creates a web environment, and then enables @Test methods to do the integration testing.
- By default, @SpringBootTest does not start a server. We need to add the attribute "webEnvironment" to further refine how your tests run. It has several options:

1. MOCK (Default): Loads a WebApplicationContext and provides a mock web environment.
2. RANDOM_PORT: Loads a WebServerApplicationContext and provides a real web environment. The embedded server will be started and listen to a random port. This is the one that should be used for the integration test.
3. DEFINED_PORT: Loads a WebServerApplicationContext and provides a real web environment.
4. NONE: Loads an ApplicationContext by using SpringApplication but does not provide any web environment.

- Mocking is not involved in integration testing.

- For Unit Testing - **@WebMvcTest**
- For Integration Testing - **@SpringBootTest**




