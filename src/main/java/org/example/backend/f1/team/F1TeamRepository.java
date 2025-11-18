package org.example.backend.f1.team;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface F1TeamRepository extends JpaRepository<F1Team, Integer> {
    F1Team findByTeamName(String name);

    List<F1Team> findAllByTeamNameIn(List<String> teamNames);
}
