package kr.or.publicdata.portal.stats;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryStatisticsRepository implements StatisticsRepository {
    private final Map<String, AgencyStatRow> rows = new LinkedHashMap<>();

    @Override
    public Optional<AgencyStatRow> findByAgencyCode(String agencyCode) {
        return Optional.ofNullable(rows.get(agencyCode));
    }

    @Override
    public void save(AgencyStatRow row) {
        rows.put(row.getAgencyCode(), row);
    }

    @Override
    public List<AgencyStatRow> findAll() {
        return new ArrayList<>(rows.values());
    }
}
