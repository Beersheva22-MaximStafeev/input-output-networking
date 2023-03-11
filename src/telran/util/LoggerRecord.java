package telran.util;

import java.time.Instant;

public class LoggerRecord {
	final Instant timestamp;
	final String zoneId;
	final Level level;
	final String loggerName;
	final String message;
	
	public LoggerRecord(Instant timestamp, String zoneId, Level level, String loggerName, String message) {
		this.timestamp = timestamp;
		this.zoneId = zoneId;
		this.level = level;
		this.loggerName = loggerName;
		this.message = message;
	}
}
