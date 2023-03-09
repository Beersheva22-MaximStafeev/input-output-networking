package telran.io;

import java.nio.file.Path;

public abstract class Copy {
	private String srcFilePath;
	private String destFilePath;
	private boolean overwite;
	
	public Copy(String srcFilePath, String destFilePath, boolean overwite) {
		this.srcFilePath = srcFilePath;
		this.destFilePath = destFilePath;
		this.overwite = overwite;
	}
		
	public boolean isOverwite() {
		return overwite;
	}

	public String getDestFilePath() {
		return destFilePath;
	}

	public String getSrcFilePath() {
		return srcFilePath;
	}

	public abstract long copy() throws Exception;
	
	abstract DisplayResult getDisplayResult(long copyTime, long fileSize);
	
	public void copyRun() throws Exception {
		long startTime, copyTime;
		startTime = System.currentTimeMillis();
		long fileSize = copy();
		copyTime = System.currentTimeMillis() - startTime; 
		String displayResult = 
				String.format("Method: %s, SrcFile: %s, DestFile: %s, ", 
						this.getClass().getName(), 
						Path.of(srcFilePath).getFileName(), 
						Path.of(destFilePath).getFileName()) 
				+ getDisplayResult(copyTime, fileSize).toString();
		System.out.println(displayResult);
	}
}
