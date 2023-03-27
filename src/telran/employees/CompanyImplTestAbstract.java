package telran.employees;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class CompanyImplTestAbstract {
	private static final int STRESS_EMPLOYEES_COUNT = 1_000_000;
	private static final String COMPANY_DATA_FILE_NAME = "company.data";
	private static final int MIN_SALARY = 100;
	private static final int MAX_SALARY = 10_000;
	static Company company;
	List<Employee> empl;
	String[] departments = new String[]{"depart1", "depart2", "depart3", "depart4", "depart5", "depart6", "depart7", "depart8", "depart9", "depart10", "depart11", "depart12", "depart13", "depart14"};
	
	@BeforeEach
	void setUpEach() {
		company.getAllEmployees().forEach(el -> company.removeEmployee(el.getId()));
		empl = new ArrayList<>();
		empl.add(new Employee(1, "Alex", LocalDate.of(2000, 1, 1), "depart1", 100));
		empl.add(new Employee(2, "Alex", LocalDate.of(2000, 2, 2), "depart2", 101));
		empl.add(new Employee(3, "Boris", LocalDate.of(2000, 3, 3), "depart1", 102));
		empl.add(new Employee(4, "Catrine", LocalDate.of(2000, 4, 4), "depart2", 103));
		empl.add(new Employee(5, "Dina", LocalDate.of(2000, 1, 5), "depart3", 104));
		empl.add(new Employee(6, "Evgen", LocalDate.of(2000, 2, 6), "depart3", 101));
		for (Employee e: empl) {
			assertTrue(company.addEmployee(e));
		}
	}
	
	private Employee getRandomEmpl(int id) {
		String name = "VasyaAlways";
		int month = new Random().nextInt(1, 13);
		int departmentIndex = new Random().nextInt(0, departments.length);
		int salary = new Random().nextInt(MIN_SALARY, MAX_SALARY);
		return new Employee(id, name, LocalDate.of(2000, month, 1), departments[departmentIndex], salary);
	}

	@Test
	@Order(value = 1)
	void saveTest() {
		company.save(COMPANY_DATA_FILE_NAME);
	}
	
	@Test
	@Order(value = 2)
	void restoreTest() {
		Company company1 = new CompanyImpl();
		company1.restore(COMPANY_DATA_FILE_NAME);
		for (Employee el: company) {
			Employee el1 = company1.getEmployee(el.getId());
			assertEquals(el.getBirthDate(), el1.getBirthDate());
			assertEquals(el.getDepartment(), el1.getDepartment());
			assertEquals(el.getName(), el1.getName());
			assertEquals(el.getSalary(), el1.getSalary());
		}
	}
	
	@Test
	@Order(value = 3)
	void saveRestoreTest() {
		company.save(COMPANY_DATA_FILE_NAME);
		while (company.getAllEmployees().size() > 0) {
			company.removeEmployee(company.getAllEmployees().get(0).getId());
		}
		company.restore(COMPANY_DATA_FILE_NAME);
		for (int i = 0; i < empl.size(); i++) {
			empl.set(i, company.getEmployee(empl.get(i).getId()));
		}
		removeTest();
	}
	
	@Test
	void addTest() {
		assertFalse(company.addEmployee(new Employee(1, "", LocalDate.of(1900, 1, 1), "", 0)));
		assertEquals(empl.size(), company.getAllEmployees().size());
	}

	@Test
	void removeTest() {
		while (empl.size() > 0) {
			int index = new Random().nextInt(0, empl.size());
			assertTrue(company.removeEmployee(empl.get(index).getId()).equals(empl.get(index)));
			empl.remove(index);
			getEmployeesByDepartmentTest();
			getEmployeesByMonthBirthTest();
			getEmployeesBySalaryTest();
			getEmployeeTest();
			getAllEmployeesTest();
		}
		assertNull(company.removeEmployee(10));
	}
	

	@Test
	void getEmployeesByMonthBirthTest() {
		for (int i = 1; i <= 12; i++) {
			final int month = i;
			assertIterableEquals(
					company.getEmployeesByMonthBirth(i).stream()
						.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
						.toList(), 
					empl.stream()
						.filter(el -> el.getBirthDate().getMonthValue() == month)
						.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
						.toList()
					);
		}
	}
	
	@Test
	void getEmployeesBySalaryTest() {
		for (int i = 99 - 2; i < 106; i++) {
			for (int j = i; j < 106; j++) {
				int min = i;
				int max = j;
				assertIterableEquals(
						company.getEmployeesBySalary(min, max).stream()
							.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
							.toList(), 
						empl.stream()
							.filter(el -> el.getSalary() >= min && el.getSalary() <= max)
							.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
							.toList()
						);
			}
		}
	}
	
	@Test
	void getEmployeesByDepartmentTest() {
		for (String depart: departments) {
			String department = depart;
			assertIterableEquals(
					company.getEmployeesByDepartment(department).stream()
						.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
						.toList(), 
					empl.stream()
						.filter(el -> el.getDepartment().equals(department))
						.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
						.toList()
					);
		}
	}
	
	@Test
	void getEmployeeTest() {
		for (int i = 0; i < empl.size(); i++) {
			assertEquals(empl.get(i), company.getEmployee(empl.get(i).getId()));
		}
	}
	
	@Test
	void getAllEmployeesTest() {
		assertIterableEquals(
				company.getAllEmployees().stream()
					.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
					.toList(), 
				empl.stream()
					.sorted((a, b) -> Long.compare(a.getId(), b.getId()))
					.toList()
				);
	}
	
	@Test
	void iteratorTest() {
		Iterator<Employee> it = company.iterator();
		while (it.hasNext()) {
			Employee e = it.next();
			assertEquals(e, empl.get( ((Long)e.getId()).intValue() - 1 ));
		}
	}
	
	@Test
	@Disabled
	void stressTest() {
		/*
		 * local times
		 * 		when STRESS_EMPLOYEES_COUNT == 1_000_000
		 * 		and MIN_SALARY = 100;
		 * 		and MAX_SALARY = 10_000;
		 * Add simple takes   : 423 ms
		 * Add good takes     : 2 643 ms
		 * Filter simple takes: 125 158 ms
		 * Filter good takes  : 237 ms
		 */
		long start = System.currentTimeMillis();
		Company companyStressSimple = new CompanyImplSimple();
		Company companyStressGood = new CompanyImpl();
		stressTestAdd(companyStressSimple);
		System.out.printf("Add simple takes   : %,d ms%n", System.currentTimeMillis() - start);
		start = System.currentTimeMillis();
		stressTestAdd(companyStressGood);
		System.out.printf("Add good takes     : %,d ms%n", System.currentTimeMillis() - start);
		start = System.currentTimeMillis();
		stressTestFilter(companyStressSimple);
		System.out.printf("Filter simple takes: %,d ms%n", System.currentTimeMillis() - start);
		start = System.currentTimeMillis();
		stressTestFilter(companyStressGood);
		System.out.printf("Filter good takes  : %,d ms%n", System.currentTimeMillis() - start);
		start = System.currentTimeMillis();
	}
	
	void stressTestAdd(Company comp) {
		empl = new ArrayList<>();
		empl.add(new Employee(1, "Alex", LocalDate.of(2000, 1, 1), "depart1", 100));
		empl.add(new Employee(2, "Alex", LocalDate.of(2000, 2, 2), "depart2", 101));
		empl.add(new Employee(3, "Boris", LocalDate.of(2000, 3, 3), "depart1", 102));
		empl.add(new Employee(4, "Catrine", LocalDate.of(2000, 4, 4), "depart2", 103));
		empl.add(new Employee(5, "Dina", LocalDate.of(2000, 1, 5), "depart3", 104));
		empl.add(new Employee(6, "Evgen", LocalDate.of(2000, 2, 6), "depart3", 101));
		for (int i = 10; i < STRESS_EMPLOYEES_COUNT; i++) {
			empl.add(getRandomEmpl(i));
		}
		for (Employee e: empl) {
			assertTrue(comp.addEmployee(e));
		}
	}
	
	void stressTestFilter(Company comp) {
		for (int j = MIN_SALARY - 1; j < MAX_SALARY + 1; j++) {
			comp.getEmployeesBySalary(j, j);
		}
		for (String department: departments) {
			comp.getEmployeesByDepartment(department);
		}
		comp.getEmployeesByDepartment("depppp");
		for (int i = 1; i <= 12; i++) {
			comp.getEmployeesByMonthBirth(i);
		}
	}
}
