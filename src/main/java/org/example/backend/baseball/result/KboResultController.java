package org.example.backend.baseball.result;

import lombok.RequiredArgsConstructor;
import org.example.backend.f1.team.F1TeamResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KboResultController {
    private final KboResultService kboResultService;

    @GetMapping("/team")
    public ResponseEntity<F1TeamResponse> getTeam(@RequestParam(value = "name") String name){
        F1TeamResponse f1TeamResponse = f1TeamService.getF1Team(name);
        return ResponseEntity.ok(f1TeamResponse);
    }
}
