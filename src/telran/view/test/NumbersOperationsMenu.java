package telran.view.test;

import telran.view.*;

public class NumbersOperationsMenu {
	public static Item getNumbersOperationsMenu() {
		return new Menu("Numbers Operations",
				new Item[] { Item.of("Add two numbers", NumbersOperationsMenu::sum),
						Item.of("Subtract two numbers", NumbersOperationsMenu::subtract),
						Item.of("Multiply two numbers", NumbersOperationsMenu::multiply),
						Item.of("Divide two numbers", NumbersOperationsMenu::divide), Item.exit() });

	}

	static void sum(InputOutput io) {
		double[] numbers = getTwoNumbers(io);
		io.writeLine(numbers[0] + numbers[1]);
	}

	private static double[] getTwoNumbers(InputOutput io) {
		double firstNumber = io.readNumber("Enter first number", "Wrong number", -Double.MAX_VALUE, Double.MAX_VALUE);
		double secondNumber = io.readNumber("Enter second number", "Wrong number", -Double.MAX_VALUE, Double.MAX_VALUE);
		return new double[] { firstNumber, secondNumber };
	}

	static void subtract(InputOutput io) {
		double[] numbers = getTwoNumbers(io);
		io.writeLine(numbers[0] - numbers[1]);
	}

	static void multiply(InputOutput io) {
		double[] numbers = getTwoNumbers(io);
		io.writeLine(numbers[0] * numbers[1]);
	}

	static void divide(InputOutput io) {
		double[] numbers = getTwoNumbers(io);
		io.writeLine(numbers[0] / numbers[1]);
	}
}