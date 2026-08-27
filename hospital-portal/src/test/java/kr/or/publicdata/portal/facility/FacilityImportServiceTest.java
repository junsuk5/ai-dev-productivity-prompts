package kr.or.publicdata.portal.facility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class FacilityImportServiceTest {
    private InMemoryFacilityRepository repository;
    private FacilityImportService service;

    @Before
    public void setUp() {
        repository = new InMemoryFacilityRepository();
        service = new FacilityImportService(repository);
    }

    @Test
    public void validBatchSavesEveryFacility() {
        ImportResult result = service.importAll(List.of(
                new FacilityRow("F-100", "중앙 시설", "서울"),
                new FacilityRow("F-200", "동부 시설", "강원")));

        assertEquals(2, result.getSavedCount());
        assertEquals(2, repository.findAll().size());
    }

    @Test
    public void emptyBatchSavesNothing() {
        ImportResult result = service.importAll(List.of());

        assertEquals(0, result.getSavedCount());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void invalidFirstRowSavesNothing() {
        InvalidFacilityException exception = assertThrows(
                InvalidFacilityException.class,
                () -> service.importAll(List.of(
                        new FacilityRow("", "중앙 시설", "서울"),
                        new FacilityRow("F-200", "동부 시설", "강원"))));

        assertEquals(1, exception.getRowNumber());
        assertEquals(0, repository.findAll().size());
    }
}
