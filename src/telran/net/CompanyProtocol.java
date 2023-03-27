package telran.net;

import java.io.Serializable;

import telran.employees.*;

public class CompanyProtocol implements Protocol {
	
	Company company;
	
	public CompanyProtocol(Company company) {
		this.company = company;
	}

	@Override
	public Response getResponse(Request request) {
		try {
			return switch (request.type) {
				case "iterator" -> new Response(ResponseCode.OK, iterator(request.data)); 
				case "addEmployee" -> new Response(ResponseCode.OK, addEmployee(request.data)); 
				case "removeEmployee" -> new Response(ResponseCode.OK, removeEmployee(request.data)); 
				case "getAllEmployees" -> new Response(ResponseCode.OK, getAllEmployees(request.data)); 
				case "getEmployeesByMonthBirth" -> new Response(ResponseCode.OK, getEmployeesByMonthBirth(request.data)); 
				case "getEmployeesBySalary" -> new Response(ResponseCode.OK, getEmployeesBySalary(request.data)); 
				case "getEmployeesByDepartment" -> new Response(ResponseCode.OK, getEmployeesByDepartment(request.data)); 
				case "getEmployee" -> new Response(ResponseCode.OK, getEmployee(request.data)); 
				case "save" -> new Response(ResponseCode.OK, save(request.data)); 
				case "restore" -> new Response(ResponseCode.OK, restore(request.data)); 
				default -> new Response(ResponseCode.WRONG_REQUEST, request.type + " wrong request");
			};
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, null);
		}
	}

	private Serializable iterator(Serializable data) {
		return (Serializable) company.iterator();
	}

	private Serializable restore(Serializable data) {
		company.restore((String) data);
		return null;
	}

	private Serializable save(Serializable data) {
		company.save((String) data);
		return null;
	}

	private Serializable getEmployee(Serializable data) {
		return company.getEmployee((long) data);
	}

	private Serializable getEmployeesByDepartment(Serializable data) {
		return (Serializable) company.getEmployeesByDepartment((String) data);
	}

	private Serializable getEmployeesBySalary(Serializable data) {
		return (Serializable) company.getEmployeesBySalary(((int[]) data)[0], ((int[]) data)[1]);
	}

	private Serializable getEmployeesByMonthBirth(Serializable data) {
		return (Serializable) company.getEmployeesByMonthBirth((int) data);
	}

	private Serializable getAllEmployees(Serializable data) {
		return (Serializable) company.getAllEmployees();
	}

	private Serializable removeEmployee(Serializable data) {
		return company.removeEmployee((long) data);
	}

	private Serializable addEmployee(Serializable data) {
		return company.addEmployee((Employee) data);
	}

}
