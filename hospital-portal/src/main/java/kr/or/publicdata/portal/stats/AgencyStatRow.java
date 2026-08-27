package kr.or.publicdata.portal.stats;

import java.util.Objects;

public final class AgencyStatRow {
    private final String agencyCode;
    private String agencyName;
    private int hospitalCount;

    public AgencyStatRow(String agencyCode, String agencyName, int hospitalCount) {
        this.agencyCode = Objects.requireNonNull(agencyCode, "agencyCode");
        this.agencyName = Objects.requireNonNull(agencyName, "agencyName");
        this.hospitalCount = hospitalCount;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public int getHospitalCount() {
        return hospitalCount;
    }

    public boolean hasSameData(String otherName, int otherCount) {
        return agencyName.equals(otherName) && hospitalCount == otherCount;
    }

    public void update(String newName, int newCount) {
        this.agencyName = Objects.requireNonNull(newName, "newName");
        this.hospitalCount = newCount;
    }
}
