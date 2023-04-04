package telran.employees.application;

import telran.employees.Company;
import telran.employees.application.controller.CompanyControllerItemsDynamic;
import telran.employees.net.app.CompanyNetImplTcp;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyNetAppl {
	private static final String COMPANY_DATA_FILE_NAME = "company.data";
	private static final int PORT = 4000;
	private static final String LOCALHOST = "localhost";
	private static final String[] departments = new String[] {"QA", "Developers", "Management", "depart1", "depart2", "depart3", "department"};

	public static void main(String[] args) {
		Company company;
		try {
			company = new CompanyNetImplTcp(LOCALHOST, PORT);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		company.restore(COMPANY_DATA_FILE_NAME);
		InputOutput io = new StandardInputOutput();
		CompanyControllerItemsDynamic companyControllerItems = new CompanyControllerItemsDynamic(company, departments);
		buildMenu(company, companyControllerItems).perform(io);
	}
	
	private static Item buildMenu(Company company, CompanyControllerItemsDynamic companyControllerItems) {
		return new Menu("Employees",
				companyControllerItems.getAdminItemMenu(),
				companyControllerItems.getUserItemMenu(),
				Item.of("Exit", io -> {company.save(COMPANY_DATA_FILE_NAME);}, true));
	}

}
