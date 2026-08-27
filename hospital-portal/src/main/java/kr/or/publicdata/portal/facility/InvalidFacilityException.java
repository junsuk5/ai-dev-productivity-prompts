package kr.or.publicdata.portal.facility;

public final class InvalidFacilityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int rowNumber;

    public InvalidFacilityException(int rowNumber, String reason) {
        super("Invalid facility at row " + rowNumber + ": " + reason);
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
