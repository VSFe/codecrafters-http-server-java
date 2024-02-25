package http.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import http.HttpResponse;
import http.model.HttpStatus;

public class HttpConnectionUtil {
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
			var request = reader.readLine();
			var response = parseRequestAndCreateResponse(request);

			writer.write(response);
		} catch (IOException e) {
			throw new RuntimeException(String.format("IOException: %s%n", e.getMessage()));
		}
	}

	private static String parseRequestAndCreateResponse(String request) {
		return new HttpResponse("HTTP/1.1", HttpStatus.OK).createHttpResponseMessage();
	}
}
