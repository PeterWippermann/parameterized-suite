package com.github.peterwippermann.junit4.parameterizedsuite.multiple_suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.peterwippermann.junit4.parameterizedsuite.ExampleNormalSuite;
import com.github.peterwippermann.junit4.parameterizedsuite.ExampleParameterizedSuite;

/**
 * A default test suite that runs other test suites - amongst them are
 * {@link ParameterizedSuite}s.
 * <p>
 * Demonstrates that several parameterized suites and default suites can be
 * combined and don't interfere with each other.
 * 
 * @author Peter Wippermann
 */
@RunWith(Suite.class)
@SuiteClasses({ ExampleParameterizedSuite.class, ExampleNormalSuite.class, AnotherParameterizedSuite.class })
public class TopLevelNoParametersSuite {

}
