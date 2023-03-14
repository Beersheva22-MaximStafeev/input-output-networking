package telran.util.test;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import telran.util.Level;
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
		for (Level level: Level.values()) {
			System.out.println("This level: " + level);
			LOG.setLevel(level);
			LOG.trace("Trace test");
			LOG.debug("Debug test");
			LOG.info("Info test");
			LOG.warn("Warn test");
			LOG.error("Error test");
		}
	}
}