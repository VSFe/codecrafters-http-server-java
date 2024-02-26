package http.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
	private FileUtil() {

	}

	public static byte[] readFile(String directory, String fileName) {
		var path = getPath(directory, fileName);

		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			return null;
		}
	}

	public static void writeFile(String directory, String fileName, String body) {
		var path = getPath(directory, fileName);

		try {
			Files.writeString(path, body);
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	private static Path getPath(String directory, String fileName) {
		var fullPath = directory.endsWith("/") ? directory + fileName : directory + "/" + fileName;
		return Paths.get(fullPath);
	}
}
