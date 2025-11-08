package org.example.backend.f1.team;

import java.util.List;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface F1TeamRepository extends JpaRepository<F1Team, Integer> {
    F1Team findByName(String name);

    List<F1Team> findAllByNameIn(List<String> teamNames);
}
