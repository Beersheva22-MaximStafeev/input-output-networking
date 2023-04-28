package telran.employees;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CompanyImpl implements Company {

	enum LockType {
		READ, WRITE;
	}
	
	private static final long serialVersionUID = 1L;
	
	private HashMap<Long, Employee> employeesMap = new HashMap<>();
	// indexes
//	private LinkedHashSet<Employee> iterator;
	// this iterator is bad,because we can change something outside our class 
	private HashMap<Integer, HashSet<Employee>> indexMonth;
	private TreeMap<Integer, HashSet<Employee>> indexSalary;
	private HashMap<String, HashSet<Employee>> indexDepartment;
	
	Object[] resourcesToLock = new Object[] {employeesMap, indexMonth, indexSalary, indexDepartment};
	private Map<Object, Map<LockType, Lock>> locks = new HashMap<>();
	private Map<Object, ReentrantReadWriteLock> globalLocks = new HashMap<>();
	
	public CompanyImpl() {
		createLocks();
		flushIndexes();
	}
	
	private void createLocks() {
		Arrays.stream(resourcesToLock).forEach(el -> {
			ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
			globalLocks.put(el, lock);
			locks.computeIfAbsent(el, i -> new HashMap<>()).put(LockType.READ, lock.readLock());
			locks.computeIfAbsent(el, i -> new HashMap<>()).put(LockType.WRITE, lock.writeLock());
		});
	}
	
	private void lock(LockType type, Object ... objects) {
		Set<Object> objectsSet = new HashSet<>(Arrays.asList(objects));
		Arrays.stream(resourcesToLock)
				.filter(objectsSet::contains)
				.forEach(el -> locks.get(el).get(type).lock());
	}
	
	private void unlock(LockType type, Object ... objects) {
		Set<Object> objectsSet = new HashSet<>(Arrays.asList(objects));
		Arrays.stream(resourcesToLock)
				.filter(objectsSet::contains)
				.forEach(el -> locks.get(el).get(type).unlock());
	}

	@Override
	public Iterator<Employee> iterator() {
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		try {
			lock(LockType.WRITE, employeesMap);
			boolean res = employeesMap.putIfAbsent(employee.getId(), employee) == null;
			if (res) {
				addToIndexes(employee);
			}
			return res;
		} finally {
			unlock(LockType.WRITE, employeesMap);
		}
	}

	private void addToIndexes(Employee employee) {
		// TO_DO Fill all indexes here
//		iterator.add(employee);
		try {
			lock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);

			int month = employee.getBirthDate().getMonthValue();
			indexMonth.computeIfAbsent(month, k -> new HashSet<>()).add(employee);
			
			indexSalary.computeIfAbsent(employee.getSalary(), k -> new HashSet<>()).add(employee);
			
			indexDepartment.computeIfAbsent(employee.getDepartment(), k -> new HashSet<>()).add(employee);
		} finally {
			unlock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);
		}
	}

	private void removeFromIndexes(Employee employee) {
		// TO_DO Fill all indexes here
//		iterator.remove(employee);
		try {
//			lock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);
	
			int month = employee.getBirthDate().getMonthValue();
			indexMonth.get(month).remove(employee);
			indexSalary.get(employee.getSalary()).remove(employee);
			indexDepartment.get(employee.getDepartment()).remove(employee);
		} finally {
//			unlock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);
		}
	}

	private void renewIndexes() {
		try {
			lock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);
			flushIndexes();
			employeesMap.forEach((k, v) -> addToIndexes(v));
		} finally {
			unlock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);
		}
		
	}

	private void flushIndexes() {
//		iterator = new LinkedHashSet<>();
		try {
//			lock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);
			indexMonth = new HashMap<>();
			indexSalary = new TreeMap<>();
			indexDepartment = new HashMap<>(); 
		} finally {
//			unlock(LockType.WRITE, indexDepartment, indexMonth, indexSalary);
		}
	}

	@Override
	public Employee removeEmployee(long id) {
		try {
			lock(LockType.WRITE, employeesMap);
			Employee employee = employeesMap.remove(id);
			if (employee != null) {
				removeFromIndexes(employee);
			}
			return employee;
		} finally {
			unlock(LockType.WRITE, employeesMap);
		}
	}

	@Override
	public List<Employee> getAllEmployees() {
		try {
			lock(LockType.READ, employeesMap);
//			return new ArrayList<>(map.values());
			return employeesMap.values().stream().toList();
		} finally {
			unlock(LockType.READ, employeesMap);
		}
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		try {
			lock(LockType.READ, indexMonth);
			return indexMonth.getOrDefault(month, new HashSet<>()).stream().toList();
		} finally {
			unlock(LockType.READ, indexMonth);
		}
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		try {
			lock(LockType.READ, indexSalary);
			return indexSalary.subMap(salaryFrom, true, salaryTo, true).values().stream()
					.flatMap(el -> el.stream()) //flatMap(Set::stream) 
					.toList();
		} finally {
			unlock(LockType.READ, indexSalary);
		}
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		try {
			lock(LockType.READ, indexDepartment);
			return indexDepartment.getOrDefault(department, new HashSet<>()).stream().toList();
		} finally {
			unlock(LockType.READ, indexDepartment);
		}
	}

	@Override
	public Employee getEmployee(long id) {
		try {
			lock(LockType.READ, employeesMap);
			return employeesMap.get(id);
		} finally {
			unlock(LockType.READ, employeesMap);
		}
	}

	@Override
	public void save(String pathName) {
		try {
			lock(LockType.READ, employeesMap);
			try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(pathName))) {
				output.writeObject(employeesMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			unlock(LockType.READ, employeesMap);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(String pathName) {
		try {
			lock(LockType.WRITE, employeesMap);
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(pathName))) {
				employeesMap = (HashMap<Long, Employee>) input.readObject();
				renewIndexes();			
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			unlock(LockType.WRITE, employeesMap);
		}
	}

	@Override
	public void updateSalary(long id, int newSalary) {
		try {
			lock(LockType.READ, employeesMap);
			lock(LockType.WRITE, indexSalary);
			Employee employee = employeesMap.get(id);
			if (employee != null) {
				employee.setSalary(newSalary);
				indexSalary.get(newSalary).remove(employee);
				indexSalary.computeIfAbsent(newSalary, k -> new HashSet<>()).add(employee);
			}
		} finally {
			unlock(LockType.READ, employeesMap);
			unlock(LockType.WRITE, indexSalary);
		}
	}

	@Override
	public void updateDepartment(long id, String department) {
		try {
			lock(LockType.WRITE, employeesMap, indexDepartment);
			Employee employee = employeesMap.get(id);
			if (employee != null) {
				employee.setDepartment(department);
				indexDepartment.get(department).remove(employee);
				indexDepartment.computeIfAbsent(department, k -> new HashSet<>()).add(employee);
			}
		} finally {
			unlock(LockType.WRITE, employeesMap, indexDepartment);
		}
	}

}
