package telran.net.application;

import java.io.IOException;

import telran.employees.CompanyImpl;
import telran.net.CompanyProtocol;
import telran.net.UdpServer;

public class CompanyUdpServer {
	public static final int PORT = 4000;
	public static void main(String[] args) throws IOException, Exception {
		UdpServer server = new UdpServer(new CompanyProtocol(new CompanyImpl()), PORT);
		server.run();
	}
}
