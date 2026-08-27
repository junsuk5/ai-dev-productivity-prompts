package kr.or.publicdata.portal.hira;

import java.util.Objects;

public final class HttpResponseData {
    private final int statusCode;
    private final String body;

    public HttpResponseData(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = Objects.requireNonNull(body, "body");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }
}
