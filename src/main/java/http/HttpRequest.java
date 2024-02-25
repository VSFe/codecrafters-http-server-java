package http;

import java.util.Map;

import http.model.HttpMethod;

public record HttpRequest(
	HttpMethod httpMethod,
	String location,
	String httpProtocolVersion,
	Map<String, String> headers
) {

}
