package http;

import java.io.IOException;
import java.util.logging.Logger;

import http.connection.HttpConnectionResolver;
import http.util.HttpConnectionUtil;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

	public static void main(String[] args) throws IOException {
		var serverSocket = HttpConnectionUtil.createHttpServerSocket(CommonConstant.DEFAULT_PORT);
		LOGGER.info("Logs from your program will appear here!");

		while (true) {
			var clientSocket = serverSocket.accept();
			var httpConnectionResolver = new HttpConnectionResolver(clientSocket);
			httpConnectionResolver.start();
			LOGGER.info("accepted new connection");
		}
	}
}
