package telran.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FilesCopy extends Copy {

	public FilesCopy(String srcFilePath, String destFilePath, boolean overwite) {
		super(srcFilePath, destFilePath, overwite);
	}

	@Override
	public long copy() throws Exception {
		if (!isOverwite() && Files.exists(Path.of(getDestFilePath()))) {
			throw new Exception(String.format("File %s already exists", getDestFilePath()));
		}
		long res = 0;
		try {
			Files.copy(Path.of(getSrcFilePath()), Path.of(getDestFilePath()), StandardCopyOption.REPLACE_EXISTING);
			res = Files.size(Path.of(getDestFilePath()));
		} catch (Exception e) {
		}
		return res;
	}

	@Override
	DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResult(fileSize, copyTime);
	}

}
