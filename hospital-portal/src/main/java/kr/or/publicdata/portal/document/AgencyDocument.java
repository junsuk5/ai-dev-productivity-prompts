package kr.or.publicdata.portal.document;

import java.util.Objects;

public final class AgencyDocument {
    private final String documentId;
    private final String ownerAgencyCode;
    private final String title;

    public AgencyDocument(String documentId, String ownerAgencyCode, String title) {
        this.documentId = Objects.requireNonNull(documentId, "documentId");
        this.ownerAgencyCode = Objects.requireNonNull(ownerAgencyCode, "ownerAgencyCode");
        this.title = Objects.requireNonNull(title, "title");
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getOwnerAgencyCode() {
        return ownerAgencyCode;
    }

    public String getTitle() {
        return title;
    }
}
