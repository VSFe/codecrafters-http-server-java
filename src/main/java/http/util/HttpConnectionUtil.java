package http.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class HttpConnectionUtil {
	private static final Logger LOGGER = Logger.getLogger(HttpConnectionUtil.class.getSimpleName());
	private HttpConnectionUtil() {

	}

	public static ServerSocket createHttpServerSocket(int port) {
		try {
			var serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					serverSocket.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}));

			return serverSocket;
		} catch (IOException e) {
			throw new RuntimeException(String.format("IOException: %s%n", e.getMessage()));
		}
	}
}
