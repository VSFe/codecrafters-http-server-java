package http.util;

import java.util.HashMap;
import java.util.logging.Logger;

import http.HttpRequest;
import http.HttpResponse;
import http.model.HttpMethod;
import http.model.HttpStatus;

public class HttpDataUtil {
	private HttpDataUtil() {

	}

	private static final Logger LOGGER = Logger.getLogger(HttpConnectionUtil.class.getSimpleName());

	public static HttpRequest parseBasicHttpRequest(String startLine) {
		var info = startLine.split(" ");
		return new HttpRequest(HttpMethod.valueOf(info[0]), info[1], info[2], new HashMap<>());
	}

	public static HttpResponse parseRequestAndCreateResponse(HttpRequest request) {
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
