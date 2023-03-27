package telran.net;

import java.io.*;
import java.net.*;

public class TcpServerClient implements Runnable {
//	private Socket socket;
	private Protocol protocol;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public TcpServerClient(Socket socket, Protocol protocol) throws IOException {
		// TOD O Auto-generated constructor stub
//		this.socket = socket;
		this.protocol = protocol;
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {
		// TO DO Auto-generated method stub
		while (true) {
			Request request;
			try {
				request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.writeObject(response);
			} catch (EOFException e) {
				System.out.println("Client closed connection");
				break;
			} catch (Exception e) {
				throw new RuntimeException(e.toString());
			}
		}

	}

}
