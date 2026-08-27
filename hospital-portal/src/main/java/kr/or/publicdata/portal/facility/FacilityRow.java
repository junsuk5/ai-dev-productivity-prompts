package kr.or.publicdata.portal.facility;

import java.util.Objects;

public final class FacilityRow {
    private final String facilityCode;
    private final String facilityName;
    private final String regionName;

    public FacilityRow(String facilityCode, String facilityName, String regionName) {
        this.facilityCode = Objects.requireNonNull(facilityCode, "facilityCode");
        this.facilityName = Objects.requireNonNull(facilityName, "facilityName");
        this.regionName = Objects.requireNonNull(regionName, "regionName");
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getRegionName() {
        return regionName;
    }
}
