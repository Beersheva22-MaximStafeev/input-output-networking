package telran.net.application;

import java.io.IOException;

import telran.employees.CompanyImpl;
import telran.net.CompanyProtocol;
import telran.net.TcpServer;

public class CompanyTcpServer {
	public static final int PORT = 4000;
	public static void main(String[] args) throws IOException {
		TcpServer server = new TcpServer(new CompanyProtocol(new CompanyImpl()), PORT);
		server.run();
	}
}
