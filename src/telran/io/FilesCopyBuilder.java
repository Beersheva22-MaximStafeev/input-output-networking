package telran.io;

public class FilesCopyBuilder {
	public Copy build(String type, String[] args) {
		Copy res = null;
		switch (type) {
			case "FilesCopy":
				res = new FilesCopy(args[0], args[1], Boolean.parseBoolean(args[2]));
				break;
			case "TransferCopy":
				res = new TransferCopy(args[0], args[1], Boolean.parseBoolean(args[2]));
				break;
			case "BufferCopy":
				Integer bufferSize = getBufferSize(args);
				if (bufferSize != null) {
					res = new BufferCopy(args[0], args[1], Boolean.parseBoolean(args[2]), bufferSize);
				} else {
					res = new BufferCopy(args[0], args[1], Boolean.parseBoolean(args[2]));
				}
				break;
		}
		return res;
	}
	
	private Integer getBufferSize(String[] args) {
		Integer res = null;
		if (args.length > 3) {
			try {
				res = Integer.parseInt(args[3]);
			} catch (Exception e) {
				res = null;
			}
		}
		return res;
	}

}
