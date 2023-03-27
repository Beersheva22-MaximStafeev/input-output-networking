package telran.employees;

import org.junit.jupiter.api.*;

public class CompanyImplTestLocal extends CompanyImplTestAbstract {
	
	@BeforeAll
	static void setUp() {
		company = new CompanyImpl();
	}
	
}
