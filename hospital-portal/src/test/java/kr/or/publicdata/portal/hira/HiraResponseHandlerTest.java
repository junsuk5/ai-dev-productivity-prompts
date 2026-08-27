package kr.or.publicdata.portal.hira;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import kr.or.publicdata.portal.search.HospitalSearchResult;
import org.junit.Test;

public class HiraResponseHandlerTest {
    private final HiraResponseHandler handler = new HiraResponseHandler(new HiraXmlParser());

    @Test
    public void returnsHospitalsWhenHttpAndServiceAreSuccessful() throws IOException {
        HttpResponseData response = new HttpResponseData(200, fixture("hospitals-success.xml"));

        HospitalSearchResult result = handler.handle(response);

        assertEquals(11, result.getTotalCount());
        assertEquals(1, result.getPageNo());
        assertEquals(10, result.getNumOfRows());
        assertEquals(1, result.getHospitals().size());
        assertEquals("서울의료원", result.getHospitals().get(0).getName());
        assertTrue(result.hasNext());
    }

    @Test
    public void returnsEmptyWhenSearchIsSuccessfulButNoItemsExist() throws IOException {
        HttpResponseData response = new HttpResponseData(200, fixture("hospitals-empty.xml"));

        HospitalSearchResult result = handler.handle(response);

        assertEquals(0, result.getTotalCount());
        assertTrue(result.getHospitals().isEmpty());
    }

    @Test
    public void rejectsHttpFailure() {
        HttpResponseData response = new HttpResponseData(503, "temporarily unavailable");

        HiraServiceException exception = assertThrows(
                HiraServiceException.class,
                () -> handler.handle(response));

        assertEquals("HTTP_503", exception.getResultCode());
    }

    @Test
    public void rejectsMalformedXml() throws IOException {
        HttpResponseData response = new HttpResponseData(200, fixture("hospitals-malformed.xml"));

        HiraServiceException exception = assertThrows(
                HiraServiceException.class,
                () -> handler.handle(response));

        assertEquals("RESPONSE_PARSE_ERROR", exception.getResultCode());
    }

    private String fixture(String name) throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/fixtures/" + name)) {
            if (input == null) {
                throw new IOException("Fixture not found: " + name);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
