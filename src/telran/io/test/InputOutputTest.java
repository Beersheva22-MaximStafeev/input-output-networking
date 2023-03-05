package telran.io.test;

import org.junit.jupiter.api.*;

import static org.junit.Assert.assertTrue;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;

public class InputOutputTest {
	String fileName = "myFile";
	String directoryName = "myDir1/myDir2";
	
	@BeforeEach
	void setUp() throws Exception {
		new File(fileName).delete();
		new File(directoryName).delete();
	}

	@Test
	@Disabled
	void testFile() throws Exception {
		File f1 = new File(fileName);
		assertTrue(f1.createNewFile());
		File dir1File = new File(directoryName);
		assertTrue(dir1File.mkdirs());
		System.out.println(dir1File.getAbsolutePath());
		System.out.println(new File(".").getCanonicalPath());
		System.out.println(new File(".").getCanonicalFile().getName());
	}
	
	@Test
	@Disabled
	void testFiles() {
		Path path = Path.of("./../.");
		System.out.println(path);
		System.out.println(path.toAbsolutePath());
		System.out.println(path.toAbsolutePath().getNameCount());
		try {
			System.out.println(path.toFile().getCanonicalFile().getName());
		} catch (Exception e) {
			
		}
		System.out.println(path.getFileName());
		System.out.println(path.toAbsolutePath().normalize().getFileName());
		System.out.println(path.toAbsolutePath().normalize().getRoot());
		System.out.println(path.getRoot());
	}
	
	@Test
	void printDirectoryFileTest() throws IOException {
		printDirectoryFile("./../.", 2);
	}
	
	@Test
	void printDirectoryFilesTest() throws IOException {
		printDirectoryFiles("./../.", 2);
	}
	
	/**
	 * 
	 * @param path - directory path
	 * @param maxLevel - maximal level of printing, 1 - current level, if < 1 - print all levels
	 * output format:
	 *  <directory name (no points, no full absolute path
	 *    <node name> - dir | file
	 *      <node name> ....
	 *    <node name>    
	 * @throws IOException 
	 */
	void printDirectoryFile(String path, int maxLevel) throws IOException {
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			System.out.printf("Path <%s> is not exists or it points not to a directory", path);
		} else {
			printDirectoryFile(dir, maxLevel, 0);
		}
	}

	private void printDirectoryFile(File dir, int maxLevel, int level) throws IOException {
		String name = Optional.of(dir.getCanonicalFile().getName())
				.filter(el -> !el.equals("")).orElse("root");
		String attributes = dir.isDirectory() ? "dir" : "file";
		printOneNode(name, attributes, level);
		if ((maxLevel < 1 || level < maxLevel) && dir.isDirectory()) {
			String[] dirList = dir.list();
			if (dirList != null) {
				for (String node: dirList) {
					printDirectoryFile(new File(dir.getAbsolutePath() + File.separator + node), maxLevel, level + 1);
				}
			}
		}
	}

	private void printOneNode(String name, String attributes, int level) {
		System.out.printf("%s%s - %s%n","  ".repeat(level), name, attributes);
//		System.out.printf("%s%s : %s%n","  ".repeat(level), attributes.substring(0, 1).toUpperCase(), name);
	}
	
	/**
	 * написать точно тоже самое, только используя класс Files
	 * @throws IOException 
	 */
	void printDirectoryFiles(String path, int maxLevel) throws IOException {
		Path dir = Path.of(path);
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			System.out.printf("Path <%s> is not exists or it points not to a directory", path);
		} else {
			Files.walk(dir, maxLevel < 1 ? Integer.MAX_VALUE : maxLevel)
				.forEach(el -> this.printDir(dir, el));
		}
	}

	private void printDir(Path start, Path el) {
		Path path = el.toAbsolutePath().normalize().getFileName();
		String name = path == null ? 
				el.toAbsolutePath().normalize().getRoot().toString() : path.toString();
		String attributes = Files.isDirectory(el) ? "dir" : "file";
		int level = start.equals(el) ? 0 : start.relativize(el).getNameCount(); 
		printOneNode(name, attributes, level);
	}

	
}
