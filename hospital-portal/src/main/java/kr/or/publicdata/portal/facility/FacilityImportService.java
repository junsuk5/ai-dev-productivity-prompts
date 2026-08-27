package kr.or.publicdata.portal.facility;

import java.util.List;
import java.util.Objects;

public final class FacilityImportService {
    private final FacilityRepository repository;

    public FacilityImportService(FacilityRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public ImportResult importAll(List<FacilityRow> facilities) {
        Objects.requireNonNull(facilities, "facilities");
        int savedCount = 0;

        for (int index = 0; index < facilities.size(); index++) {
            FacilityRow facility = facilities.get(index);
            validate(facility, index + 1);
            repository.save(facility);
            savedCount++;
        }

        return new ImportResult(savedCount);
    }

    private void validate(FacilityRow facility, int rowNumber) {
        if (facility.getFacilityCode().isBlank()) {
            throw new InvalidFacilityException(rowNumber, "facilityCode is blank");
        }
        if (facility.getFacilityName().isBlank()) {
            throw new InvalidFacilityException(rowNumber, "facilityName is blank");
        }
        if (facility.getRegionName().isBlank()) {
            throw new InvalidFacilityException(rowNumber, "regionName is blank");
        }
    }
}
