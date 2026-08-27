package kr.or.publicdata.portal.facility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryFacilityRepository implements FacilityRepository {
    private final Map<String, FacilityRow> facilities = new LinkedHashMap<>();

    @Override
    public void save(FacilityRow facility) {
        facilities.put(facility.getFacilityCode(), facility);
    }

    @Override
    public List<FacilityRow> findAll() {
        return new ArrayList<>(facilities.values());
    }
}
