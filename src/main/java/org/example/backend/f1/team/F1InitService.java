package org.example.backend.f1.team;

import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
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

        // 1) 드라이버 메트릭스 초기화
        driverMetricsRepository.deleteAll();
        driverRepository.findAll().forEach(driver -> {

            DriverMetrics dm = new DriverMetrics(
                    driver.getId().longValue(),
                    driver.getName(),
                    driver.getSeasonPoint(),
                    null,               // 프차 여부
                    null                         // 연차
            );

            driverMetricsRepository.save(dm);
        });

        // 2) 팀 메트릭스 초기화
        f1MetricsRepository.deleteAll();
        f1TeamRepository.findAll().forEach(team -> {

            // 해당 팀 드라이버 2명 가져오기
            List<Driver> teamDrivers = driverRepository.findAllByTeam(team);

            DriverMetrics m1 = null;
            DriverMetrics m2 = null;

            if (teamDrivers.size() >= 1) {
                m1 = driverMetricsRepository.findById(teamDrivers.get(0).getId().longValue()).orElse(null);
            }
            if (teamDrivers.size() >= 2) {
                m2 = driverMetricsRepository.findById(teamDrivers.get(1).getId().longValue()).orElse(null);
            }

            F1Metrics metrics = new F1Metrics(
                    null,
                    team.getName(),
                    team.getSeasonPoint(),   // TODO : 팀 누적 성적으로 수정
                    m1,
                    m2,
                    null,             // 근본
                    null,                   // 언더독
                    null,                    // 팬덤
                    team.getSeasonPoint()   // 팀 올해 성적
            );

            f1MetricsRepository.save(metrics);
        });
    }
}
