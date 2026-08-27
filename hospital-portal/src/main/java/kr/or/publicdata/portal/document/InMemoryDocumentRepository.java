package kr.or.publicdata.portal.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class InMemoryDocumentRepository implements DocumentRepository {
    private final List<AgencyDocument> documents;

    public InMemoryDocumentRepository(List<AgencyDocument> documents) {
        this.documents = new ArrayList<>(Objects.requireNonNull(documents, "documents"));
    }

    @Override
    public Optional<AgencyDocument> findByDocumentId(String documentId) {
        return documents.stream()
                .filter(document -> document.getDocumentId().equals(documentId))
                .findFirst();
    }
}
