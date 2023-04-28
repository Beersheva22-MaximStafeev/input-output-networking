package telran.net;

import java.io.*;
import java.net.*;
//import java.time.temporal.ChronoUnit;
//import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;

public class TcpServer implements Runnable {

	private static final int TIMEOUT = 5000;
	private Protocol protocol;
	private int port;
	private ServerSocket serverSocket;
	private ThreadPoolExecutor executor;
	private boolean alive = true;
	
	public TcpServer(Protocol protocol, int port) throws IOException {
		// TO DO Auto-generated constructor stub
		this.protocol = protocol;
		this.port = port;
		serverSocket = new ServerSocket(port);
		int nThreads = Runtime.getRuntime().availableProcessors();
		nThreads = 2;
		System.out.println("Number threads in thread pool is: " + nThreads);
		// это прямо максимальное количество одновременно работающих клиентов,
		// каждый следующий будет приходить - его просто поставят в очередь и он будет ждать
		// когда предыдущие доработают и освободят ресурсы
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
//		(ThreadPoolExecutor) executor
		
	}
	
	@Override
	public void run() {
		// TO DO Auto-generated method stub
		System.out.printf("TcpServer %s listening on port %s\n", this, port);
		try {
			serverSocket.setSoTimeout(TIMEOUT);
		} catch (SocketException e) {
			// TO DO Auto-generated catch block
			e.printStackTrace();
		}
		while (isAlive()) {
			try {
				try {
					Socket socket = serverSocket.accept();
					System.out.println("Client connected");
					TcpServerClient serverClient = new TcpServerClient(socket, protocol, this);
					// without threads
//					serverClient.run();
				
					// with many threads
//					Thread thread = new Thread(serverClient);
//					thread.start();
				
					// with limited number of threads
					executor.execute(serverClient);
				} catch (SocketTimeoutException e) {
					if (!isAlive()) System.out.printf("TcsServer %s shutting down\n", this);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				System.out.println("Client disconnected");
			}
		}
		System.out.printf("TcpServer %s finished\n", this);
	}
	
	public void shutdown() {
		alive = false;
		try {
			executor.shutdown();
//			executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean isAlive() {
		return alive;
	}
	
	ThreadPoolExecutor getExecutor() {
		return executor;
	}

}
