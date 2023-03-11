package telran.io.test;

import org.junit.jupiter.api.*;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class LineOrientedStreams {
	private static final int N_RECORDS = 100_000_000;
	static final String fileNamePrintSream = "lines-stream.txt";
	static final String fileNamePrintWriter = "lines-writer.txt";
	static final String line = "Hello World";
	static final String helloFileName = "test.txt";

	@Test
	@Timeout(value = 1, unit = TimeUnit.SECONDS) 
	@Disabled
	void printStreamTest() throws Exception {
		@SuppressWarnings("resource")
		PrintStream printStream = new PrintStream(fileNamePrintSream);
//		printStream.println(line);
		IntStream.range(0, N_RECORDS).forEach(printStream::println);
	}

	@Test
	@Disabled
	void printWriterTest() throws Exception {
//		PrintWriter printWriter = new PrintWriter(fileNamePrintWriter);
//		printWriter.println(line);
//		здесь ничего не сохранится, потому что PrintWriter пишет через буфер и скидывает на диск только в случае переполнения 
//			буфера, закрытия стрима или спец метода флаш
//		ничего из этого здесь не было

//		а вот следующий код закрывает стрим, поэтому в файле что-то будет
		try (PrintWriter printWriter = new PrintWriter(fileNamePrintWriter)) {
//			printWriter.println(line);
			IntStream.range(0, N_RECORDS).forEach(printWriter::println);
		}

	}
	
	@Test
	void BufferedReaderTest() throws Exception {
		try (BufferedReader reader = new BufferedReader(new FileReader(helloFileName))) {
		while (true) {
			String nextLine = reader.readLine();
			if (nextLine == null) {
				break;
			}
			assertEquals(line, nextLine);
		}
//		reader.lines().parallel().forEach(l -> assertEquals(line, l));
		}
	}

}