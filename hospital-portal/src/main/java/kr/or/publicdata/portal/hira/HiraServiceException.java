package kr.or.publicdata.portal.hira;

public final class HiraServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String resultCode;

    public HiraServiceException(String resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public HiraServiceException(String resultCode, String message, Throwable cause) {
        super(message, cause);
        this.resultCode = resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }
}
