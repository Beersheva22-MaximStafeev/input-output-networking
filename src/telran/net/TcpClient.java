package telran.net;

import java.io.*;
import java.net.*;


public class TcpClient implements NetworkClient {
	
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public TcpClient(String hostname, int port) throws Exception {
		// TODO Auto-generated constructor stub
		socket = new Socket(hostname, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String requestType, Serializable obj) {
		T result = null;
		Request request = new Request(requestType, obj);
		Response response = null;
		try {
			output.writeObject(request);
			response = (Response) input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		if (response.code == ResponseCode.OK) {
			result = (T) response.data;
		} else {
			throw new RuntimeException("Error with request. Response code: " + response.code);
		}
		return result;
	}
	
	@Override
	public void close() throws IOException {
		output.flush();
		output.close();
		input.close();
		socket.close();
	}

}
