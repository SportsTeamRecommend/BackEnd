package org.example.backend.baseball.result;

import org.example.backend.baseball.table.KboPlayerInfo;
import org.example.backend.baseball.table.KboTeamInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KboTeamInfoRepository extends JpaRepository<KboTeamInfo, Integer> {
    KboTeamInfo findByName(String name);
}
