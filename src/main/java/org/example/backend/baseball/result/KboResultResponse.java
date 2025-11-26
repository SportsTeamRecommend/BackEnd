package org.example.backend.baseball.result;

import java.util.List;
import org.example.backend.baseball.table.KboTeamInfo;

public record KboResultResponse(
        long id,
        String name,
        String description,
        int leagueWins,
        int koreaSeasonWins,
        int postSeason,
        double avgRank,
        int seasonRank,
        double winRate,
        double battingAverage,
        double earnedRunAverage,
        List<KboPlayerResponse> players
) {
    public KboResultResponse(KboTeamInfo kboTeamInfo, List<KboPlayerResponse> kboPlayerResponses) {
        this(
                kboTeamInfo.getId(),
                kboTeamInfo.getName(),
                kboTeamInfo.getDescription(),
                kboTeamInfo.getLeagueWins(),
                kboTeamInfo.getKoreaSeasonWins(),
                kboTeamInfo.getPostSeason(),
                kboTeamInfo.getAvgRank(),
                kboTeamInfo.getSeasonRank(),
                kboTeamInfo.getWinRate(),
                kboTeamInfo.getBattingAverage(),
                kboTeamInfo.getEarnedRunAverage(),
                kboPlayerResponses
        );
    }
}
