package telran.employees.application.controller;

import java.time.LocalDate;
import java.util.List;

import telran.employees.Company;
import telran.employees.Employee;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class CompanyControllerItemsStatic {
	
	// private constructor to prevent create of object of class
	private CompanyControllerItemsStatic(Company company) {
	}
	
	public static Item getUserItemMenu(Company company) {
		return new Menu("User menu",
				Item.of("getEmployee", io -> getEmployee(io, company)),
				Item.of("getAllEmployees", io -> getAllEmployees(io, company)),
				Item.of("getEmployeesByMonthBirth", io -> getEmployeesByMonthBirth(io, company)),
				Item.of("getEmployeesBySalary", io -> getEmployeesBySalary(io, company)),
				Item.of("getEmployeesByDepartment", io -> getEmployeesByDepartment(io, company)),
				Item.exit());
	}
	
	public static  Item getAdminItemMenu(Company company) {
		return new Menu("Admin menu",
				Item.of("addEmployee", io -> addEmployee(io, company)),
				Item.of("removeEmployee", io -> removeEmployee(io, company)),
				Item.exit());
	}
	
	private static  void getEmployee(InputOutput io, Company company) {
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
	
	private static  void getAllEmployees(InputOutput io, Company company) {
		printEmployes(io, company.getAllEmployees());
	}
	
	private static  void getEmployeesByMonthBirth(InputOutput io, Company company) {
		int month = io.readInt("Enter month of birth from 1 to 12", "Incorrect month", 1, 12);
		printEmployes(io, company.getEmployeesByMonthBirth(month));		
	}
	
	private static  void getEmployeesBySalary(InputOutput io, Company company) {
		int salaryFrom = io.readInt("Enter salaryFrom", "Incorrect salary");
		int salaryTo = io.readInt("Enter salaryTo", "Incorrect salary");
		printEmployes(io, company.getEmployeesBySalary(salaryFrom, salaryTo));
	}
	
	private static  void getEmployeesByDepartment(InputOutput io, Company company) {
		String department = io.readString("Enter department name");
		printEmployes(io, company.getEmployeesByDepartment(department));
	}
	
	private static  void addEmployee(InputOutput io, Company company) {
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
	
	private static  void removeEmployee(InputOutput io, Company company) {
		int id = io.readInt("Enter id of Employee", "Wrong id");
		Employee employee = company.removeEmployee(id);
		if (employee == null) {
			io.writeLine(String.format("No such employee with id=%s", id));
		} else {
			io.writeLine(String.format("Employee succesfully deleted: %s", employee.toString()));
		}
	}
}
