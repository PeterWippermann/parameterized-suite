package com.github.peterwippermann.junit4.parameterizedsuite.parameter;

import com.github.peterwippermann.junit4.parameterizedsuite.util.ParameterizedUtil;

public abstract class Parameter {

	public abstract Object[] asNormalized();

	public abstract Object asIs();

	public static Parameter from(Object parameter) {
		if (ParameterizedUtil.isParameterAnArray(parameter)) {
			return new ArrayParameter((Object[]) parameter);
		} else {
			return new SingleParameter(parameter);
		}
	}

	private static class ArrayParameter extends Parameter {
		private Object[] parameter;
	
		public ArrayParameter(Object[] parameter) {
			this.parameter = parameter;
		}
	
		@Override
		public Object[] asNormalized() {
			return parameter;
		}
	
		@Override
		public Object asIs() {
			return parameter;
		}
	
	}

	private static class SingleParameter extends Parameter {
		private Object parameter;
		private Object[] normalizedParameter;
	
		public SingleParameter(Object parameter) {
			this.parameter = parameter;
			this.normalizedParameter = ParameterizedUtil.convertSingleParameterToArray(parameter);
		}
	
		@Override
		public Object[] asNormalized() {
			return normalizedParameter;
		}
	
		@Override
		public Object asIs() {
			return parameter;
		}
	
	}

}
