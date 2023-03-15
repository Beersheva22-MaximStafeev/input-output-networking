package telran.employees;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class CompanyImplSimple implements Company {

	private static final long serialVersionUID = 1L;
	
	private LinkedHashMap<Long, Employee> map = new LinkedHashMap<>();
	
	@Override
	public Iterator<Employee> iterator() {
		return map.values().iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		return map.putIfAbsent(employee.getId(), employee) == null;
	}

	@Override
	public Employee removeEmployee(long id) {
		return map.remove(id);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return map.values().stream().toList();
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		return map.values().stream()
				.filter(el -> el.getBirthDate().getMonth().getValue() == month)
				.toList();
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return map.values().stream()
				.filter(el -> el.getSalary() >= salaryFrom && el.getSalary() <= salaryTo)
				.toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return map.values().stream()
				.filter(el -> el.getDepartment().toLowerCase().equals(department.toLowerCase()))
				.toList();
	}

	@Override
	public Employee getEmployee(long id) {
		return map.get(id);
	}

	@Override
	public void save(String pathName) {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))) {
			output.writeObject(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
			map = (LinkedHashMap<Long, Employee>) input.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
