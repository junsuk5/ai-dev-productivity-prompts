package kr.or.publicdata.portal.document;

import java.util.Optional;

public interface DocumentRepository {
    Optional<AgencyDocument> findByDocumentId(String documentId);
}
