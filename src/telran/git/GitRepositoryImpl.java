package telran.git;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

@SuppressWarnings("unused")
public class GitRepositoryImpl implements GitRepository {
	
	public static final String SAVE_GIT_NOT_SAVED_ERROR = "Git not saved. ERROR: ";
	public static final String SAVE_GIT_OK = "Git saved successfully";
	public static final String SAVE_GIT_NOT_INIT = "Git is not initialazed";
	public static final String INIT_ERROR_IN_PATH = "ERORR initialazing git in this path. Path is not exists or nor writable or not a directory";
	public static final String IGNORED_FILENAME_REMOVE_NOT_EXISTS = "No such regex";
	public static final String IGNORED_FILENAME_REMOVE_OK = "Regex successfully removed";
	public static final String IGNORED_FILENAME_ADD_OK = "Regex successfully added";
	public static final String IGNORED_FILENAME_ADD_NOT_CORRECT = "It's not a correct regex";
	public static final String IGNORED_FILENAME_ADD_ALREADY_EXISTS = "This regex already exists";
	public static final String DELETE_BRANCH_OK = "Branch successfully removed";
	public static final String DELETE_BRANCH_ACTIVE_BRANCH = "Cannot delete active branch";
	public static final String DELETE_BRANCH_NO_BRANCH = "There is no such branch.";
	public static final String RENAME_BRANCH_OK = "Branch successfully renamed";
	public static final String RENAME_BRANCH_ALREADY_EXISTS = "Cannot rename branch, branch with such name or commit with this id already exists";
	public static final String CREATE_BRANCH_OK = "Branch created. HEAD mooved to new branch";
	public static final String CREATE_BRANCH_ALREADY_EXISTS = "Cannot create branch. Branch with this name or commit with this id already exists";

	public static final String GIT_SAVE_NAME = ".mygit";
	
	private static final long serialVersionUID = 1L;
	
	private static GitRepositoryImpl objectCreated;
	
	private Object head;
	private String homePath;
	private Set<String> ignoredFileNameExp;
	private HashMap<String, Branch> branches;
	private HashMap<String, Commit> commits;


	private GitRepositoryImpl() {
		
	}

	public static GitRepositoryImpl init(String homePath) {
		if (objectCreated != null)
			return objectCreated;
		if (!checkExistingAndWritablePath(homePath))
			throw new RuntimeException(INIT_ERROR_IN_PATH);
		if (gitSaveFileExists(homePath)) {
			restoreGitObject(homePath);
		} else {
			createNewGitObject(homePath);
		}
		return objectCreated;
	}
	
