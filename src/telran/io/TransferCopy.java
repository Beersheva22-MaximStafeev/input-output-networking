package telran.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TransferCopy extends Copy {

	public TransferCopy(String srcFilePath, String destFilePath, boolean overwite) {
		super(srcFilePath, destFilePath, overwite);
	}

	@Override
	public long copy() throws Exception {
		if (!isOverwite() && Files.exists(Path.of(getDestFilePath()))) {
			throw new Exception(String.format("File %s already exists", getDestFilePath()));
		}
		long res = 0;
		try (InputStream input = new FileInputStream(getSrcFilePath());
				OutputStream output = new FileOutputStream(getDestFilePath(), false)) {
			res = input.available();
			input.transferTo(output);
		}
		return res;
	}

	@Override
	DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResult(fileSize, copyTime);
	}

}
