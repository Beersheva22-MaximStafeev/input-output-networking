package telran.employees.application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import telran.employees.Company;
import telran.employees.application.controller.CompanyControllerItemsDynamic;
import telran.employees.net.app.CompanyNetImplAbstract;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class CompanyUniversalAppl {
	
	private static final String COMPANY_DATA_FILE_NAME = "company.data";
	private static final String DEFAULT_CLIENT = "Tcp";
	private static final String DEFAULT_HOSTNAME = "localhost";
	private static final String DEFAULT_PORT = "4000";
	private static final String[] DEFAULT_DEPARTMENTS = new String[] {"QA", "Developers", "Management", "depart1", "depart2", "depart3", "department"};
	private static final String BASE_PACKAGE = "telran.employees.net.app.CompanyNetImpl"; 
	private static String client;
	private static String hostName;
	private static int port;
	private static String[] departments;
	
	public static void main(String[] args) throws Exception {
		readConfig(args);
		CompanyNetImplAbstract company = createCompany();
		company.restore(COMPANY_DATA_FILE_NAME);
		InputOutput io = new StandardInputOutput();
		CompanyControllerItemsDynamic companyControllerItems = new CompanyControllerItemsDynamic(company, departments);
		buildMenu(company, companyControllerItems).perform(io);
	}

	private static CompanyNetImplAbstract createCompany() throws Exception {
		@SuppressWarnings("unchecked")
		Class<CompanyNetImplAbstract> companyClass = (Class<CompanyNetImplAbstract>) Class.forName(BASE_PACKAGE + client);
		Constructor<CompanyNetImplAbstract> constructor = companyClass.getConstructor(String.class, int.class);
		return constructor.newInstance(hostName, port);
	}

	private static void readConfig(String[] args) throws Exception {
		// здесь по правильному мы должны проверить, что в файле присутствуют все 4 параметра и каждый из них заполнен
		Properties properties = new Properties();
		if (args.length != 1) {
			System.out.println("Must be one argument with file name of configuration");
		} else {
			if (Files.exists(Path.of(args[0]))) {
				properties.load(new FileInputStream(args[0]));
			}
		}
		client = properties.computeIfAbsent("client", x -> DEFAULT_CLIENT).toString();
		hostName = properties.computeIfAbsent("hostName", x -> DEFAULT_HOSTNAME).toString();
		port = Integer.parseInt(properties.computeIfAbsent("port", x -> DEFAULT_PORT).toString());
		departments = properties.computeIfAbsent("departments", x -> String.join(", ", DEFAULT_DEPARTMENTS)).toString().split(", ");
		if (args.length == 1) {
			if (!Files.exists(Path.of(args[0]))) {
				properties.store(new FileOutputStream(args[0]), "default configuration");
			}
		}
	}

	private static Item buildMenu(Company company, CompanyControllerItemsDynamic companyControllerItems) {
		return new Menu("Employees",
				companyControllerItems.getAdminItemMenu(),
				companyControllerItems.getUserItemMenu(),
				Item.of("Exit", io -> {company.save(COMPANY_DATA_FILE_NAME);}, true));
	}

}
