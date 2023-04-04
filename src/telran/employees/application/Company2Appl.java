package telran.employees.application;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.application.controller.CompanyControllerItemsStatic;
import telran.employees.net.app.CompanyNetImplTcp;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;
import java.util.*;

public class Company2Appl {
	private static final String COMPANY_DATA_FILE_NAME = "company.data";
	private static final int PORT = 4000;
	private static final String LOCALHOST = "localhost";

	public static void main(String[] args) {
		Company company;
		InputOutput io = new StandardInputOutput();
		Company company2;

		try {
			company = new CompanyNetImplTcp(LOCALHOST, PORT);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		company.restore(COMPANY_DATA_FILE_NAME);
		
		company2 = new CompanyImpl();
		company2.restore(COMPANY_DATA_FILE_NAME);

		buildMenu(buildMenu("Tcp", company), buildMenu("Local", company2)).perform(io);
	}
	
	private static Item buildMenu(String name, Company company) {
		return new Menu(name,
				CompanyControllerItemsStatic.getAdminItemMenu(company),
				CompanyControllerItemsStatic.getUserItemMenu(company),
				Item.of("Exit", io -> {company.save(COMPANY_DATA_FILE_NAME);}, true));
	}
	
	private static Item buildMenu(Item ... items) {
		ArrayList<Item> list = new ArrayList<>(Arrays.asList(items));
		list.add(Item.exit());
		return new Menu("All lists", list);
	}
}
