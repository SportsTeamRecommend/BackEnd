package org.example.backend.f1.team;

import java.util.List;
import org.example.backend.f1.driver.DriverRepository;
import org.example.backend.f1.driver.DriverResponse;
import org.example.backend.f1.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class F1TeamService {

    F1TeamRepository f1TeamRepository;
    DriverRepository driverRepository;

    @Autowired
    public F1TeamService(F1TeamRepository f1TeamRepository, DriverRepository driverRepository) {
        this.f1TeamRepository = f1TeamRepository;
        this.driverRepository = driverRepository;
    }

    public F1TeamResponse getF1Team(String name) {
        F1Team f1Team = f1TeamRepository.findByName(name);
        List<Driver> drivers = driverRepository.findAllByTeam(f1Team);

        List<DriverResponse> driverResponses = drivers.stream()
                .map(driver -> new DriverResponse(
                        driver.getName(),
                        driver.getDateOfBirth(),
                        driver.getImageUrl(),
                        driver.getNationality(),
                        driver.getDebutYear(),
                        driver.getSeasonPolls(),
                        driver.getSeasonPosition(),
                        driver.getSeasonPoint(),
                        driver.getSeasonWins(),
                        driver.getSeasonPodiums(),
                        driver.getCareerWins(),
                        driver.getCareerPodiums(),
                        driver.getWorldChampionship()
                )).toList();

        return new F1TeamResponse(
                f1Team.getName(),
                f1Team.getImageUrl(),
                f1Team.getVideoUrl(),
                f1Team.getSeasonPosition(),
                f1Team.getSeasonPoint(),
                f1Team.getSeasonWins(),
                f1Team.getSeasonPodiums(),
                f1Team.getTotalWins(),
                f1Team.getTotalPodiums(),
                f1Team.getConstructorChampionship(),
                driverResponses
        );
    }

}
