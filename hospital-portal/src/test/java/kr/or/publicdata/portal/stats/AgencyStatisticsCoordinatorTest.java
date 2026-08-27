package kr.or.publicdata.portal.stats;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AgencyStatisticsCoordinatorTest {
    @Test
    public void createsRowsWhenRepositoryIsEmpty() {
        InMemoryStatisticsRepository repository = new InMemoryStatisticsRepository();
        AgencyStatisticsCoordinator coordinator = new AgencyStatisticsCoordinator(
                (path, requestBody) -> "AGENCY-A|중앙기관|12\nAGENCY-B|동부기관|7",
                repository);

        SyncResult result = coordinator.synchronize();

        assertEquals(2, result.getProcessedCount());
        assertEquals(2, result.getCreatedCount());
        assertEquals(0, result.getUpdatedCount());
        assertEquals(2, repository.findAll().size());
    }

    @Test
    public void updatesRowWhenDataChanged() {
        InMemoryStatisticsRepository repository = new InMemoryStatisticsRepository();
        repository.save(new AgencyStatRow("AGENCY-A", "중앙기관", 9));
        AgencyStatisticsCoordinator coordinator = new AgencyStatisticsCoordinator(
                (path, requestBody) -> "AGENCY-A|중앙기관|12",
                repository);

        SyncResult result = coordinator.synchronize();

        assertEquals(1, result.getProcessedCount());
        assertEquals(0, result.getCreatedCount());
        assertEquals(1, result.getUpdatedCount());
        assertEquals(12, repository.findByAgencyCode("AGENCY-A").orElseThrow().getHospitalCount());
    }
}
