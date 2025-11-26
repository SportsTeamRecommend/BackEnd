package org.example.backend.baseball.result;

import java.util.List;
import org.example.backend.baseball.table.KboPlayerInfo;
import org.example.backend.baseball.table.KboTeamInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KboPlayerInfoRepository extends JpaRepository<KboPlayerInfo, Integer> {
    List<KboPlayerInfo> findAllByTeam(KboTeamInfo team);
}
