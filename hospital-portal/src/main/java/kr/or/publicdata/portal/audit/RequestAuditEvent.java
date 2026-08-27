package kr.or.publicdata.portal.audit;

import java.net.URI;
import java.util.Objects;

public final class RequestAuditEvent {
    private final String method;
    private final URI requestUri;
    private final int statusCode;
    private final long elapsedMillis;

    public RequestAuditEvent(String method, URI requestUri, int statusCode, long elapsedMillis) {
        this.method = Objects.requireNonNull(method, "method");
        this.requestUri = Objects.requireNonNull(requestUri, "requestUri");
        this.statusCode = statusCode;
        this.elapsedMillis = elapsedMillis;
    }

    public String getMethod() {
        return method;
    }

    public URI getRequestUri() {
        return requestUri;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }
}
