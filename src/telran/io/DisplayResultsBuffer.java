package telran.io;

public class DisplayResultsBuffer extends DisplayResult {
	long bufferSize;
	
	public DisplayResultsBuffer(long fileSize, long copyTime, long bufferSize) {
		super(fileSize, copyTime);
		this.bufferSize = bufferSize;
	}
	
	@Override
	public String toString() {
//		return String.format("FileSize: %d bytes, CopyTime: %d ms", fileSize, copyTime);
		return super.toString() + String.format(", BufferSize: %,d bytes", bufferSize);
	}
}
