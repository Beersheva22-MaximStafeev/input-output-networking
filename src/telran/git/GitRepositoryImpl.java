package telran.git;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GitRepositoryImpl implements GitRepository {
	
	public static final String SWITCH_OK = "Switching finished successfully";
	public static final String SWITCH_ERROR_CANNOT_REWRITE = "Cannot rewrite file";
	public static final String SWITCH_ERROR_CANNOT_DELETE = "Cannot delete file";
	public static final String SWITCH_NO_BRANCH = "No such branch or commit";
	public static final String SWITCH_NOT_COMMITED = "Switch can be made only after commit";
	public static final String GET_HOMEDIRECTORY_ERROR = "Error browsing home directory";
	public static final String COMMIT_OK = "Commit successfully created";
	public static final String COMMIT_NOTHING = "Nothing to commit";
	public static final String COMMIT_OUTSIDE = "Cannot commit outside a branch";
	public static final String COMMIT_FIRST = "First commit, master branch created";
	public static final String COMMIT_EMPTY = "Commit message cannot be empty";
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
	
	private static void restoreGitObject(String homePath) {
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(getGitStorageFileName(getAbsolutePath(homePath)).toString()))) {
			objectCreated = (GitRepositoryImpl) input.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private static void createNewGitObject(String homePath) {
		objectCreated = new GitRepositoryImpl();
		objectCreated.head = null;
		objectCreated.homePath = getAbsolutePath(homePath).toString();
		objectCreated.ignoredFileNameExp = new HashSet<>();
		objectCreated.addIgnoredFileNameExp(GIT_SAVE_NAME.replace(".", "\\."));
		objectCreated.branches = new HashMap<>();
		objectCreated.commits = new HashMap<>();
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
			return COMMIT_EMPTY;
		}
		if (head == null) {
			createBranch("Master");
			Commit commit = new Commit(null, commitMessage, info());
			((Branch) head).setCommit(commit);
			commits.put(commit.getId(), commit);
			save();
			return COMMIT_FIRST;
		} else {
			if (head instanceof Commit) {
				return COMMIT_OUTSIDE;
			}
			Map<FileStates, List<FileState>> differsMap = infoDiff();
			if (differsMap.getOrDefault(FileStates.UNTRACKED, new LinkedList<>()).size() == 0 && 
					differsMap.getOrDefault(FileStates.MODIFIED, new LinkedList<>()).size() == 0 &&
					differsMap.getOrDefault(FileStates.DELETED, new LinkedList<>()).size() == 0) {
				return COMMIT_NOTHING;
			}
			Commit commit = new Commit(getCommitFromHead(), commitMessage, info());
			((Branch) head).setCommit(commit);
			commits.put(commit.getId(), commit);
			save();
			return COMMIT_OK;
		}
	}

	@Override
	public List<FileState> info() {
		return infoDiff().entrySet().stream().flatMap(el -> el.getValue().stream()).toList();
	}

	private Map<FileStates, List<FileState>> infoDiff() {
		List<Path> dirContent = getHomeDirectoryContent();
		Commit commit = getCommitFromHead();
		Map<FileStates, List<FileState>> res = dirContent.stream()
				.map(path -> 
					new FileState(path, 
							commit == null ? 
									FileStates.UNTRACKED : 
									commit.getDifference(path, getFileContent(path))))
				.collect(Collectors.groupingBy(FileState::getState));
		commit.getContent().forEach((k, v) -> {
					if (!Files.exists(Path.of(k))) {
						res.computeIfAbsent(FileStates.DELETED, (kk) -> new LinkedList<>()).add(new FileState(Path.of(k), FileStates.DELETED));
					}
				});
		return res;
	}

	private List<Path> getHomeDirectoryContent() {
		try {
			return Files.walk(Path.of(homePath))
					.filter(path -> !Files.isDirectory(path))
					.filter(path -> fileNotIgnored(path))
					.toList();
		} catch (IOException e) {
			throw new RuntimeException(GET_HOMEDIRECTORY_ERROR);
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
		save();
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
		save();
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
		save();
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
			return commit.getContent().keySet().stream().map(el -> Path.of(el)).toList();
		} else {
			return new LinkedList<>();
		}
	}

	@Override
	public String switchTo(String name) {
		Map<FileStates, List<FileState>> diff = infoDiff();
		if (diff.getOrDefault(FileStates.MODIFIED, new LinkedList<>()).size() != 0 || 
				diff.getOrDefault(FileStates.UNTRACKED, new LinkedList<>()).size() != 0 ||
				diff.getOrDefault(FileStates.DELETED, new LinkedList<>()).size() != 0) {
			return SWITCH_NOT_COMMITED;
		}
		if (branches.containsKey(name)) {
			head = branches.get(name);
		} else if (commits.containsKey(name)) {
			head = commits.get(name);
		} else {
			return SWITCH_NO_BRANCH;
		}
		diff = infoDiff();
		diff.getOrDefault(FileStates.UNTRACKED, new LinkedList<>()).forEach(el -> {
			try {
				Files.delete(el.getName());
			} catch (IOException e) {
				throw new RuntimeException(SWITCH_ERROR_CANNOT_DELETE);
			}
		});
		diff.getOrDefault(FileStates.MODIFIED, new LinkedList<>()).forEach(el -> {
			try {
				Files.write(el.getName(), getCommitFromHead().getContent().get(el.getName().toString()));
			} catch (IOException e) {
				throw new RuntimeException(SWITCH_ERROR_CANNOT_REWRITE);
			}
		});
		diff.getOrDefault(FileStates.DELETED, new LinkedList<>()).forEach(el -> {
			try {
				Files.createDirectories(el.getName().getParent());
				Files.write(el.getName(), getCommitFromHead().getContent().get(el.getName().toString()));
			} catch (IOException e) {
				throw new RuntimeException(SWITCH_ERROR_CANNOT_REWRITE);
			}
		});
		save();
		return SWITCH_OK;
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
		save();
		return IGNORED_FILENAME_ADD_OK;
	}

	@Override
	public String removeIgnoredFileNameExp(String regex) {
		if (!ignoredFileNameExp.remove(regex)) {
			return IGNORED_FILENAME_REMOVE_NOT_EXISTS;
		}
		save();
		return IGNORED_FILENAME_REMOVE_OK;
	}

	@Override
	public List<String> getAllIgnoredFileNameExp() {
		return ignoredFileNameExp.stream().toList();
	}

}
