package telran.reflection.tests;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestsFramework {
	public static void main(String[] args) throws Exception {
		// надо выдать отчет, какие тесты прошли, а какие тесты не прошли
		try {
			if (args.length == 0) {
				throw new IllegalArgumentException("Should be class name as argument");
			}
			Class<?> clazz = Class.forName(args[0]);
			Constructor<?> constructor = clazz.getConstructor();
			Object obj = constructor.newInstance();
			Method[] methods = clazz.getDeclaredMethods();
			runTests(methods, obj);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Wrong argument containing class name");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException("Must be public default constructor");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void runTests(Method[] methods, Object obj) {
		for (Method method: methods) {
			String name = method.getName();
			if (name.startsWith("test") && method.getParameterCount() == 0) {
				method.setAccessible(true); // for making possible to call private methods
				// setAccesible works only for classes, that are in packages, that not started with "java"
				try {
					method.invoke(obj);
					System.out.printf("test %s passed\n", name);
				} catch (Exception e) {
					System.out.printf("test %s failed, exception %s\n", name, e.toString());
				}
			}
		}
	}

}
