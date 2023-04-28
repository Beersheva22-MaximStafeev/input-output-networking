package telran.employees;

import java.io.Serializable;
import java.util.List;

public interface Company extends Iterable<Employee>, Serializable {
	/**
	 * adds employee to Company
	 * @param employee
	 * @return false, if exists employee with this id, otherwise true
	 */
	boolean addEmployee(Employee employee);
	
	/**
	 * 
	 * @param id
	 * @return reference to removed Emploeyee or null
	 */
	Employee removeEmployee(long id);
	
	/**
	 * get all Employees
	 * @return
	 */
	List<Employee> getAllEmployees();
	
	/**
	 * get all employees, who born in estimated month
	 * @param month
	 * @return
	 */
	List<Employee> getEmployeesByMonthBirth(int month);
	
	/**
	 * return Employees with salary in a given range
	 * @param salaryFrom
	 * @param salaryTo
	 * @return
	 */
	List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);
	
	/**
	 * 
	 * @param department
	 * @return
	 */
	List<Employee> getEmployeesByDepartment(String department);
	
	/**
	 * 
	 * @param id
	 * @return Employee or null
	 */
	Employee getEmployee(long id);
	
	/**
	 * saves Company to file
	 * @param pathName
	 */
	void save(String pathName);
	
	/**
	 * restore Company from file
	 * @param pathName
	 */
	void restore(String pathName);
	
	/**
	 * update salary
	 * @param emplId
	 * @param newSalary
	 */
	void updateSalary(long id, int newSalary);
	
	/**
	 * update department
	 * @param empId
	 * @param department
	 */
	void updateDepartment(long id, String department);
}
