package telran.git;

/**
 * 1.7.	public List<CommitMessage> log()  returns list of objects containing a pair of commit name  and commit message 
 * from the HEAD to a first commit
 * @author Stafeev
 *
 */
public class CommitMessage {
	// TO DO
	private String name;
	private String message;
	
	public CommitMessage(String name, String message) {
		this.name = name;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	
}
