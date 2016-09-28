# Parameterized Suite
This library enables you to **define parameters for a test suite**.

It provides a new Runner `ParameterizedSuite` for JUnit 4 that combines the features of `Suite` and `Parameterized`. The new Runner can be used as a drop-in replacement for `Suite`.

## Features
- Define parameters for a test suite by providing a method annotated with `@Parameters` - just as in `Parameterized`.
- The test suite's child classes (annotated with `@SuiteClasses`) will be executed for every parameter.
  - The execution strategy is: "All test cases per parameter", i.e. for every parameter all test cases are executed in a row, then the test cases are executed for the next parameter and so on.
- During test execution the current parameter is stored in a singleton `ParameterContext`, so that the test cases can access it from there.
- **Bonus feature**: Annotations like `@Before`, `@After`,`@ClassRule` & `@Rule` are also available for test suites now!
  - In standard JUnit 4 test suites have not been instantiated (as opposed to normal test cases). That's why the annotations mentioned above were not available.
  - `ParameterizedSuite` gives you full access to these annotations.
  
## Use cases
* In Selenium, run your automated test with all the browsers you want to support! Just make your test cases accept parameters of type `WebDriver`.
  * Nice detail: You can reuse your browser between the test cases! Loaded website and cookies will remain active when the parameter object is passed around.

## Usage
This is how a parameterized test suite looks like:
```java
@RunWith(ParameterizedSuite.class)
@SuiteClasses({OneTest.class, TwoTest.class})
public class MyParameterizedTestSuite {
    @Parameters(name = "Parameters are {0} and {1}")
    public static Object[] params() {
        return new Object[][] {{'A',1}, {'B',2}, {'C',3}};
    }

    /**
     * Always provide a target for the defined parameters - even if you only want to access them in the suite's child classes.
     * Instead of fields you can also use a constructor with parameters.
     */
    @Parameter(0)
    public char myCharParameter;
    
    @Parameter(1)
    public int myIntParameter;
}
```

Your test case reads the current parameter from the `ParameterContext`:
```java
@RunWith(Parameterized.class)
public class MyParameterizedTestCase {
    
    @Parameters(name = "Parameters are {0} and {1}")
    public static Iterable<Object[]> params() {
        if (ParameterContext.isParameterSet()) {
            return Collections.singletonList(ParameterContext.getParameter(Object[].class));
        } else {
            // if the test case is not executed as part of a ParameterizedSuite, you can define fallback parameters
        }
    }

    private char myCharParameter;
    private int myIntParameter;
    
    public MyParameterizedTestCase(char c, int i) {
        super();
        this.myCharParameter = c;
        this.myIntParameter = i;
    }

    @Test
    public void testWithParameters() {
        // ...
    }

}
```

You can find a fully working example in the tests of this project: [ExampleParameterizedSuite](https://github.com/PeterWippermann/parameterized-suite/blob/master/src/test/java/com/github/peterwippermann/junit4/parameterizedsuite/ExampleParameterizedSuite.java)

## Dependency setup
Add the following dependency declaration of *parameterized-suite* together with *JUnit 4*.
### Maven
```xml
<dependency>
  <groupId>com.github.peterwippermann.junit4</groupId>
  <artifactId>parameterized-suite</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.12</version>
  <scope>test</scope>
</dependency>
```
### Gradle
````groovy
dependencies {
    runtime 'junit:junit:4.12',
            'com.github.peterwippermann.junit4:parameterized-suite:1.0.0'
}
```

## License
This library is [licensed under EPL - version 1](LICENSE-parameterized-suite.txt).  
The package `com.github.peterwippermann.junit4.parameterizedsuite.util` also contains source from JUnit 4. See details in the [package info](src/main/java/com/github/peterwippermann/junit4/parameterizedsuite/util/package-info.java) and the [license file](LICENSE-junit.txt). 

## Contributing
