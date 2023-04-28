package telran.net;

import java.io.*;
import java.net.*;

public class TcpServerClient implements Runnable {
	private Socket socket;
	private Protocol protocol;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	TcpServer tcpServer;
	private static final int TIMEOUT = 200;
	private static final long MAX_RUN_TIME_MILLS = TIMEOUT * 2;
	
	public TcpServerClient(Socket socket, Protocol protocol, TcpServer tcpServer) throws IOException {
		this.socket = socket;
		this.protocol = protocol;
		this.tcpServer = tcpServer;
		this.socket.setSoTimeout(TIMEOUT);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {
		long timeStart = System.currentTimeMillis();
		System.out.printf("TcpServerClient %s started\n", this);
		boolean running = true;
		while (tcpServer.isAlive() && running) {
			Request request;
			try {
				request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.reset();
				output.writeObject(response);
			} catch (SocketTimeoutException e) {
				if (!tcpServer.isAlive()) {
					System.out.println("TcpServerClient: tcpServer is not alive");
					break;
				}
				if (System.currentTimeMillis() - timeStart > MAX_RUN_TIME_MILLS) {
					if (tcpServer.getExecutor().getQueue().size() > 0) {
						System.out.printf("getPoolSize: %s, getMaximumPoolSize: %s, queue: %s and this TcpServerClient %s stopped or not\n", 
								tcpServer.getExecutor().getPoolSize(),
								tcpServer.getExecutor().getMaximumPoolSize(),
								tcpServer.getExecutor().getQueue().size(), 
								this);
						running = false;
					}
				}
			} catch (EOFException e) {
				System.out.println("Client closed connection");
				break;
			} catch (Exception e) {
				throw new RuntimeException(e.toString());
			}
		}
		System.out.printf("TcpServerClient %s finished \n", this);
	}

}
