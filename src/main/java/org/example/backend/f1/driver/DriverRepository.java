package org.example.backend.f1.driver;

import java.util.List;
import java.util.Optional;
import org.example.backend.f1.driver.entity.Driver;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    List<Driver> findAllByTeam(F1Team team);

    Optional<Driver> findByName(String driverName);

    List<Driver> findAllByNameIn(List<String> driverNames);
}
