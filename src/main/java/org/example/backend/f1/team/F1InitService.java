package org.example.backend.f1.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.f1.driver.Driver;
import org.example.backend.f1.driver.DriverRepository;
import org.example.backend.f1.table.DriverMetrics;
import org.example.backend.f1.table.F1Metrics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class F1InitService {

    private final F1TeamRepository f1TeamRepository;
    private final DriverRepository driverRepository;
    private final DriverMetricsRepository driverMetricsRepository;
    private final F1MetricsRepository f1MetricsRepository;

    @Transactional
    public void buildMetrics() {

        /*
         * 1) DriverMetrics 업데이트
         */
        driverRepository.findAll().forEach(driver -> {

            DriverMetrics dm = driverMetricsRepository
                    .findById(driver.getId().longValue())
                    .orElse(DriverMetrics.builder()
                            .id(driver.getId().longValue())
                            .name(driver.getName())
                            .build()
                    );

            dm.update(
                    driver.getSeasonPoint(),
                    null,   // 프랜차이즈 여부
                    null    // 연차
            );

            driverMetricsRepository.save(dm);
        });


        /*
         * 2) F1Metrics 업데이트
         */
        f1TeamRepository.findAll().forEach(team -> {

            List<Driver> teamDrivers = driverRepository.findAllByTeam(team);

            DriverMetrics m1 = null;
            DriverMetrics m2 = null;

            if (teamDrivers.size() >= 1) {
                m1 = driverMetricsRepository.findById(teamDrivers.get(0).getId().longValue()).orElse(null);
            }
            if (teamDrivers.size() >= 2) {
                m2 = driverMetricsRepository.findById(teamDrivers.get(1).getId().longValue()).orElse(null);
            }

            F1Metrics metrics = f1MetricsRepository
                    .findByName(team.getTeamName())
                    .orElse(F1Metrics.builder()
                            .name(team.getTeamName())
                            .build()
                    );

            metrics.update(
                    team.getAvgRank(),            // rank
                    team.getSeasonPoint(),        // TODO : 누적 포인트로 수정
                    m1,
                    m2,
                    null,                         // legacy
                    null,                         // last year
                    null,                         // fandom
                    team.getSeasonPoint()         // current year
            );

            f1MetricsRepository.save(metrics);
        });
    }
}
