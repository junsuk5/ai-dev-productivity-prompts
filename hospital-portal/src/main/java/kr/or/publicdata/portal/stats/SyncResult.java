package kr.or.publicdata.portal.stats;

public final class SyncResult {
    private final int processedCount;
    private final int createdCount;
    private final int updatedCount;

    public SyncResult(int processedCount, int createdCount, int updatedCount) {
        this.processedCount = processedCount;
        this.createdCount = createdCount;
        this.updatedCount = updatedCount;
    }

    public int getProcessedCount() {
        return processedCount;
    }

    public int getCreatedCount() {
        return createdCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }
}
