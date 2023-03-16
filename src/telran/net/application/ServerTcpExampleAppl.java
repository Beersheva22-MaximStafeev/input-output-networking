package telran.net.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

public class ServerTcpExampleAppl {
	
	private static final int PORT = 4000;

	public static void main(String[] args) throws Exception {
		
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server listening on port " + PORT);
		while (true) {
			Socket socket = serverSocket.accept();
			runServerClient(socket);
		}
	}

	private static void runServerClient(Socket socket) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream writer = new PrintStream(socket.getOutputStream());
		while (true) {
			String request = reader.readLine();
			if (request == null) {
				break;
			}
			String response = getResponse(request);
			writer.println(response);
		}
		System.out.println("Client closed connection");
	}

	private static String getResponse(String request) {
		String res = "Wrong request";
		String[] tokens = request.split("#");
		if (tokens.length == 2) {
			res = switch (tokens[0]) {
				case "reverse" -> new StringBuilder(tokens[1]).reverse().toString();
				case "length" -> Integer.toString(tokens[1].length());
				default -> "Wrong type " + tokens[0];
				
			};
		}	
		return res;
	}
}
