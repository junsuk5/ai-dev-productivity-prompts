package kr.or.publicdata.portal.search;

public final class PaginationPolicy {
    private PaginationPolicy() {
    }

    public static boolean hasNext(int pageNo, int numOfRows, int totalCount) {
        if (pageNo < 1) {
            throw new IllegalArgumentException("pageNo must be at least 1");
        }
        if (numOfRows < 1) {
            throw new IllegalArgumentException("numOfRows must be at least 1");
        }
        if (totalCount < 0) {
            throw new IllegalArgumentException("totalCount must not be negative");
        }
        return (long) pageNo * numOfRows <= totalCount;
    }
}
