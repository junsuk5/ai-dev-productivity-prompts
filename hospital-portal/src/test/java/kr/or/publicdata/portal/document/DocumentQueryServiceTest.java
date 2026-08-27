package kr.or.publicdata.portal.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DocumentQueryServiceTest {
    private DocumentQueryService service;

    @Before
    public void setUp() {
        InMemoryDocumentRepository repository = new InMemoryDocumentRepository(List.of(
                new AgencyDocument("DOC-100", "AGENCY-A", "시설 점검 결과"),
                new AgencyDocument("DOC-200", "AGENCY-B", "장비 교체 계획")));
        service = new DocumentQueryService(repository);
    }

    @Test
    public void owningAgencyCanReadItsDocument() {
        AgencyDocument document = service.findForAgency("AGENCY-A", "DOC-100");

        assertEquals("시설 점검 결과", document.getTitle());
        assertEquals("AGENCY-A", document.getOwnerAgencyCode());
    }

    @Test
    public void missingDocumentUsesPublicNotFoundResult() {
        DocumentNotFoundException exception = assertThrows(
                DocumentNotFoundException.class,
                () -> service.findForAgency("AGENCY-A", "DOC-999"));

        assertEquals("DOCUMENT_NOT_FOUND", exception.getResultCode());
        assertEquals("요청한 문서를 찾을 수 없습니다.", exception.getMessage());
    }
}
