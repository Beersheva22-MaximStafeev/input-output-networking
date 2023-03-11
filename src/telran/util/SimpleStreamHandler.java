package telran.util;

import java.io.PrintStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SimpleStreamHandler implements Handler {

	private PrintStream stream;
	
	@Override
	public void publish(LoggerRecord loggerRecord) {
		stream.println(String.format("[%s][%s][%s] %s", 
				loggerRecord.loggerName,
				loggerRecord.level, 
				loggerRecord.timestamp.atZone(ZoneId.of(loggerRecord.zoneId)).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),  
				loggerRecord.message));
	}

	public SimpleStreamHandler(PrintStream stream) {
		this.stream = stream;
	}
}
