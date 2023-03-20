package telran.util;

import java.io.*;
import java.net.*;

public class TcpClientHandler implements Handler {

	private Socket socket;
	private PrintStream writer;
	private BufferedReader reader;
	
	public TcpClientHandler(String hostname, int port) throws Exception {
		socket = new Socket(hostname, port);
		writer = new PrintStream(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public int getCounter(Level level) {
		writer.printf("counter#%s%n", level.toString());
		String response = getResponse();
		return Integer.parseInt(response);
	}
	
	@Override
	public void publish(LoggerRecord loggerRecord) {
		writer.printf("log#%s:%s%n", loggerRecord.level.toString().toLowerCase(), loggerRecord.message);
		getResponse();
	}
	
	private String getResponse() {
		String response;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error connecting to server");
		}
//		if (response == null || !response.equals("OK")) {
//			throw new RuntimeException("Error posting log");
//		}
		return response;
	}
	
	
	@Override
	public void close() {
		Handler.super.close();
		try {
			reader.close();
			writer.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
