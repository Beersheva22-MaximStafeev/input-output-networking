package telran.io.test;

import telran.io.*;

import org.junit.jupiter.api.Test;

public class CopyTest {
	String srcFile1_41Gb = "src_1_46Gb.ext"; 
	String dstFile1_41Gb = "dst_1_46Gb.ext";
//	"FilesCopy", "TransferCopy", "BufferCopy"
	
	@Test
	void copyTest() throws Exception {
		new FilesCopyBuilder().build("FilesCopy", new String[] {srcFile1_41Gb, dstFile1_41Gb, "true"}).copyRun();
		new FilesCopyBuilder().build("TransferCopy", new String[] {srcFile1_41Gb, dstFile1_41Gb, "true"}).copyRun();
		new FilesCopyBuilder().build("BufferCopy", new String[] {srcFile1_41Gb, dstFile1_41Gb, "true"}).copyRun();
		new FilesCopyBuilder().build("BufferCopy", new String[] {srcFile1_41Gb, dstFile1_41Gb, "true", "1000"}).copyRun();
		new FilesCopyBuilder().build("BufferCopy", new String[] {srcFile1_41Gb, dstFile1_41Gb, "true", ((Long) Runtime.getRuntime().freeMemory()).toString()}).copyRun();
	}
}
