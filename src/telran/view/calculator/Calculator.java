package telran.view.calculator;

import java.time.LocalDate;
import java.util.function.BiFunction;
import telran.view.*;

public class Calculator {
	private static StandartInputOutput io = new StandartInputOutput();
	
	public static void main(String[] args) {
		createMenu().perform(io);
		io.writeLine("Thanks & Goodbye");
	}

	private static Menu createMenu() {
		return new Menu("Calculator", 
				new Menu("Arithmetic operations",
						Item.of("Add numbers", el -> processNumbers("+", (a, b) -> a + b)),
						Item.of("Subtract numbers", el -> processNumbers("-", (a, b) -> a - b)),
						Item.of("Multiply numbers", el -> processNumbers("*", (a, b) -> a * b)),
						Item.of("Divide numbers", el -> processNumbers("/", (a, b) -> a / b)),
						Item.exit()
						),
				new Menu("Date operations",
						Item.of("Add days", el -> processDates("add", (a, b) -> a.plusDays(b))),
						Item.of("Subtract days", el -> processDates("substract", (a, b) -> a.minusDays(b))),
						Item.exit()
						),
				Item.exit());
	}
	
	private static void processDates(String operation, BiFunction<LocalDate, Integer, LocalDate> function) {
		LocalDate date = io.readDateISO("Enter date", "Enter correct date");
		int days = io.readInt(String.format("Enter days to %s", operation), "Enter correct number");
		try {
			io.writeLine(String.format("Date: %s, %s %s day(s), result: %s", date, operation, days, function.apply(date, days)));
		} catch (Exception e) {
			io.writeLine(String.format("Error while processing %s %s day(s) with date %s", operation, days, date));
		}
	}
	
	private static void processNumbers(String sign, BiFunction<Double, Double, Double> function) {
		double a = io.readNumber("Enter first number", "Enter correct double");
		double b = io.readNumber("Enter second number", "Enter correct double");
		try {
			io.writeLine(String.format("%s %s %s = %s", a, sign, b, function.apply(a, b)));
		} catch (Exception e) {
			io.writeLine(String.format("Error processing  %s %s %s", a, sign, b));
		}
	}
}
