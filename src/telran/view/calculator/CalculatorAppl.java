package telran.view.calculator;

import java.time.LocalDate;
import java.util.function.BiFunction;
import telran.view.*;

public class CalculatorAppl {
	
	public static void main(String[] args) {
		InputOutput io = new StandartInputOutput();
		createMenu().perform(io);
	}

	private static Menu createMenu() {
		return new Menu("Calculator", 
				new Menu("Arithmetic operations",
						Item.of("Add numbers", io -> processNumbers(io, "+", (a, b) -> a + b)),
						Item.of("Subtract numbers", io -> processNumbers(io, "-", (a, b) -> a - b)),
						Item.of("Multiply numbers", io -> processNumbers(io, "*", (a, b) -> a * b)),
						Item.of("Divide numbers", io -> processNumbers(io, "/", (a, b) -> a / b)),
						Item.exit()
						),
				new Menu("Date operations",
						Item.of("Add days", io -> processDates(io, "add", (a, b) -> a.plusDays(b))),
						Item.of("Subtract days", io -> processDates(io, "substract", (a, b) -> a.minusDays(b))),
						Item.exit()
						),
				Item.exit());
	}
	
	private static void processDates(InputOutput io, String operation, BiFunction<LocalDate, Integer, LocalDate> function) {
		LocalDate date = io.readDateISO("Enter date", "Enter correct date");
		int days = io.readInt(String.format("Enter days to %s", operation), "Enter correct number");
		try {
			io.writeLine(String.format("%s %s %s day(s) is: %s", date, operation, days, function.apply(date, days)));
		} catch (Exception e) {
			io.writeLine(String.format("Error while processing %s %s day(s) with date %s: %s", operation, days, date, e.getMessage()));
		}
	}
	
	private static void processNumbers(InputOutput io, String sign, BiFunction<Double, Double, Double> function) {
		double a = io.readNumber("Enter first number", "Enter correct double");
		double b = io.readNumber("Enter second number", "Enter correct double");
		try {
			io.writeLine(String.format("%s %s %s = %s", a, sign, b, function.apply(a, b)));
		} catch (Exception e) {
			io.writeLine(String.format("Error processing  %s %s %s: %s", a, sign, b, e.getMessage()));
		}
	}
}
