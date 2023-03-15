package telran.employees;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class CompanyImpl implements Company {

	private static final long serialVersionUID = 1L;
	
	private HashMap<Long, Employee> map = new HashMap<>();
	// indexes
	private LinkedHashSet<Employee> iterator; 
	private HashMap<Integer, HashSet<Employee>> indexMonth;
	private TreeMap<Integer, HashSet<Employee>> indexSalary;
	private HashMap<String, HashSet<Employee>> indexDepartment;
	
	public CompanyImpl() {
		flushIndexes();
	}
	
	@Override
	public Iterator<Employee> iterator() {
		return iterator.iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		boolean res = map.putIfAbsent(employee.getId(), employee) == null;
		if (res) {
			addToIndexes(employee);
		}
		return res;
	}

	private void addToIndexes(Employee employee) {
		// TO_DO Fill all indexes here
		iterator.add(employee);
		
		int month = employee.getBirthDate().getMonthValue();
		indexMonth.computeIfAbsent(month, k -> new HashSet<>()).add(employee);
		
		indexSalary.computeIfAbsent(employee.getSalary(), k -> new HashSet<>()).add(employee);
		
		indexDepartment.computeIfAbsent(employee.getDepartment(), k -> new HashSet<>()).add(employee);
	}

	private void removeFromIndexes(Employee employee) {
		// TO_DO Fill all indexes here
		iterator.remove(employee);

		int month = employee.getBirthDate().getMonthValue();
		indexMonth.get(month).remove(employee);
		
		indexSalary.get(employee.getSalary()).remove(employee);
		
		indexDepartment.get(employee.getDepartment()).remove(employee);
	}

	private void renewIndexes() {
		flushIndexes();
		map.forEach((k, v) -> addToIndexes(v));
		
	}

	private void flushIndexes() {
		iterator = new LinkedHashSet<>(); 
		indexMonth = new HashMap<>();
		indexSalary = new TreeMap<>();
		indexDepartment = new HashMap<>(); 
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee employee = map.remove(id);
		if (employee != null) {
			removeFromIndexes(employee);
		}
		return employee;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return iterator.stream().toList();
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		return indexMonth.getOrDefault(month, new HashSet<>()).stream().toList();
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return indexSalary.subMap(salaryFrom, true, salaryTo, true).values().stream()
				.flatMap(el -> el.stream())
				.toList();
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return indexDepartment.getOrDefault(department, new HashSet<>()).stream().toList();
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
			map = (HashMap<Long, Employee>) input.readObject();
			renewIndexes();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
