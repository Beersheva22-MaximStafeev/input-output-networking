package telran.employees.application;

import telran.employees.application.controller.CompanyControllerItemsDynamic;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;
import telran.employees.Company;
import telran.employees.CompanyImpl;

public class CompanyAppl {
	
	private static final String COMPANY_DATA_FILE_NAME = "company.data";
	private static final String[] departments = new String[] {"QA", "Developers", "Management", "depart1", "depart2", "depart3", "department"};

	public static void main(String[] args) {
		Company company = new CompanyImpl();
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
