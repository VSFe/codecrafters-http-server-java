package http;

import http.model.HttpStatus;

public record HttpResponse(
	String httpProtocolVersion,
	HttpStatus httpStatus
) {
	public String createHttpResponseMessage() {
		return "%s %s %s\r\n\r\n".formatted(httpProtocolVersion, httpStatus.getHttpStatusCode(), httpStatus.getMessage());
	}
}