	private static void restoreGitObject(String ) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(getGitStorageFileName(getAbsolutePath()).toString()))) {
			objectCreated = (GitRepositoryImpl) input.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
//	private static String saveGitObject(String ) {
//		if (objectCreated == null) {
//			return SAVE_GIT_NOT_INIT;
//		}
//		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(getGitStorageFileName(getAbsolutePath()).toString()))) {
//			output.writeObject(objectCreated);
//		} catch (Exception e) {
//			return "Git not saved. ERROR: " + e.getMessage();
//		}
//		return SAVE_GIT_OK;
//	}

	private static void createNewGitObject(String homePath) {
		objectCreated = new GitRepositoryImpl();
		objectCreated.head = null;
		objectCreated.homePath = getAbsolutePath(homePath).toString();
		objectCreated.ignoredFileNameExp = new HashSet<>();
		String res = objectCreated.addIgnoredFileNameExp(GIT_SAVE_NAME.replace(".", "\\."));
		objectCreated.branches = new HashMap<>();
		objectCreated.commits = new HashMap<>();
		// TO DO GitRepositoryImpl : createNewGitObject
	}

	private static boolean gitSaveFileExists(String homePath) {
		Path path = getAbsolutePath(homePath);
		Path gitFile = getGitStorageFileName(path);
		return Files.exists(gitFile);
	}

	private static Path getGitStorageFileName(Path path) {
		return path.resolve(GIT_FILE);
	}

	private static Path getAbsolutePath(String homePath) {
		return Path.of(homePath).toAbsolutePath().normalize();
	}
	
	private static boolean checkExistingAndWritablePath(String homePath) {
		Path path = getAbsolutePath(homePath);
		return Files.exists(path) && Files.isDirectory(path) && Files.isWritable(path);
	}

	@Override
	public String commit(String commitMessage) {
		if (commitMessage.length() == 0) {
			return "Commit message cannot be empty";
		}
		if (head == null) {
//			res = makeFirstCommit(commitMessage);
			createBranch("Master");
			Commit commit = new Commit(null, commitMessage, info());
			((Branch) head).setCommit(commit);
			commits.put(commit.getId(), commit);
			// TO DO makeFirstCommit
			return "First commit, master branch created";
		} else {
			if (head instanceof Commit) {
				return "Cannot commit outside a branch";
			}
			Map<FileStates, List<FileState>> differsMap = infoDiff();
			if (differsMap.getOrDefault(FileStates.UNTRACKED, new LinkedList<>()).size() == 0 && differsMap.getOrDefault(FileStates.MODIFIED, new LinkedList<>()).size() == 0) {
				return "Nothing to commit";
			}
			Commit commit = new Commit(getCommitFromHead(), commitMessage, info());
			((Branch) head).setCommit(commit);
			commits.put(commit.getId(), commit);
			return "Commit successfully created";
		}
		// TO DO GitRepositoryImpl : public String commit(String commitMessage)
	}

	@Override
	public List<FileState> info() {
		// TO DO GitRepositoryImpl : public List<FileState> info()
//		List<Path> dirContent = getHomeDirectoryContent();
//		Commit commit = getCommitFromHead();
//		return dirContent.stream()
//				.map(path -> 
//					new FileState(path, 
//							commit == null ? 
//									FileStates.UNTRACKED : 
//									commit.getDifference(path, getFileContent(path))))
//				.toList();
		return infoDiff().entrySet().stream().flatMap(el -> el.getValue().stream()).toList();
	}

	public HashMap<FileStates, List<FileState>> infoDiff() {
		// TO DO GitRepositoryImpl : public List<FileState> info()
		List<Path> dirContent = getHomeDirectoryContent();
		Commit commit = getCommitFromHead();
		return (HashMap<FileStates, List<FileState>>) dirContent.stream()
				.map(path -> 
					new FileState(path, 
							commit == null ? 
									FileStates.UNTRACKED : 
									commit.getDifference(path, getFileContent(path))))
				.collect(Collectors.groupingBy(FileState::getState));
	}

	private List<Path> getHomeDirectoryContent() {
		try {
			return Files.walk(Path.of(homePath))
					.filter(path -> !Files.isDirectory(path))
					.filter(path -> fileNotIgnored(path))
					.toList();
		} catch (IOException e) {
			throw new RuntimeException("Error browsing home directory");
		}
	}

	private boolean fileNotIgnored(Path path) {
		return ignoredFileNameExp.stream().noneMatch(regex -> Pattern.matches(regex, path.getFileName().toString()));
	}

	protected static byte[] getFileContent(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			return new byte[0];
		}
	}

	@Override
	public String createBranch(String branchName) {
		if (branches.containsKey(branchName) || commits.containsKey(branchName)) {
			return CREATE_BRANCH_ALREADY_EXISTS;
		}
		Branch branch = new Branch(branchName, getCommitFromHead());
		head = branch;
		branches.put(branchName, branch);
		return CREATE_BRANCH_OK;
	}

	@Override
	public String renameBranch(String branchName, String newName) {
		if (branches.containsKey(newName) || commits.containsKey(newName)) {
			return RENAME_BRANCH_ALREADY_EXISTS;
		}
		Branch branch = branches.get(branchName);
		branch.setName(newName);
		branches.remove(branchName);
		branches.put(newName, branch);
		return RENAME_BRANCH_OK;
	}

	@Override
	public String deleteBranch(String branchName) {
		Branch branch = branches.get(branchName);
		if (branch == null) {
			return DELETE_BRANCH_NO_BRANCH;
		}
		if (head == branch) {
			return DELETE_BRANCH_ACTIVE_BRANCH;
		}
		branches.remove(branchName);
		return DELETE_BRANCH_OK;
	}

	@Override
	public List<CommitMessage> log() {
		LinkedList<CommitMessage> res = new LinkedList<>();
		if (head != null) {
			Commit current = getCommitFromHead(); 
			while (current != null) {
				res.add(new CommitMessage(current.getId(), current.getMessage()));
				current = current.getPrevious();
			}
		}
		return res;
	}

	private Commit getCommitFromHead() {
		return (head instanceof Branch) ? ((Branch) head).getCommit() :(Commit) head;
	}

	@Override
	public List<String> branches() {
		return branches.values().stream()
				.map(el -> head == el ? el.getName() + " *" : el.getName())
				.toList();
	}

	@Override
	public List<Path> commitContent(String commitName) {
		Commit commit = commits.get(commitName);
		if (commit != null) {
			return commit.getContent().keySet().stream().toList();
		} else {
			return new LinkedList<>();
		}
		// TO DO GitRepositoryImpl : List<Path> commitContent(String commitName)
	}

	@Override
	public String switchTo(String name) {
		HashMap<FileStates, List<FileState>> diff = infoDiff();
		if (diff.getOrDefault(FileStates.MODIFIED, new LinkedList<>()).size() != 0 || diff.getOrDefault(FileStates.UNTRACKED, new LinkedList<>()).size() != 0) {
			return "Switch can be made only after commit";
		}
		if (branches.containsKey(name)) {
			head = branches.get(name);
		} else if (commits.containsKey(name)) {
			head = commits.get(name);
		} else {
			return "No such branch or commit";
		}
		diff = infoDiff();
		diff.get(FileStates.UNTRACKED).forEach(el -> {
			try {
				Files.delete(el.getName());
			} catch (IOException e) {
				throw new RuntimeException("Cannot delete file");
			}
		});
		diff.get(FileStates.MODIFIED).forEach(el -> {
			try {
				Files.write(el.getName(), getCommitFromHead().getContent().get(el.getName()));
			} catch (IOException e) {
				throw new RuntimeException("Cannot rewrite file");
			}
		});
		getCommitFromHead().getContent().keySet().forEach(path -> createIfAbsent(path));
		// TO DO GitRepositoryImpl : public String switchTo(String name)
		return "Switching finished successfully";
	}

	private Object createIfAbsent(Path path) {
		if (!Files.exists(path)) {
			try {
				Files.write(path, getCommitFromHead().getContent().get(path));
			} catch (IOException e) {
				throw new RuntimeException("Cannot write file");
			}
		}
		return null;
	}

	@Override
	public String getHead() {
		if (head == null)
			return null;
		return head instanceof Branch ? ((Branch) head).getName() : ((Commit) head).getId();
	}

	@Override
	public void save() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(getGitStorageFileName(Path.of(homePath)).toString()))) {
			output.writeObject(this);
		} catch (Exception e) {
			throw new RuntimeException(SAVE_GIT_NOT_SAVED_ERROR + e.getMessage());
		}

	}

	@Override
	public String addIgnoredFileNameExp(String regex) {
		if (ignoredFileNameExp.contains(regex)) {
			return IGNORED_FILENAME_ADD_ALREADY_EXISTS;
		}
		try {
			Pattern.matches(regex, "tmpstring");
		} catch (Exception e) {
			return IGNORED_FILENAME_ADD_NOT_CORRECT;
		}
		ignoredFileNameExp.add(regex);
		return IGNORED_FILENAME_ADD_OK;
	}

	@Override
	public String removeIgnoredFileNameExp(String regex) {
		return ignoredFileNameExp.remove(regex) ? IGNORED_FILENAME_REMOVE_OK : IGNORED_FILENAME_REMOVE_NOT_EXISTS;
	}

	@Override
	public List<String> getAllIgnoredFileNameExp() {
		return ignoredFileNameExp.stream().toList();
	}

}
