package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

	public static void main(String[] args) throws IOException {
		// You can use print statements as follows for debugging, they'll be visible when running tests.
		LOGGER.info("Logs from your program will appear here!");

		try (var serverSocket = new ServerSocket(4221)) {
			serverSocket.setReuseAddress(true);
			var clientSocket = serverSocket.accept(); // Wait for connection from client.
			LOGGER.info("accepted new connection");
		} catch (IOException e) {
			LOGGER.info(String.format("IOException: %s", e.getMessage()));
		}
	}
}
