package telran.io;

public class DisplayResult {
	long fileSize;
	long copyTime;
	
	public DisplayResult(long fileSize, long copyTime) {
		this.fileSize = fileSize;
		this.copyTime = copyTime;
	}
	
	@Override
	public String toString() {
		return String.format("FileSize: %,d bytes, CopyTime: %,d ms", fileSize, copyTime);
	}
}
