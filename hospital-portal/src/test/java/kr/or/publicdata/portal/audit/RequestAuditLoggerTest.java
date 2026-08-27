package kr.or.publicdata.portal.audit;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import org.junit.Test;

public class RequestAuditLoggerTest {
    private static final URI REQUEST_URI = URI.create(
            "https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList"
                    + "?ServiceKey=TRAINING-SECRET&pageNo=1&numOfRows=10&yadmNm=training");

    private final RequestAuditLogger logger = new RequestAuditLogger();

    @Test
    public void includesOperationalMetadata() {
        RequestAuditEvent event = new RequestAuditEvent("GET", REQUEST_URI, 200, 42);

        String message = logger.format(event);

        assertTrue(message.contains("GET"));
        assertTrue(message.contains("/getHospBasisList"));
        assertTrue(message.contains("status=200"));
        assertTrue(message.contains("elapsedMs=42"));
    }
}
