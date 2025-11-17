package org.example.backend.baseball.team;

import org.example.backend.baseball.table.KboRegionDistance;
import org.example.backend.baseball.table.KboTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegionDistanceRepository extends JpaRepository<KboRegionDistance, Long> {

    @Query("SELECT MAX(r.distanceKm) FROM KboRegionDistance r")
    Double findMaxDistanceKm();

    @Query("SELECT r FROM KboRegionDistance r WHERE r.region = :region AND r.kboTeam = :team")
    Optional<KboRegionDistance> findByRegionAndTeam(
            @Param("region") Region region,
            @Param("team") KboTeam kboTeam
    );
}
