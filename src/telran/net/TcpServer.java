package telran.net;

import java.io.*;
import java.net.*;

public class TcpServer implements Runnable {

	private Protocol protocol;
	private int port;
	private ServerSocket serverSocket;
	
	public TcpServer(Protocol protocol, int port) throws IOException {
		// TO DO Auto-generated constructor stub
		this.protocol = protocol;
		this.port = port;
		serverSocket = new ServerSocket(port);
		
	}
	
	@Override
	public void run() {
		// TO DO Auto-generated method stub
		System.out.println("Server listening on port " + port);
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected");
				TcpServerClient serverClient = new TcpServerClient(socket, protocol);
				serverClient.run();
			} catch (Exception e) {
				System.out.println(e.toString());
				System.out.println("Client disconnected");
			}
		}
	}

}
