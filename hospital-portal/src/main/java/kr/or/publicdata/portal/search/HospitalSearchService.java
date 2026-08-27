package kr.or.publicdata.portal.search;

import java.util.Objects;
import kr.or.publicdata.portal.hira.HiraResponseHandler;
import kr.or.publicdata.portal.hira.HttpResponseData;

public final class HospitalSearchService {
    private final HiraResponseHandler responseHandler;

    public HospitalSearchService(HiraResponseHandler responseHandler) {
        this.responseHandler = Objects.requireNonNull(responseHandler, "responseHandler");
    }

    public HospitalSearchResult search(HttpResponseData response) {
        return responseHandler.handle(response);
    }
}
