package kr.or.publicdata.portal.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class HospitalSearchResult {
    private final List<HospitalInfo> hospitals;
    private final int totalCount;
    private final int pageNo;
    private final int numOfRows;
    private final boolean hasNext;

    public HospitalSearchResult(
            List<HospitalInfo> hospitals,
            int totalCount,
            int pageNo,
            int numOfRows,
            boolean hasNext) {
        this.hospitals = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(hospitals, "hospitals")));
        this.totalCount = totalCount;
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.hasNext = hasNext;
    }

    public List<HospitalInfo> getHospitals() {
        return hospitals;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public boolean hasNext() {
        return hasNext;
    }
}
