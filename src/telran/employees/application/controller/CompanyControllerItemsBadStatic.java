package telran.employees.application.controller;

import java.time.LocalDate;
import java.util.List;

import telran.employees.Company;
import telran.employees.Employee;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class CompanyControllerItemsBadStatic {
	
	private static Company company;
	
	// private constructor to prevent create of object of class
	private CompanyControllerItemsBadStatic(Company company) {
	}
	
	public static Item getUserItemMenu(Company company) {
		CompanyControllerItemsBadStatic.company = company;
		return new Menu("User menu",
				Item.of("getEmployee", io -> getEmployee(io)),
				Item.of("getAllEmployees", io -> getAllEmployees(io)),
				Item.of("getEmployeesByMonthBirth", io -> getEmployeesByMonthBirth(io)),
				Item.of("getEmployeesBySalary", io -> getEmployeesBySalary(io)),
				Item.of("getEmployeesByDepartment", io -> getEmployeesByDepartment(io)),
				Item.exit());
	}
	
	public static  Item getAdminItemMenu(Company company) {
		CompanyControllerItemsBadStatic.company = company;
		return new Menu("Admin menu",
				Item.of("addEmployee", io -> addEmployee(io)),
				Item.of("removeEmployee", io -> removeEmployee(io)),
				Item.exit());
	}
	
	private static  void getEmployee(InputOutput io) {
		int id = io.readInt("Enter id of Employee", "Wrong id");
		Employee employee = company.getEmployee(id);
		if (employee == null) {
			io.writeLine(String.format("No such employee with id=%s", id));
		} else {
			io.writeLine(employee.toString());
		}
	}
	
	private static  void printEmployes(InputOutput io, List<Employee> employees) {
		io.writeLine(String.format("Find %s employee%s:", employees.size(), employees.size() == 1 ? "" : "s" ));
		employees.forEach(empl -> io.writeLine(empl.toString()));
	}
	
	private static  void getAllEmployees(InputOutput io) {
		printEmployes(io, company.getAllEmployees());
	}
	
	private static  void getEmployeesByMonthBirth(InputOutput io) {
		int month = io.readInt("Enter month of birth from 1 to 12", "Incorrect month", 1, 12);
		printEmployes(io, company.getEmployeesByMonthBirth(month));		
	}
	
	private static  void getEmployeesBySalary(InputOutput io) {
		int salaryFrom = io.readInt("Enter salaryFrom", "Incorrect salary");
		int salaryTo = io.readInt("Enter salaryTo", "Incorrect salary");
		printEmployes(io, company.getEmployeesBySalary(salaryFrom, salaryTo));
	}
	
	private static  void getEmployeesByDepartment(InputOutput io) {
		String department = io.readString("Enter department name");
		printEmployes(io, company.getEmployeesByDepartment(department));
	}
	
	private static  void addEmployee(InputOutput io) {
		int id = io.readInt("Enter id of Employee", "Wrong id");
		String name = io.readString("Enter employee name");
		LocalDate birthDate = io.readDateISO("Enter birthdate in format yyyy-mm-dd", "Enter correct date");
		String department = io.readString("Enter department name");
		int salary = io.readInt("Enter salary", "Incorrect salary");
		if (company.addEmployee(new Employee(id, name, birthDate, department, salary))) {
			io.writeLine("Employee added succesfully");
		} else {
			io.writeLine(String.format("Employee with id=%s already exists", id));
		}
	}
	
	private static  void removeEmployee(InputOutput io) {
		int id = io.readInt("Enter id of Employee", "Wrong id");
		Employee employee = company.removeEmployee(id);
		if (employee == null) {
			io.writeLine(String.format("No such employee with id=%s", id));
		} else {
			io.writeLine(String.format("Employee succesfully deleted: %s", employee.toString()));
		}
	}
}
