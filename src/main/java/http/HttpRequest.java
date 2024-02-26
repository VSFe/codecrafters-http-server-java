package http;

import java.util.Map;

import http.model.HttpMethod;

public record HttpRequest(
	HttpMethod httpMethod,
	String location,
	String httpProtocolVersion,
	Map<String, String> headers,
	String body
) {
	public static HttpRequest withBody(HttpRequest request, String body) {
		return new HttpRequest(request.httpMethod, request.location, request.httpProtocolVersion, request.headers, body);
	}
}
