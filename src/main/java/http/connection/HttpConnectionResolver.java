package http.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;

import http.util.HttpDataUtil;

public class HttpConnectionResolver extends Thread {
	private final Socket socket;
	private final Logger logger = Logger.getLogger(this.getName());

	public HttpConnectionResolver(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (
			var inputStream = socket.getInputStream();
			var outputStream = socket.getOutputStream();
			var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			var bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
		) {
			String input;
			var startLine = bufferedReader.readLine();
			var request = HttpDataUtil.parseBasicHttpRequest(startLine);
			while ((input = bufferedReader.readLine()) != null && !input.isEmpty()) {
				var data = input.split(": ");
				request.headers().put(data[0], data[1]);
			}
			var response = HttpDataUtil.parseRequestAndCreateResponse(request);

			bufferedWriter.write(response.createHttpResponseMessage());
		} catch (IOException e) {
			logger.info(String.format("create I/O stream error: %s", e.getMessage()));
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				logger.info(String.format("close socket error: %s%n", e.getMessage()));
			}
		}
	}
}
