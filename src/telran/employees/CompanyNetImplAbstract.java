package telran.employees;

import java.util.Iterator;
import java.util.List;

import telran.net.NetworkClient;

public abstract class CompanyNetImplAbstract implements Company {

	private static final long serialVersionUID = 1L;
	private NetworkClient networkClient;
	
	public NetworkClient getNetworkClient() {
		return networkClient;
	}

	public void setNetworkClient(NetworkClient networkClient) {
		this.networkClient = networkClient;
	}

	@Override
	public Iterator<Employee> iterator() {
		return getAllEmployees().iterator();
	}

	@Override
	public boolean addEmployee(Employee employee) {
		return networkClient.send("addEmployee", employee);
	}

	@Override
	public Employee removeEmployee(long id) {
		return networkClient.send("removeEmployee", id);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return networkClient.send("getAllEmployees", null);
	}

	@Override
	public List<Employee> getEmployeesByMonthBirth(int month) {
		return networkClient.send("getEmployeesByMonthBirth", month);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return networkClient.send("getEmployeesBySalary", new int[] {salaryFrom, salaryTo});
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return networkClient.send("getEmployeesByDepartment", department);
	}

	@Override
	public Employee getEmployee(long id) {
		return networkClient.send("getEmployee", id);
	}

	@Override
	public void save(String pathName) {
		networkClient.send("save", pathName);
	}

	@Override
	public void restore(String pathName) {
		networkClient.send("restore", pathName);		
	}

}
