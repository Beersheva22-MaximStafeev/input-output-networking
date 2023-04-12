package telran.git;

import java.io.Serializable;

public class Branch implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Commit commit;
	
	public Branch(String name, Commit commit) {
		this.name = name;
		this.commit = commit;
	}
	
	public Commit getCommit() {
		return commit;
	}
	
	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
