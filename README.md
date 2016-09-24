# Parameterized Suite
This library allows you to **define parameters for a test suite**.

It provides a new Runner `ParameterizedSuite` for JUnit 4 that combines the features of `Suite` and `Parameterized`. The new Runner can be used as a drop-in replacement for `Suite`.

## Features
- Define parameters at a test suite by providing a method annotated with `@Parameters` - just as in `Parameterized`.
- The test suite's child classes (annotated with `@SuiteClasses`) will be executed for every parameter.
  - The execution strategy is: "All test cases per parameter", i.e. for every parameter all test cases are executed in a row, than the test cases are executed for the next parameter and so on...
- During test execution the current parameter is stored in a singleton `ParameterContext`, so the test cases can access it from there.
- **Bonus feature**: Annotations like `@Before`, `@After`,`@ClassRule` & `@Rule` are also available for test suites now!
  - In standard JUnit 4 test suites have not been instantiated (as opposed to normal test cases). Thus the annotations mentioned above were not available.
  - `ParameterizedSuite` gives you full access to these annotations.

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

## Dependency setup
### Maven
### Gradle

## License

## Contributing
