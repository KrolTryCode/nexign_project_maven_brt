package nexign_project_maven.brt.repository;

import nexign_project_maven.brt.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff, Integer> {
}
