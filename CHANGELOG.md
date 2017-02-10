# Change Log
All notable changes to this project will be documented in this file.

The format is derived from [Keep a Changelog](http://keepachangelog.com/) 
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
## 1.1.0
- Improvement: JavaDoc meets new requirements of Java 8
- Fix: `ParameterContext.getParameter(MyType.class)` caused a `ClassCastException` for any type but `Object[]`.
- Feature: New method `ParameterContext.getParameterAsArray()` - use if stored parameter is expected to be an array of `Object` or if it shall be converted
- Feature: New method `ParameterContext.getParameter` - Use this method to let the Java compiler infer the type of the parameter. No class type parameter needed anymore.

## 1.0.0
- initial version
