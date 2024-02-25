package http.model;

public enum HttpStatus {
	// 200
	OK("200", "OK");

	private final String httpStatusCode;
	private final String message;

	HttpStatus(String httpStatusCode, String message) {
		this.httpStatusCode = httpStatusCode;
		this.message = message;
	}

	public String getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getMessage() {
		return message;
	}
}
