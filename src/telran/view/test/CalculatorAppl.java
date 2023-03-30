package telran.view.test;

import telran.view.*;

public class CalculatorAppl {

	public static void main(String[] args) {
		InputOutput io = new StandardInputOutput();
		Menu menu = new Menu("Calculator", NumbersOperationsMenu.getNumbersOperationsMenu(),
				DatesOperationsMenu.getDatesOperationsMenu(), Item.exit());
		menu.perform(io);
		io.writeLine("Thanks & Goodbye");

	}

}