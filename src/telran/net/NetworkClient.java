package telran.net;

import java.io.Closeable;
import java.io.Serializable;

public interface NetworkClient extends Closeable {
	<T> T send(String requestType, Serializable obj);
}
