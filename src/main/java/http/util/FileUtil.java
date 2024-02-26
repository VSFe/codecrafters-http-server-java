package http.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
	private FileUtil() {

	}

	public static byte[] readFile(String directory, String fileName) {
		var fullPath = directory.endsWith("/") ? directory + fileName : directory + "/" + fileName;
		var path = Paths.get(fullPath);

		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			return null;
		}
	}
}
