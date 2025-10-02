package org.example.backend.baseball.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    @Transactional // dirty checking 용
    public void updateTeamRank() {
        var teams = teamRepository.findAll();

        for (Team team : teams) {
            if (team.getPastAvgRank() != null && team.getCurrentRank() != null) {
                double avg = (team.getPastAvgRank() * 2 + team.getCurrentRank()) / 3.0;
                team.setAvgRank(avg);
            }
        }
    }
}
