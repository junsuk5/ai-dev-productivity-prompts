package kr.or.publicdata.portal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import kr.or.publicdata.portal.audit.RequestAuditEvent;
import kr.or.publicdata.portal.audit.RequestAuditLogger;
import kr.or.publicdata.portal.document.AgencyDocument;
import kr.or.publicdata.portal.document.DocumentNotFoundException;
import kr.or.publicdata.portal.document.DocumentQueryService;
import kr.or.publicdata.portal.document.InMemoryDocumentRepository;
import kr.or.publicdata.portal.facility.FacilityImportService;
import kr.or.publicdata.portal.facility.FacilityRow;
import kr.or.publicdata.portal.facility.ImportResult;
import kr.or.publicdata.portal.facility.InMemoryFacilityRepository;
import kr.or.publicdata.portal.hira.HiraResponseHandler;
import kr.or.publicdata.portal.hira.HiraServiceException;
import kr.or.publicdata.portal.hira.HiraXmlParser;
import kr.or.publicdata.portal.hira.HttpResponseData;
import kr.or.publicdata.portal.search.HospitalSearchResult;
import kr.or.publicdata.portal.search.HospitalSearchService;
import kr.or.publicdata.portal.stats.AgencyStatisticsCoordinator;
import kr.or.publicdata.portal.stats.InMemoryStatisticsRepository;
import kr.or.publicdata.portal.stats.SyncResult;

public final class HospitalPortalApplication {
    private static final String TRAINING_URI =
            "https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList"
                    + "?ServiceKey=TRAINING-SECRET&pageNo=1&numOfRows=10&yadmNm=training";

    private HospitalPortalApplication() {
    }

    public static void main(String[] args) throws IOException {
        printSearchFlow();
        printAuditFlow();
        printDocumentFlow();
        printFacilityFlow();
        printStatisticsFlow();
    }

    private static void printSearchFlow() throws IOException {
        HospitalSearchService searchService =
                new HospitalSearchService(new HiraResponseHandler(new HiraXmlParser()));

        printSearchCase(searchService, "hospitals-success.xml", "전체 11건");
        printSearchCase(searchService, "hospitals-page-full.xml", "전체 10건");
        printSearchCase(searchService, "hospitals-service-error.xml", "resultCode 30");
    }

    private static void printSearchCase(
            HospitalSearchService searchService,
            String fixtureName,
            String label) throws IOException {
        HttpResponseData response = new HttpResponseData(200, fixture(fixtureName));
        try {
            HospitalSearchResult result = searchService.search(response);
            System.out.println("[검색] " + label
                    + " · 전체 건수=" + result.getTotalCount()
                    + ", 현재 페이지=" + result.getPageNo()
                    + ", 페이지 크기=" + result.getNumOfRows()
                    + ", 조회 건수=" + result.getHospitals().size()
                    + ", 다음 페이지=" + result.hasNext());
        } catch (HiraServiceException exception) {
            System.out.println("[검색] " + label
                    + " · 오류로 판정 resultCode=" + exception.getResultCode());
        }
    }

    private static void printAuditFlow() {
        RequestAuditEvent event = new RequestAuditEvent("GET", URI.create(TRAINING_URI), 200, 42);
        System.out.println("[감사] " + new RequestAuditLogger().format(event));
    }

    private static void printDocumentFlow() {
        DocumentQueryService documentService = new DocumentQueryService(
                new InMemoryDocumentRepository(List.of(
                        new AgencyDocument("DOC-100", "AGENCY-A", "시설 점검 결과"),
                        new AgencyDocument("DOC-200", "AGENCY-B", "장비 교체 계획"))));

        try {
            AgencyDocument document = documentService.findForAgency("AGENCY-A", "DOC-200");
            System.out.println("[문서] 요청 기관=AGENCY-A, 문서=" + document.getDocumentId()
                    + ", 소유 기관=" + document.getOwnerAgencyCode()
                    + ", 제목=" + document.getTitle());
        } catch (DocumentNotFoundException exception) {
            System.out.println("[문서] 요청 기관=AGENCY-A, 문서=DOC-200 · "
                    + exception.getResultCode());
        }
    }

    private static void printFacilityFlow() {
        InMemoryFacilityRepository repository = new InMemoryFacilityRepository();
        FacilityImportService importService = new FacilityImportService(repository);

        try {
            importService.importAll(List.of(
                    new FacilityRow("F-100", "중앙 시설", "서울"),
                    new FacilityRow("", "동부 시설", "강원"),
                    new FacilityRow("F-300", "남부 시설", "부산")));
        } catch (RuntimeException exception) {
            System.out.println("[일괄등록] 실패: " + exception.getMessage());
        }
        System.out.println("[일괄등록] 실패 후 저장된 건수=" + repository.findAll().size());

        InMemoryFacilityRepository cleanRepository = new InMemoryFacilityRepository();
        ImportResult result = new FacilityImportService(cleanRepository).importAll(List.of(
                new FacilityRow("F-100", "중앙 시설", "서울"),
                new FacilityRow("F-200", "동부 시설", "강원")));
        System.out.println("[일괄등록] 정상 배치 저장 건수=" + result.getSavedCount());
    }

    private static void printStatisticsFlow() {
        AgencyStatisticsCoordinator coordinator = new AgencyStatisticsCoordinator(
                (path, requestBody) -> "AGENCY-A|중앙기관|12\nAGENCY-B|동부기관|7",
                new InMemoryStatisticsRepository());
        SyncResult syncResult = coordinator.synchronize();
        System.out.println("[통계] 처리=" + syncResult.getProcessedCount()
                + ", 신규=" + syncResult.getCreatedCount()
                + ", 수정=" + syncResult.getUpdatedCount());
    }

    private static String fixture(String name) throws IOException {
        try (InputStream input = HospitalPortalApplication.class.getResourceAsStream("/fixtures/" + name)) {
            if (input == null) {
                throw new IOException("Fixture not found: " + name);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
