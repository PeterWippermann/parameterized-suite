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
