package telran.net.test;

import java.io.IOException;
import telran.net.*;

public class TcpServerTest {
	public static final int PORT = 4000;
	public static void main(String[] args) throws IOException {
		TcpServer server = new TcpServer(new ProtocolTest(), PORT);
		server.run();
	}
}
