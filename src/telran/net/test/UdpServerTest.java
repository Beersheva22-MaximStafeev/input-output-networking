package telran.net.test;

import java.io.IOException;

import telran.net.*;

public class UdpServerTest {
	public static final int PORT = 4000;
	public static void main(String[] args) throws IOException, Exception {
		UdpServer server = new UdpServer(new ProtocolTest(), PORT);
		server.run();
	}
}
