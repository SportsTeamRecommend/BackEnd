package org.example.backend.f1.statistics;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.backend.f1.team.F1TeamRepository;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class F1StatisticsInitRunner implements CommandLineRunner {

    private final F1TeamRepository teamRepository;
    private final F1StatisticsRepository teamStatsRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<F1Team> allTeams = teamRepository.findAll();

        for (F1Team team : allTeams) {
            if (!teamStatsRepository.existsById(team.getId())) {
                F1Statistics newStat = new F1Statistics(team, 0.0, 0L, 0L);
                teamStatsRepository.save(newStat);
                System.out.println(team.getName() + " 팀의 통계 데이터를 새로 생성했습니다.");
            }
        }
    }

}
