package kr.or.publicdata.portal.hira;

import java.util.Objects;
import kr.or.publicdata.portal.search.HospitalSearchResult;

public final class HiraResponseHandler {
    private final HiraXmlParser xmlParser;

    public HiraResponseHandler(HiraXmlParser xmlParser) {
        this.xmlParser = Objects.requireNonNull(xmlParser, "xmlParser");
    }

    public HospitalSearchResult handle(HttpResponseData response) {
        Objects.requireNonNull(response, "response");
        if (!response.isSuccessful()) {
            throw new HiraServiceException(
                    "HTTP_" + response.getStatusCode(),
                    "HIRA HTTP request failed");
        }
        return xmlParser.parse(response.getBody());
    }
}
