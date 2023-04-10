package telran.git;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

public interface GitRepository extends Serializable {
	String GIT_FILE = ".mygit";

	/*
	 * The file .mygit in the working (current) directory contains a snapshot of an object of 
	 * the class GitRepositoryImpl. 
	 * In the case of the file existence the method should return an object restored from the file, 
	 * otherwise it returns the empty object
	 */
	// public static GitRepositoryImpl init();
	/**
	 * 1.2.	public String commit(String commitMessage) adds commit. 
	 * If reference to HEAD (consider the proper field as either name of commit or name of branch) 
	 * contains name of commit it should return message saying about impossibility to do commit with no branch; 
	 * if head equals null it should create first branch “master”. 
	 * If working directory doesn’t have files in the either UNTRACKED or MODIFIED state it should 
	 * return message saying about nothing to commit. 
	 * Other requirements should follow your understanding of “commit”. 
	 * @param commitMessage
	 * @return
	 */
	String commit(String commitMessage);
	/**
	 * 1.3.	public List<FileState> info()  returns list of objects of the class FileState  
	 * It creates object of the class FileState for each file in the working directory except 
	 * those that have names matching a regular expression of the ignored files. 
	 * Think of the structure, like Path and enum Status
	 * @return
	 */
	List<FileState> info();
	/**
	 * 1.4.	public String createBranch(String branchName) creates branch on the current commsit and sets HEAD with that name. 
	 * Returns a proper message saying about a result of creating
	 * @param branchName
	 * @return
	 */
	String createBranch(String branchName);
	/**
	 * 1.5.	public String renameBranch(String branchName, String newName) renames the specified branch with new name. 
	 * In the case HEAD refers to being renamed branch the HEAD will be updated as well. Return a proper message
	 * @param branchName
	 * @param newName
	 * @return
	 */
	String renameBranch(String branchName, String newName);
	/**
	 * 1.6.	public String deleteBranch(String branchName)  deletes existing branch. 
	 * Current branch (referred by the HEAD) cannot be deleted. Returns a proper message
	 * @param branchName
	 * @return
	 */
	String deleteBranch(String branchName);
	/**
	 * 1.7.	public List<CommitMessage> log()  returns list of objects containing a pair of commit name  and commit message 
	 * from the HEAD to a first commit
	 * @return
	 */
	List<CommitMessage> log();
	/**
	 * 1.8.	public List<String> branches() returns list of branch names. 
	 * For current branch the asterisk follows the name
	 * @return
	 */
	List<String> branches(); //list of branch names
	/**
	 * 1.9.	public List<Path> commitContent(String commitName) 
	 * Returns list of the Path objects of files included in a specified commit
	 * @param commitName
	 * @return
	 */
	List<Path> commitContent(String commitName);
	/**
	 * 1.13.	Write method switchTo that takes either commit name or branch name and returns message 
	 * saying about a result of the switching. 
	 * 1.14.	Working directory contains the files in the state of either UNTRACKED or MODIFIED
	 * 1.14.1.	The method should return message like “switchTo may be done only after commit”
	 * 1.15.	The method takes a name equaled to the head
	 * 1.15.1.	The method should return a message like “switching to the current commit doesn’t make a sense
	 * 1.16.	Neither 1.1 nor 1.2
	 * 1.16.1.	Working directory should have exactly files with their content according to the commit or branch
	 * 1.16.2.	Head should contain the being switched either commit or branch
	 * @param name
	 * @return
	 */
	String switchTo(String name); //name is either a commit name or a branch name
	/**
	 * 1.10.	public String getHead() Returns the name specified in the field “head” or null
	 * @param name
	 * @return
	 */
	String getHead(); //return null if head refers commit with no branch
	/**
	 * 1.11.	public void save() Saves “this” to file “.mygit” of the working directory
	 */
	void save(); //saving to .mygit serialization to file (Object Stream)
	/**
	 * 1.12.	public String addIgnoredFileNameExp(String regex) adds regular expression for being ignored file names. 
	 * If regex is incorrect returns a proper message. 
	 * How to check regex correctness (“vasya”.matches(regex) throws exception in the case of incorrect regex
	 * @param regex
	 * @return
	 */
	String addIgnoredFileNameExp(String regex);
	String removeIgnoredFileNameExp(String regex);
	String[] getAllIgnoredFileNameExp();
}
