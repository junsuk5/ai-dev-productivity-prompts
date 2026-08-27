package kr.or.publicdata.portal.audit;

import java.util.Objects;

public final class RequestAuditLogger {
    public String format(RequestAuditEvent event) {
        Objects.requireNonNull(event, "event");
        return event.getMethod()
                + " "
                + event.getRequestUri()
                + " status="
                + event.getStatusCode()
                + " elapsedMs="
                + event.getElapsedMillis();
    }
}
