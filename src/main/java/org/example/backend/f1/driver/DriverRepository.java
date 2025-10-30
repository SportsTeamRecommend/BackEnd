package org.example.backend.f1.driver;

import java.util.List;
import org.example.backend.baseball.team.Team;
import org.example.backend.f1.driver.entity.Driver;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    List<Driver> findAllByTeam(F1Team team);
}
