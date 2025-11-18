package org.example.backend.f1.team;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.weight.WeightCalculator;
import org.example.backend.f1.driver.Driver;
import org.example.backend.f1.driver.DriverRepository;
import org.example.backend.f1.driver.DriverResponse;
import org.example.backend.f1.table.DriverMetrics;
import org.example.backend.f1.table.F1Metrics;
import org.example.backend.f1.table.F1Weight;
import org.example.backend.f1.weight.F1WeightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class F1TeamService {

    private final F1MetricsRepository f1MetricsRepository;
    private final F1WeightRepository weightRepository;
    private final WeightCalculator calculator;

    private final F1TeamRepository teamRepository;
    private final DriverRepository driverRepository;
    private final DriverMetricsRepository driverMetricsRepository;

    /**
     * 단일 팀의 F1Weight 생성 또는 업데이트
     */
    @Transactional
    public void calculateTeamWeights() {
        // 전 팀 데이터 가져오기
        List<F1Metrics> f1Metrics = f1MetricsRepository.findAll();

        // 전체 팀 기준 min/max 계산
        int minTeamRec = f1MetricsRepository.findMinTeamRecord();
        int maxTeamRec = f1MetricsRepository.findMaxTeamRecord();

        int minDriverAvg = driverMetricsRepository.findMinDriverRecord();
        int maxDriverAvg = driverMetricsRepository.findMaxDriverRecord();

        int minYears = f1MetricsRepository.findMinLegacy();
        int maxYears = f1MetricsRepository.findMaxLegacy();

        int minTenure = driverMetricsRepository.findMinTenure();
        int maxTenure = driverMetricsRepository.findMaxTenure();

        double minIncrease = f1MetricsRepository.findMinUnderdog();
        double maxIncrease = f1MetricsRepository.findMaxUnderdog();

        double minFan = f1MetricsRepository.findMinFandom();
        double maxFan = f1MetricsRepository.findMaxFandom();

        // 팀별 계산
        for (F1Metrics metrics : f1Metrics) {

            DriverMetrics d1 = metrics.getDriver1();
            DriverMetrics d2 = metrics.getDriver2();

            if (d1 == null || d2 == null) continue;

            double teamRecord =
                    calculator.calculateF1TeamRecord(metrics.getTeamRecord(), minTeamRec, maxTeamRec);

            double driverRecord =
                    calculator.calculateF1DriverRecord(
                            Math.max(d1.getRecord(), d2.getRecord()),
                            minDriverAvg, maxDriverAvg
                    );

            double heritage =
                    calculator.calculateF1Legacy(metrics.getLegacy(), minYears, maxYears);

            double f1 = d1.getIsFranchise()
                    ? calculator.calculateF1FranchiseStar(d1.getTenure(), minTenure, maxTenure)
                    : 0.0;

            double f2 = d2.getIsFranchise()
                    ? calculator.calculateF1FranchiseStar(d2.getTenure(), minTenure, maxTenure)
                    : 0.0;

            double franchiseStar = (f1 + f2) / 2.0;

            double ratio = (double) metrics.getCurrentYearPoint() / metrics.getLastYearPoint();
            double increase = Math.sqrt(ratio);
            double underdog = calculator.calculateF1Underdog(increase, minIncrease, maxIncrease);

            double fandom = calculator.calculateF1Fandom(metrics.getFandom(), minFan, maxFan);

            F1Weight weight = weightRepository.findByMetrics(metrics)
                    .orElse(new F1Weight());

            weight.setMetrics(metrics);
            weight.setTeamRecord(teamRecord);
            weight.setDriverRecord(driverRecord);
            weight.setLegacy(heritage);
            weight.setFranchiseStar(franchiseStar);
            weight.setUnderdog(underdog);
            weight.setFandom(fandom);

            weightRepository.save(weight);
        }
    }

    /**
     * 팀 정보 조회
     */
    public F1TeamResponse getF1Team(String name) {
        F1Team team = teamRepository.findByName(name);
        List<Driver> drivers = driverRepository.findAllByTeam(team);
        List<DriverResponse> driverResponses = drivers.stream().map(d -> new DriverResponse(d.getName(), d.getDateOfBirth(), d.getImageUrl(), d.getNationality(), d.getDebutYear(), d.getSeasonPolls(), d.getSeasonPosition(), d.getSeasonPoint(), d.getSeasonWins(), d.getSeasonPodiums(), d.getCareerWins(), d.getCareerPodiums(), d.getWorldChampionship())).toList();

        return new F1TeamResponse(team.getName(), team.getImageUrl(), team.getVideoUrl(), team.getSeasonPosition(), team.getSeasonPoint(), team.getSeasonWins(), team.getSeasonPodiums(), team.getTotalWins(), team.getTotalPodiums(), team.getConstructorChampionship(), driverResponses);
    }
}
