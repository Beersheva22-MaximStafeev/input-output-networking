package telran.employees.application.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import telran.employees.Company;
import telran.employees.Employee;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class CompanyControllerItemsDynamic {
	
	private Company company;
	private String[] departments;
	
	public CompanyControllerItemsDynamic(Company company, String[] departments) {
		this.company = company;
		this.departments = departments;
	}
	
	public Item getUserItemMenu() {
		return new Menu("User menu",
				Item.of("getEmployee", io -> getEmployee(io)),
				Item.of("getAllEmployees", io -> getAllEmployees(io)),
				Item.of("getEmployeesByMonthBirth", io -> getEmployeesByMonthBirth(io)),
				Item.of("getEmployeesBySalary", io -> getEmployeesBySalary(io)),
				Item.of("getEmployeesByDepartment", io -> getEmployeesByDepartment(io)),
				Item.exit());
	}
	
	public Item getAdminItemMenu() {
		return new Menu("Admin menu",
				Item.of("addEmployee", io -> addEmployee(io)),
				Item.of("removeEmployee", io -> removeEmployee(io)),
				Item.exit());
	}
	
	private void getEmployee(InputOutput io) {
		int id = io.readInt("Enter id of Employee", "Wrong id");
		Employee employee = company.getEmployee(id);
		if (employee == null) {
			io.writeLine(String.format("No such employee with id=%s", id));
		} else {
			io.writeLine(employee.toString());
		}
	}
	
	private void printEmployes(InputOutput io, List<Employee> employees, String filter) {
		io.writeLine(String.format("Find %s employee%s %s:", employees.size(), employees.size() == 1 ? "" : "s", filter ));
		employees.forEach(empl -> io.writeLine(empl.toString()));
	}
	
	private void getAllEmployees(InputOutput io) {
		printEmployes(io, company.getAllEmployees(),"");
	}
	
	private void getEmployeesByMonthBirth(InputOutput io) {
		int month = io.readInt("Enter month of birth from 1 to 12", "Incorrect month", 1, 12);
		printEmployes(io, company.getEmployeesByMonthBirth(month),"in month " + month + " of bithday");		
	}
	
	private void getEmployeesBySalary(InputOutput io) {
		int salaryFrom = io.readInt("Enter salaryFrom", "Incorrect salary");
		int salaryTo = io.readInt("Enter salaryTo", "Incorrect salary");
		printEmployes(io, company.getEmployeesBySalary(salaryFrom, salaryTo), String.format("with salary from %s to %s", salaryFrom, salaryTo));
	}
	
	private String readDepartment(InputOutput io) {
		io.writeLine("Choose department from list:");
		IntStream.range(0, departments.length).forEach(index -> io.writeLine(String.format("%s. %s", index + 1, departments[index])));
		int depNo = io.readInt("Enter departnemt number", "Wrong department number", 1, departments.length);
		return departments[depNo - 1];
	}
	
	private void getEmployeesByDepartment(InputOutput io) {
		String department = readDepartment(io);
		printEmployes(io, company.getEmployeesByDepartment(department), "in department <" + department + ">");
	}
	
	private void addEmployee(InputOutput io) {
		int id = io.readInt("Enter id of Employee", "Wrong id");
		if (company.getEmployee(id) == null) {
			String name = io.readString("Enter employee name");
			LocalDate birthDate = io.readDateISO("Enter birthdate in format yyyy-mm-dd", "Enter correct date");
			String department = readDepartment(io);
			int salary = io.readInt("Enter salary", "Incorrect salary");
			if (company.addEmployee(new Employee(id, name, birthDate, department, salary))) {
				io.writeLine("Employee added succesfully");
			} else {
				io.writeLine(String.format("Employee with id=%s already exists", id));
			}
		} else {
			io.writeLine(String.format("Employee with this id %s already exists", id));
		}
	}
	
	private void removeEmployee(InputOutput io) {
		int id = io.readInt("Enter id of Employee", "Wrong id");
		Employee employee = company.removeEmployee(id);
		if (employee == null) {
			io.writeLine(String.format("No such employee with id=%s", id));
		} else {
			io.writeLine(String.format("Employee succesfully deleted: %s", employee.toString()));
		}
	}
}
