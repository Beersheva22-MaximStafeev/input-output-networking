package telran.io.test;

import org.junit.jupiter.api.Test;
import java.io.*;

public class LineOrientedStreams {
	static final String fileNamePrintSream = "lines-stream.txt";
	static final String fileNamePrintWriter = "lines-writer.txt";
	static final String line = "Hello World !!!";

	@Test
	void printStreamTest() throws Exception {
		@SuppressWarnings("resource")
		PrintStream printStream = new PrintStream(fileNamePrintSream);
		printStream.println(line);
	}

	@Test
	void printWriterTest() throws Exception {
//		PrintWriter printWriter = new PrintWriter(fileNamePrintWriter);
//		printWriter.println(line);
//		здесь ничего не сохранится, потому что PrintWriter пишет через буфер и скидывает на диск только в случае переполнения 
//			буфера, закрытия стрима или спец метода флаш
//		ничего из этого здесь не было

//		а вот следующий код закрывает стрим, поэтому в файле что-то будет
		try (PrintWriter printWriter = new PrintWriter(fileNamePrintWriter)) {
			printWriter.println(line);
		}

	}

}