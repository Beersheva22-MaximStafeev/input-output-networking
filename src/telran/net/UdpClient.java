package telran.net;

import java.io.*;
import java.net.*;

import static telran.net.UdpUtils.*;

public class UdpClient implements NetworkClient {
	private String host;
	private int port;
	private DatagramSocket socket;

	public UdpClient(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			socket = new DatagramSocket();
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String requestType, Serializable obj) {
		Request request = new Request(requestType, obj);
		Response response = null;
		try {
			byte[] bufferSend = toBytesArray(request);
			byte[] bufferReceive = new byte[MAX_BUFFER_LENGTH];
			DatagramPacket packetSend = new DatagramPacket(bufferSend, bufferSend.length, InetAddress.getByName(host), port);
			socket.send(packetSend);
			DatagramPacket packetReceive = new DatagramPacket(bufferReceive, MAX_BUFFER_LENGTH);
			socket.receive(packetReceive);
			response = (Response) toSerializable(packetReceive.getData(), packetReceive.getLength());
			if (response.code != ResponseCode.OK) {
				throw new Exception(response.data.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return (T) response.data;
	}

}
