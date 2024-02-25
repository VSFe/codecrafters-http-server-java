package http.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

import http.HttpRequest;
import http.HttpResponse;
import http.model.HttpMethod;
import http.model.HttpStatus;

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

	public static void readAndResponseRequest(Socket socket) {
		try (
			var inputStream = socket.getInputStream();
			var outputStream = socket.getOutputStream();
			var reader = new BufferedReader(new InputStreamReader(inputStream));
			var writer = new BufferedWriter(new OutputStreamWriter(outputStream))
		) {
			String input;
			var startLine = reader.readLine();
			var request = parseBasicHttpRequest(startLine);
			while ((input = reader.readLine()) != null && !input.isEmpty()) {
				var data = input.split(": ");
				request.headers().put(data[0], data[1]);
			}
			var response = parseRequestAndCreateResponse(request);

			writer.write(response.createHttpResponseMessage());
		} catch (IOException e) {
			throw new RuntimeException(String.format("IOException: %s%n", e.getMessage()));
		}
	}

	private static HttpRequest parseBasicHttpRequest(String startLine) {
		var info = startLine.split(" ");
		return new HttpRequest(HttpMethod.valueOf(info[0]), info[1], info[2], new HashMap<>());
	}

	private static HttpResponse parseRequestAndCreateResponse(HttpRequest request) {
		try {
			if ("/".equals(request.location())) {
				return HttpResponse.basicOf(HttpStatus.OK);
			} else if (request.location().startsWith("/echo/")) {
				var body = request.location().substring(request.location().indexOf("/echo/") + 6);
				return HttpResponse.basicOf(HttpStatus.OK, body);
			} else if (request.location().equals("/user-agent")) {
				var body = request.headers().get("User-Agent");
				return HttpResponse.basicOf(HttpStatus.OK, body);
			} else {
				return HttpResponse.basicOf(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.info(String.format("exception: %s%n", e.getMessage()));
			return HttpResponse.basicOf(HttpStatus.BAD_REQUEST);
		}
	}
}
