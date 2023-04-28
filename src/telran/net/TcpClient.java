package telran.net;

import java.io.*;
import java.net.*;


public class TcpClient implements NetworkClient {
	
	private static final int TIMEOUT = 200;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String hostname;
	private int port;

	public TcpClient(String hostname, int port) {
		// TO DO Auto-generated constructor stub
		this.hostname = hostname;
		this.port = port;
		getConnection();
	}
	
	private void getConnection() {
		try {
			socket = new Socket(hostname, port);
			socket.setSoTimeout(TIMEOUT);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String requestType, Serializable obj) {
		try {
			Request request = new Request(requestType, obj);
			Response response = null;
			output.writeObject(request);
			response = (Response) input.readObject();
			if (response.code != ResponseCode.OK) {
				throw new RuntimeException("Error with request. Response code: " + response.code + ", response data: " + response.data);
			}
			return (T) response.data;
		} catch (SocketTimeoutException e) {
			getConnection();
			return send(requestType, obj);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void close() throws IOException {
		output.flush();
		output.close();
		input.close();
		socket.close();
	}

}
