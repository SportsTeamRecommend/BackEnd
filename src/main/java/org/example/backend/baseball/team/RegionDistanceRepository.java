package org.example.backend.baseball.team;

import org.example.backend.baseball.table.RegionDistance;
import org.example.backend.baseball.table.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegionDistanceRepository extends JpaRepository<RegionDistance, Long> {

    @Query("SELECT MAX(r.distanceKm) FROM RegionDistance r")
    Double findMaxDistanceKm();

    @Query("SELECT r FROM RegionDistance r WHERE r.region = :region AND r.team = :team")
    Optional<RegionDistance> findByRegionAndTeam(
            @Param("region") Region region,
            @Param("team") Team team
    );
}
