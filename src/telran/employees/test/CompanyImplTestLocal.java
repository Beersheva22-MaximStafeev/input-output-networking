package telran.employees.test;

import org.junit.jupiter.api.*;

import telran.employees.CompanyImpl;

public class CompanyImplTestLocal extends CompanyImplTestAbstract {
	
	@BeforeAll
	static void setUp() {
		company = new CompanyImpl();
	}
	
}
