package telran.util.test;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import telran.util.Logger;
import telran.util.SimpleStreamHandler;

public class LoggerTest {
	private static final Logger LOG = new Logger(new SimpleStreamHandler(System.out), "telran.util.test");
	
	@Test
	void loggerTest() {	
		LOG.debug("this is a debug message");
		try {
			int a = 10 / 0;
		} catch (Exception e) {
			LOG.error("some big trouble happends: " + e.getMessage() + ", " + Arrays.deepToString(e.getStackTrace()));
		}
	}
}