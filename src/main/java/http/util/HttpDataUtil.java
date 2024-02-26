package http.util;

import java.util.HashMap;
import java.util.logging.Logger;

import http.HttpRequest;
import http.HttpResponse;
import http.Main;
import http.model.HttpMethod;
import http.model.HttpStatus;

public class HttpDataUtil {
	private HttpDataUtil() {

	}

	private static final Logger LOGGER = Logger.getLogger(HttpConnectionUtil.class.getSimpleName());

	public static HttpRequest parseBasicHttpRequest(String startLine) {
		var info = startLine.split(" ");
		return new HttpRequest(HttpMethod.valueOf(info[0]), info[1], info[2], new HashMap<>(), null);
	}

	public static HttpResponse parseRequestAndCreateResponse(HttpRequest request) {
		try {
			var location = request.location();

			if ("/".equals(location)) {
				return HttpResponse.basicOf(HttpStatus.OK);
			} else if (location.startsWith("/echo/")) {
				var body = location.substring(location.indexOf("/echo/") + 6);
				return HttpResponse.basicOf(HttpStatus.OK, body);
			} else if (location.equals("/user-agent")) {
				var body = request.headers().get("User-Agent");
				return HttpResponse.basicOf(HttpStatus.OK, body);
			} else if (location.startsWith("/files")) {
				var fileName = location.substring(location.indexOf("/files/") + 7);
				if (request.httpMethod() == HttpMethod.GET) {
					var file = FileUtil.readFile(Main.ARGS_MAP.get("directory"), fileName);
					return HttpResponse.fileOf(file);
				} else if (request.httpMethod() == HttpMethod.POST) {
					var file = request.body();
					FileUtil.writeFile(Main.ARGS_MAP.get("directory"), fileName, file);
					return HttpResponse.basicOf(HttpStatus.CREATED, null);
				} else {
					// TODO: replace 405
					throw new RuntimeException();
				}
			} else {
				return HttpResponse.basicOf(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.info(String.format("exception: %s%n", e.getMessage()));
			return HttpResponse.basicOf(HttpStatus.BAD_REQUEST);
		}
	}
}
