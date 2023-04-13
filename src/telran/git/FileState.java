package telran.git;

import java.nio.file.Path;

/**
 * 1.3.	public List<FileState> info()  returns list of objects of the class FileState  
 * It creates object of the class FileState for each file in the working directory except 
 * those that have names matching a regular expression of the ignored files. 
 * Think of the structure, like Path and enum Status
 * @author Stafeev
 *
 */
public class FileState {
	Path name;
	FileStates state;
	
	public FileState(Path name, FileStates state) {
		this.name = name;
		this.state = state;
	}

	public Path getName() {
		return name;
	}

	public FileStates getState() {
		return state;
	}
	
	
}
