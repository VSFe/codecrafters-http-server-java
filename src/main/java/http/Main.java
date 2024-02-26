package http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import http.connection.HttpConnectionResolver;
import http.util.HttpConnectionUtil;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());
	public static final Map<String, String> ARGS_MAP = new HashMap<>();

	public static void main(String[] args) throws IOException {
		parseArgs(args);
		var serverSocket = HttpConnectionUtil.createHttpServerSocket(CommonConstant.DEFAULT_PORT);
		LOGGER.info("Logs from your program will appear here!");

		while (true) {
			var clientSocket = serverSocket.accept();
			var httpConnectionResolver = new HttpConnectionResolver(clientSocket);
			httpConnectionResolver.start();
			LOGGER.info("accepted new connection");
		}
	}

	private static void parseArgs(String[] args) {
		if (args.length >= 2 && args[0].equals("--directory")) {
			ARGS_MAP.put("directory", args[1]);
		}
	}
}
