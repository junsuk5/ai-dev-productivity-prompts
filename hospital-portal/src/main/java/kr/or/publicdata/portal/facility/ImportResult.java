package kr.or.publicdata.portal.facility;

public final class ImportResult {
    private final int savedCount;

    public ImportResult(int savedCount) {
        this.savedCount = savedCount;
    }

    public int getSavedCount() {
        return savedCount;
    }
}
