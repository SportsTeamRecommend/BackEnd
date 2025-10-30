package org.example.backend.f1.team;

import lombok.RequiredArgsConstructor;
import org.example.backend.baseball.team.Team;
import org.example.backend.f1.team.dto.F1TeamResponse;
import org.example.backend.f1.team.entity.F1Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class F1TeamController {

    F1TeamService f1TeamService;

    @Autowired
    public F1TeamController(F1TeamService f1TeamService) {
        this.f1TeamService = f1TeamService;
    }

    @GetMapping("/f1/team")
    public ResponseEntity<F1TeamResponse> getTeam(@RequestParam(value = "name") String name){
        F1TeamResponse f1TeamResponse = f1TeamService.getF1Team(name);
        return ResponseEntity.ok(f1TeamResponse);
    }

}

