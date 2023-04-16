package telran.git;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

public class File implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Instant fileModifiyed;
	private byte[] content;
	
	public String getName() {
		return name;
	}
	
	public Instant getFileModifiyed() {
		return fileModifiyed;
	}
	
	public byte[] getContent() {
		return content;
	}

	public File(String name, Instant fileModifiyed, byte[] content) {
		this.name = name;
		this.fileModifiyed = fileModifiyed;
		this.content = content;
	}
	
	public void rewrite() throws IOException {
		Files.write(Path.of(getName()), getContent());
		Files.setLastModifiedTime(Path.of(getName()), FileTime.from(getFileModifiyed()));	
	}

	public static Instant getLastMofifiyed(Path file) {
		try {
			return Files.getLastModifiedTime(file).toInstant();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
