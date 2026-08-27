package kr.or.publicdata.portal.document;

import java.util.Objects;

public final class DocumentQueryService {
    private final DocumentRepository repository;

    public DocumentQueryService(DocumentRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public AgencyDocument findForAgency(String requesterAgencyCode, String documentId) {
        Objects.requireNonNull(requesterAgencyCode, "requesterAgencyCode");
        Objects.requireNonNull(documentId, "documentId");
        return repository.findByDocumentId(documentId)
                .orElseThrow(DocumentNotFoundException::new);
    }
}
