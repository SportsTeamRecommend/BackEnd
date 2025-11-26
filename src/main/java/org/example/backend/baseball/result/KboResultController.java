package org.example.backend.baseball.result;

import lombok.RequiredArgsConstructor;
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

    @GetMapping("/kbo/team")
    public ResponseEntity<KboResultResponse> getTeam(@RequestParam(value = "name") String name){
        KboResultResponse kboResultResponse = kboResultService.getTeam(name);
        return ResponseEntity.ok(kboResultResponse);
    }
}
