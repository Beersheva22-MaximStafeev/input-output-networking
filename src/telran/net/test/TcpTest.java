package telran.net.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.jupiter.api.*;
import telran.net.*;

public class TcpTest {
	static NetworkClient client; 
	@BeforeAll
	static void setUp() throws Exception {
		client = new TcpClient("localhost", 4000);
	}
	
	@Test
	void test() {
		assertEquals("olleH", client.send("reverse", "Hello"));
		Integer expected = 5;
		assertEquals(expected, client.send("length", "Hello"));
	}
	
	@AfterAll
	void finish() throws IOException {
		client.close();
	}
}
