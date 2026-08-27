package kr.or.publicdata.portal.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AgencyStatisticsCoordinator {
    private static final String STAT_LIST_PATH = "/stats/request/agency_list";

    private final StatisticsTransport transport;
    private final StatisticsRepository repository;

    public AgencyStatisticsCoordinator(StatisticsTransport transport, StatisticsRepository repository) {
        this.transport = Objects.requireNonNull(transport, "transport");
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public SyncResult synchronize() {
        System.out.println("기관별 통계 동기화를 시작합니다.");
        String responseBody = transport.post(STAT_LIST_PATH, "{}");
        List<String[]> parsedRows = parseResponse(responseBody);
        int createdCount = 0;
        int updatedCount = 0;

        for (String[] fields : parsedRows) {
            String agencyCode = fields[0];
            String agencyName = fields[1];
            int hospitalCount = Integer.parseInt(fields[2]);

            if (agencyCode.isBlank()) {
                System.out.println("기관 코드가 없는 데이터는 건너뜁니다.");
                continue;
            }

            Optional<AgencyStatRow> existing = repository.findByAgencyCode(agencyCode);
            if (existing.isEmpty()) {
                repository.save(new AgencyStatRow(agencyCode, agencyName, hospitalCount));
                createdCount++;
            } else if (!existing.get().hasSameData(agencyName, hospitalCount)) {
                AgencyStatRow row = existing.get();
                row.update(agencyName, hospitalCount);
                repository.save(row);
                updatedCount++;
            }
        }

        System.out.println(
                "통계 동기화 완료: 처리=" + parsedRows.size()
                        + ", 신규=" + createdCount
                        + ", 수정=" + updatedCount);
        return new SyncResult(parsedRows.size(), createdCount, updatedCount);
    }

    private List<String[]> parseResponse(String responseBody) {
        List<String[]> rows = new ArrayList<>();
        if (responseBody == null || responseBody.isBlank()) {
            return rows;
        }

        String[] lines = responseBody.split("\\R");
        for (String line : lines) {
            String[] fields = line.split("\\|", -1);
            if (fields.length != 3) {
                throw new IllegalArgumentException("Invalid agency statistics response: " + line);
            }
            rows.add(new String[] {fields[0].trim(), fields[1].trim(), fields[2].trim()});
        }
        return rows;
    }
}
