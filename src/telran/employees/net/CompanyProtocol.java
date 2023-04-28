package telran.employees.net;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import telran.employees.*;
import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

@SuppressWarnings("unused")
public class CompanyProtocol implements Protocol {
	
	Company company;
	
	public CompanyProtocol(Company company) {
		this.company = company;
	}

	@Override
	public Response getResponse(Request request) {
		try {
			Method method = CompanyProtocol.class.getDeclaredMethod(request.type, Serializable.class);
			Serializable responseData = (Serializable) method.invoke(this, request.data);
			return new Response(ResponseCode.OK, responseData);
		} catch (NoSuchMethodException e) {
			return new Response(ResponseCode.WRONG_REQUEST, request.type + " wrong request, error: " + e.toString());
		} catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.getMessage());
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
	
	private Serializable updateDepartment(Serializable data) {
		@SuppressWarnings("unchecked")
		PairId<String> idDepartment = (PairId<String>) data;
		company.updateDepartment(idDepartment.id(), idDepartment.value());
		return "";
	}

}
