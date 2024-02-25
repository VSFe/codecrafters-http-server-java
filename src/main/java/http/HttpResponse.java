package http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import http.model.HttpStatus;

public record HttpResponse(
	String httpProtocolVersion,
	HttpStatus httpStatus,
	Map<String, String> header,
	String body
) {
	public static HttpResponse basicOf(HttpStatus httpStatus) {
		return basicOf(httpStatus, null, null);
	}

	public static HttpResponse basicOf(HttpStatus httpStatus, String body) {
		return basicOf(httpStatus, null, body);
	}

	public static HttpResponse basicOf(HttpStatus httpStatus, Map<String, String> header, String body) {
		header = Objects.requireNonNullElse(header, new HashMap<>());
		if (body != null) {
			header.put("Content-Type", "text/plain");
			header.put("Content-Length", String.valueOf(body.length()));
		}

		return new HttpResponse("HTTP/1.1", httpStatus, header, body);
	}

	public String createHttpResponseMessage() {
		var strBuilder = new StringBuilder("%s %s %s\r\n".formatted(httpProtocolVersion, httpStatus.getHttpStatusCode(), httpStatus.getMessage()));

		for (var headerEntry : header.entrySet()) {
			strBuilder.append(String.format("%s: %s\r\n", headerEntry.getKey(), headerEntry.getValue()));
		}
		strBuilder.append("\r\n");

		if (body != null && !body.isEmpty()) {
			strBuilder.append(String.format("%s\r\n", body));
		}

		return strBuilder.toString();
	}
}
