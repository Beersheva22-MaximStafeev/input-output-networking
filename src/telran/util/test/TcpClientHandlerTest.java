package telran.util.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import telran.util.*;

public class TcpClientHandlerTest {
	
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 4000;
	private static Logger LOG;
	private static TcpClientHandler handler;
	
	@BeforeAll
	static void startUp() throws Exception {
		handler = new TcpClientHandler(HOSTNAME, PORT);
		LOG = new Logger(handler, "server-log");
	}
	
	@Test
	void publishTest() {
		LOG.error("ERROR1");
		LOG.info("INFO1");
		LOG.debug("DEBUG1");
		LOG.trace("TRACE1");
		LOG.warn("WARN1");
	}
	
	@Test
	void counterTest() {
		checkCounters(new int[]{0, 0, 0, 0, 0});	
		LOG.error("error message");
		checkCounters(new int[]{0, 0, 0, 0, 1});
		LOG.trace("trace something");
		checkCounters(new int[]{0, 0, 0, 0, 1});
		for (int i = 0; i < 500; i++) {
			LOG.warn("WARN MESSAGE");
		}
		checkCounters(new int[]{0, 0, 0, 500, 1});
	}

	private void checkCounters(int[] counters) {
		for (Level level: Level.values()) {
			assertEquals(counters[level.ordinal()],  handler.getCounter(level));
		}
	}
}
