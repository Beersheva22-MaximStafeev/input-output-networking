package telran.util;

import java.time.ZonedDateTime;

public class Logger {
	private Level level;
	private Handler handler;
	private String name;
	
	public Logger(Handler handler, String name) {
		this.handler = handler;
		this.name = name;
		this.level = Level.INFO;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	private void postMessage(Level level, String message) {
		if (level.compareTo(this.level) >= 0) {
			ZonedDateTime timestamp = ZonedDateTime.now();
			handler.publish(new LoggerRecord(
					timestamp.toInstant(), 
					timestamp.getZone().toString(), 
					level, 
					name, 
					message
			));
		}
	}
	
	public void error(String message) {
		postMessage(Level.ERROR, message);
	}
	
	public void warn(String message) {
		postMessage(Level.WARN, message);
	}
	
	public void info(String message) {
		postMessage(Level.INFO, message);
	}
	
	public void debug(String message) {
		postMessage(Level.DEBUG, message);
	}
	
	public void trace(String message) {
		postMessage(Level.TRACE, message);
	}
}
