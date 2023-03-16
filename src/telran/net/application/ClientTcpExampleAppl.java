package telran.net.application;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ClientTcpExampleAppl {

	private static final String HOSTNAME = "localhost";
	private static final int PORT = 4000;

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket(HOSTNAME, PORT);
		PrintStream writer = new PrintStream(socket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		runClient(socket, writer, reader);
	}

	private static void runClient(Socket socket, PrintStream writer, BufferedReader reader) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Enter request: <type>#<string> or exit");
			String line = scanner.nextLine();
			if (line.equalsIgnoreCase("exit")) {
				break;
			}
			writer.println(line);
			String response = reader.readLine();
			if (response == null) {
				break;
			}
			System.out.println(response);
		}
		socket.close();
	}
}
