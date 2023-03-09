package telran.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class BufferCopy extends Copy {
	private static final int DEFAULT_BUFFER_SIZE = 1024*1024;
	private long bufferSize;
	
	public BufferCopy(String srcFilePath, String destFilePath, boolean overwite) {
		this(srcFilePath, destFilePath, overwite, DEFAULT_BUFFER_SIZE);
	}
	
	public BufferCopy(String srcFilePath, String destFilePath, boolean overwite, long bufferSize) {
		super(srcFilePath, destFilePath, overwite);
		this.bufferSize = bufferSize;
	}

	public long getBufferSize() {
		return bufferSize;
	}

	@Override
	public long copy() throws Exception {
		if (!isOverwite() && Files.exists(Path.of(getDestFilePath()))) {
			throw new Exception(String.format("File %s already exists", getDestFilePath()));
		}
		long res = 0;
		try (InputStream input = new FileInputStream(getSrcFilePath());
				OutputStream output = new FileOutputStream(getDestFilePath(), !isOverwite())) {
			int size;
			byte[] buffer = new byte[(int) bufferSize];
			while ((size = input.read(buffer)) > 0) {
				output.write(buffer, 0, size);
				res += size;
			}
		}
		return res;
	}

	@Override
	DisplayResult getDisplayResult(long copyTime, long fileSize) {
		return new DisplayResultsBuffer(fileSize, copyTime, bufferSize);
	}

}
