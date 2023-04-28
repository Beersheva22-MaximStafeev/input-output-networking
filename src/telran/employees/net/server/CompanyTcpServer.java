package telran.employees.net.server;

import java.io.IOException;
import java.util.Scanner;

import telran.employees.Company;
import telran.employees.CompanyImpl;
import telran.employees.net.CompanyProtocol;
import telran.net.TcpServer;

public class CompanyTcpServer {
	
	public static final int PORT = 4000;
	private static final String COMPANY_DATA_FILE_NAME = "company.data";

	public static void main(String[] args) throws IOException {
		System.out.println("Main thread started");
		Company company = new CompanyImpl();
		company.restore(COMPANY_DATA_FILE_NAME);
		TcpServer server = new TcpServer(new CompanyProtocol(company), PORT);
		Thread thread = new Thread(server);
		thread.start();
//		server.run();
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		while (running) {
			System.out.println("For stopping server enter command 'shutdown'");
			String line = scanner.nextLine();
			if (line.equals("shutdown")) {
//				company.save("");
				running = false;
//				break;
			}
		}
		scanner.close();
		server.shutdown();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		company.save(COMPANY_DATA_FILE_NAME);
		System.out.println("Main thread stopped and company saved");
	}
}
