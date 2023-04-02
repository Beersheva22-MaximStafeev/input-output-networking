package telran.employees.application;

import telran.employees.application.controller.CompanyControllerItems;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;
import telran.employees.Company;
import telran.employees.CompanyImpl;

public class CompanyAppl {
	
	private static final String COMPANY_DATA_FILE_NAME = "company.data";

	public static void main(String[] args) {
		Company company = new CompanyImpl();
		company.restore(COMPANY_DATA_FILE_NAME);
		InputOutput io = new StandardInputOutput();
		CompanyControllerItems companyControllerItems = new CompanyControllerItems(company);
		buildMenu(companyControllerItems, company).perform(io);
	}
	
	private static Item buildMenu(CompanyControllerItems companyControllerItems, Company company) {
		return new Menu("Employees",
				companyControllerItems.getAdminItemMenu(),
				companyControllerItems.getUserItemMenu(),
				Item.of("Exit", io -> {company.save(COMPANY_DATA_FILE_NAME);}, true));
	}

}
