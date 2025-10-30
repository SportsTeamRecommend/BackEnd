package org.example.backend.f1.statistics;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface F1StatisticsRepository extends JpaRepository<F1Statistics, Integer> {

    List<F1Statistics> findAllByOrderByRecommendedDesc();
}
