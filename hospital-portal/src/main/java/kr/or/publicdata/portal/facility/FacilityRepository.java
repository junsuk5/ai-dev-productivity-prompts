package kr.or.publicdata.portal.facility;

import java.util.List;

public interface FacilityRepository {
    void save(FacilityRow facility);

    List<FacilityRow> findAll();
}
