package telran.view.test;

import telran.view.*;
import static telran.view.Item.*;

import java.time.LocalDate;

public class DatesOperationsMenu {
	public static Item getDatesOperationsMenu() {
		return new Menu("Dates Operations", of("Days After", DatesOperationsMenu::daysAfter),
				of("Days Before", DatesOperationsMenu::daysBefore), exit());
	}

	static void daysOperations(boolean isBefore, InputOutput io) {
		LocalDate date = io.readDateISO("Enter date", "Wrong date");
		int days = io.readInt("Enter number of days", "Wrong number of days", 1, Integer.MAX_VALUE);
		if (isBefore) {
			days = -days;
		}
		io.writeLine(date.plusDays(days));
	}

	static void daysAfter(InputOutput io) {
		daysOperations(false, io);
	}

	static void daysBefore(InputOutput io) {
		daysOperations(true, io);
	}
}