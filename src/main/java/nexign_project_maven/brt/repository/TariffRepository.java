package nexign_project_maven.brt.repository;

import nexign_project_maven.brt.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
    Tariff findTariffById(Long id);

    @Query(value = "SELECT * FROM tariffs ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Tariff findRandomTariff();
}
