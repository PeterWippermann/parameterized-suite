package com.github.peterwippermann.junit4.parameterizedsuite;

import java.lang.reflect.Array;

import com.github.peterwippermann.junit4.parameterizedsuite.parameter.Parameter;

/**
 * This singleton stores a parameter. This way parameters can be transferred
 * between test classes.<br>
 * The type of the parameter is unbound and can also be an {@link Array}. Null
 * is interpreted as "parameter is not set".
 * <p>
 * This implementation is not thread-safe. Concurrent implementations e.g. could
 * use {@link ThreadLocal}.
 *
 */
public class ParameterContext {
	private static Parameter context;

	public static void setParameter(Parameter parameter) {
		context = parameter;
	}

	/**
	 * Retrieve the stored parameter.
	 * 
	 * @param parameterClass
	 *            - Enables type-safety at compile time.
	 */
	@SuppressWarnings("unchecked")
	public static <P> P getParameter(Class<P> parameterClass) {
		return (P) context.asIs();
	}

	/**
	 * Retrieve the stored parameter as an Object[].
	 * <p>
	 * If it has not been an Array when being stored, an Array representation
	 * has been created and stored aside with the original parameter. That array
	 * representation is being returned.
	 */
	public static Object[] getParameterAsArray() {
		return context.asNormalized();
	}

	public static void removeParameter() {
		setParameter(null);
	}

	/**
	 * @return true, if parameter is not null.
	 */
	public static boolean isParameterSet() {
		return context != null;
	}
}
