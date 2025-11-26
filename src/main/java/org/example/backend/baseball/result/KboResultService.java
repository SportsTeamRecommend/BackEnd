package org.example.backend.baseball.result;

import java.util.List;
import lombok.AllArgsConstructor;
import org.example.backend.baseball.table.KboPlayerInfo;
import org.example.backend.baseball.table.KboTeamInfo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KboResultService {
    private final KboTeamInfoRepository kboTeamInfoRepository;
    private final KboPlayerInfoRepository kboPlayerInfoRepository;

    public KboResultResponse getTeam(String name) {
        KboTeamInfo team = kboTeamInfoRepository.findByName(name);
        List<KboPlayerInfo> players = kboPlayerInfoRepository.findAllByTeam(team);
        List<KboPlayerResponse> playerResponses =
                players.stream().map(p -> new KboPlayerResponse(p)).toList();

        return new KboResultResponse(team, playerResponses);
    }
}
