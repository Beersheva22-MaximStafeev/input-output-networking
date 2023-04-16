package telran.git;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Commit implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int LENGTH_SHORT_ID = 12;
	private final String id;
	private final Commit previous;
	private final String message;
	private final HashMap<String, File> content;
	
	public Commit(Commit previous, String message, List<FileState> content) {
		this.id = getUniqueString();
		this.previous = previous;
		this.message = message;
		this.content = new HashMap<>();
		content.forEach(el -> {
			if (el.getState() != FileStates.DELETED)
				this.content.put(el.getName().toString(), getFile(el, previous));
		});
	}
	

	private File getFile(FileState el, Commit previous) {
		return new File(
				el.getName().toString(),
				File.getLastMofifiyed(el.getName()),
				getByteContent(el.getState(), previous, el.getName()));
	}



	private byte[] getByteContent(FileStates state, Commit previous, Path name) {
		if (previous == null) {
			return getFileContent(name);
		}
		if (state != FileStates.STAGED) {
			return getFileContent(name);
		} else {
			return previous.getContent().get(name.toString()).getContent();
		}
	}

	private String getUniqueString() {
		// FIX ME Commit : private String getUniqueString()
		// Should check, that it is really unique in commits id and branches names 
		return UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
	
	public String getShortId() {
		return id.substring(id.length() - LENGTH_SHORT_ID, id.length());
	}
	
	public Commit getPrevious() {
		return previous;
	}
	
	public String getMessage() {
		return message;
	}
	
	public HashMap<String, File> getContent() {
		return content;
	}

	public FileStates getDifference(Path path) {
		if (content.containsKey(path.toString())) {
			return content.get(path.toString()).getFileModifiyed().equals(File.getLastMofifiyed(path)) ? 
					FileStates.STAGED : FileStates.MODIFIED;
		} else {
			return FileStates.UNTRACKED;
		}
	}
	
	protected static byte[] getFileContent(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			return new byte[0];
		}
	}

}
