package telran.git;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class GitRepositoryAppl {
	
	private static GitRepositoryImpl git; 
	
	public static void main(String[] args) {
		git = GitRepositoryImpl.init("C:\\TelRan\\myGitRepa");
		InputOutput io = new StandardInputOutput();
		createMenu().perform(io);		
	}

	private static Menu createMenu() {
		return new Menu("GIT",
				new Menu("Branches",
						Item.of("New branch", GitRepositoryAppl::newBranch),
						Item.of("Delete branch", GitRepositoryAppl::deleteBranch),
						Item.of("Rename branch", GitRepositoryAppl::renameBranch),
						Item.of("Print branches", GitRepositoryAppl::printBranch),
						Item.exit()),
				new Menu("IgnoredFileNameExp",
						Item.of("Add regex", GitRepositoryAppl::addIgnoredFileNameExp),
						Item.of("Remove regex", GitRepositoryAppl::removeIgnoredFileNameExp),
						Item.of("Show all", GitRepositoryAppl::getAllIgnoredFileNameExp),
						Item.exit()
						),
				new Menu("Commits",
						Item.of("info", GitRepositoryAppl::info),
						Item.of("log", GitRepositoryAppl::log),
						Item.of("commitContent", GitRepositoryAppl::commitContent),
						Item.of("Commit", GitRepositoryAppl::commit),
						Item.of("switchTo", GitRepositoryAppl::switchTo),
						Item.of("getHead", GitRepositoryAppl::getHead),
						Item.exit()
						),
				Item.exit()
				);
				
	}
	
	private static void switchTo(InputOutput io) {
		String newName = io.readStringPredicate("Enter commit or branch name to switch", "Enter not emply commit or branch name", el -> el.length() > 0);
		io.writeLine(git.switchTo(newName));
	}
	
	private static void getHead(InputOutput io) {
		io.writeLine(git.getHead());
	}
	
	private static void save(InputOutput io) {
		git.save();
	}
	
	private static void commitContent(InputOutput io) {
		String commitName = io.readStringPredicate("Enter commit name", "Enter not emply commit name", el -> el.length() > 0);
		git.commitContent(commitName).forEach(el -> io.writeLine(el));
	}
	
	private static void info(InputOutput io) {
		git.info().forEach(el -> io.writeLine(el.getName() + " - " + el.getState()));
	}
	
	private static void log(InputOutput io) {
		git.log().forEach(el -> io.writeLine(el.getName() + " - " + el.getMessage()));
	}
	
	private static void commit(InputOutput io) {
		String commitMessage = io.readStringPredicate("Enter commit message", "Enter not emply commit message", el -> el.length() > 0);
		io.writeLine(git.commit(commitMessage));
	}
	
	private static void addIgnoredFileNameExp(InputOutput io) {
		String newRegex = io.readStringPredicate("Enter regex to add", "Enter not empty regex", el -> el.length() > 0);
		io.writeLine(git.addIgnoredFileNameExp(newRegex));
	}

	private static void removeIgnoredFileNameExp(InputOutput io) {
		String removeRegex = io.readStringPredicate("Enter regex to remove", "Enter not empty regex", el -> el.length() > 0);
		io.writeLine(git.removeIgnoredFileNameExp(removeRegex));
	}

	private static void getAllIgnoredFileNameExp(InputOutput io) {
		git.getAllIgnoredFileNameExp().forEach(io::writeLine);
	}

	private static void printBranch(InputOutput io) {
		git.branches().stream().forEach(io::writeLine);
	}

	private static void renameBranch(InputOutput io) {
		String branchName = io.readStringPredicate("Enter old name of branch", "Enter not empty name of branch", el -> el.length() > 0);
		String newBranchName = io.readStringPredicate("Enter new name of branch", "Enter not empty name of branch", el -> el.length() > 0);
		io.writeLine(git.renameBranch(branchName, newBranchName));
	}

	private static void deleteBranch(InputOutput io) {
		String branchName = io.readStringPredicate("Enter name of branch", "Enter not empty name of branch", el -> el.length() > 0);
		io.writeLine(git.deleteBranch(branchName));
	}

	private static void newBranch(InputOutput io) {
		String branchName = io.readStringPredicate("Enter name of branch", "Enter not empty name of branch", el -> el.length() > 0);
		io.writeLine(git.createBranch(branchName));
	}
} 
