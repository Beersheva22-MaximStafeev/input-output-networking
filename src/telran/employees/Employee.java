package telran.employees;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private LocalDate birthDate;
	private String department;
	private int salary;
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Employee)obj).id && 
				salary == ((Employee)obj).salary && 
				name.equals(((Employee)obj).name) && 
				birthDate.equals(((Employee)obj).birthDate) && 
				department.equals(((Employee)obj).department); 
	}
	
	@Override
	public int hashCode() {
		return (int) (id % 1000) + 
				salary % 1000 * 1000 + 
				name.hashCode() % 700 * 1_000_000 + 
				birthDate.hashCode() % 700 * 1_000_000  + 
				department.hashCode() % 700 * 1_000_000;
	}
	
	public Employee(long id, String name, LocalDate birthDate, String department, int salary) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.department = department;
		this.salary = salary;
	}

	public String getDepartment() {
		return department;
	}

	public int getSalary() {
		return salary;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	@Override
	public String toString() {
		return String.format("Id: %s, Name: %s, BirthDate: %s, Department: %s, Salary: %s", getId(), getName(), getBirthDate(), getDepartment(), getSalary());
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
}
