package kr.or.publicdata.portal.stats;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository {
    Optional<AgencyStatRow> findByAgencyCode(String agencyCode);

    void save(AgencyStatRow row);

    List<AgencyStatRow> findAll();
}
