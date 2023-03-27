package telran.employees;

import org.junit.jupiter.api.*;

public class CompanyImplTestTcp extends CompanyImplTestAbstract {
	
	
	private static final int PORT = 4000;
	private static final String LOCALHOST = "localhost";
	
	@BeforeAll
	static void setUpAll() {
		try {
			company = new CompanyNetImplTcp(LOCALHOST, PORT);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
