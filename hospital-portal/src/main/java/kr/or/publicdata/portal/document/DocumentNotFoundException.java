package kr.or.publicdata.portal.document;

public final class DocumentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private static final String RESULT_CODE = "DOCUMENT_NOT_FOUND";

    public DocumentNotFoundException() {
        super("요청한 문서를 찾을 수 없습니다.");
    }

    public String getResultCode() {
        return RESULT_CODE;
    }
}
