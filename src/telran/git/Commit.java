package telran.git;

import java.io.Serializable;
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
	private final HashMap<Path, byte[]> content;
	
	public Commit(Commit previous, String message, List<FileState> content) {
		this.id = getUniqueString();
		this.previous = previous;
		this.message = message;
		// TO DO Commit : Commit
		this.content = new HashMap<>();
		content.forEach(el -> this.content.put(el.getName(), getByteContent(el.state, previous, el.name)));
	}
	
	private byte[] getByteContent(FileStates state, Commit previous, Path name) {
		if (previous == null) {
			return GitRepositoryImpl.getFileContent(name);
		}
		if (state != FileStates.STAGED) {
			return GitRepositoryImpl.getFileContent(name);
		} else {
			return previous.getContent().get(name);
		}
		// TO DO Auto-generated method stub
//		return null;
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
	
	public HashMap<Path, byte[]> getContent() {
		return content;
	}

	public FileStates getDifference(Path path, byte[] fileContent) {
		if (content.containsKey(path)) {
			return content.get(path).equals(fileContent) ? FileStates.STAGED : FileStates.MODIFIED;
		} else {
			return FileStates.UNTRACKED;
		}
	}

}
