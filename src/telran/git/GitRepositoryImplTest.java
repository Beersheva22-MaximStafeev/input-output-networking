package telran.git;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class GitRepositoryImplTest {
	@Test
	void testPath() {
		String path = "..\\.\\";
		System.out.println(Path.of(path).toAbsolutePath().normalize());
		System.out.println(Path.of(path).toAbsolutePath().normalize().resolve(".mygit"));
	}
	
	@Test
	void testRegexp() {
		assertEquals(true, Pattern.matches(".mygit", "amygit"));
		assertEquals(true, Pattern.matches(".mygit", ".mygit"));
		assertEquals(true, Pattern.matches(".mygit", "?mygit"));
		assertEquals(true, Pattern.matches("\\.mygit", ".mygit"));
	}
	
	@Test
	void ignoredTests() {
		try {
			GitRepositoryImpl object = GitRepositoryImpl.init(".");
			Method addIgnoredFileNameExp = GitRepositoryImpl.class.getDeclaredMethod("addIgnoredFileNameExp", String.class);
			addIgnoredFileNameExp.setAccessible(true);
			assertEquals(GitRepositoryImpl.IGNORED_FILENAME_ADD_OK, 
					addIgnoredFileNameExp.invoke(object, GitRepositoryImpl.GIT_SAVE_NAME));
			assertEquals(GitRepositoryImpl.IGNORED_FILENAME_ADD_NOT_CORRECT, 
					addIgnoredFileNameExp.invoke(object, "[sdfs"));
			assertEquals(GitRepositoryImpl.IGNORED_FILENAME_ADD_ALREADY_EXISTS, 
					addIgnoredFileNameExp.invoke(object, "\\.mygit"));
//			assertEquals(GIT_SAVE_NAME.replace(".", "\\."), "\\.");
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void emptyHeadTest() {
		try {
			GitRepositoryImpl object = GitRepositoryImpl.init(".");
			Method getCommitFromHead = GitRepositoryImpl.class.getDeclaredMethod("getCommitFromHead");
			getCommitFromHead.setAccessible(true);
			assertEquals(null, 
					getCommitFromHead.invoke(object));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void collectorsTest() {
		List<String> list = new LinkedList<>();
		list.stream().collect(Collectors.groupingBy(el -> el.length()));
	}
	
	@Test
	void initTest() {
		String home = "C:\\TelRan\\myGitRepa";
		GitRepositoryImpl git1 = GitRepositoryImpl.init(home);
		GitRepositoryImpl git2 = GitRepositoryImpl.init(home);
		assertEquals(git1, git2);
	}
}
