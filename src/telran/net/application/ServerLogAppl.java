package telran.net.application;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import telran.util.*;

public class ServerLogAppl {

	private static final int PORT = 4000;
	private static HashMap <Level, Integer> map = new HashMap<>();

	public static void main(String[] args) throws Exception {
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server listening on port " + PORT);
		while (true) {
			Socket socket = serverSocket.accept();
			runServerClient(socket);
		}
	}

	private static void runServerClient(Socket socket) throws Exception {
		System.out.println("Client connected: " + socket.getRemoteSocketAddress());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream writer = new PrintStream(socket.getOutputStream());
		while (true) {
			String request;
			try {
				request = reader.readLine();
			} catch (Exception e) {
				break;
			}
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
			res = switch (tokens[0].toLowerCase()) {
				case "log" -> postLog(tokens[1]);
				case "counter" -> getCounter(tokens[1]);
				default -> "Wrong type " + tokens[0];
				
			};
		}	
		return res;
	}

	private static String getCounter(String levelStr) {
		Level level = Level.valueOf(levelStr.toUpperCase());
		return level == null ? "ERROR" : map.getOrDefault(level, 0).toString();
	}

	private static String postLog(String logMessage) {
		int index = logMessage.indexOf(":");
		String result = "ERROR";
		if (index > -1) {
			Level level = Level.valueOf(logMessage.substring(0, index).toUpperCase());
			if (level != null) {
				map.merge(level, 1, Integer::sum);
				result = "OK";
			}
		}
		return result;
	}

}
