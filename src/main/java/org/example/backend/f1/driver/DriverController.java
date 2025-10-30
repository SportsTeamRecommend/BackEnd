package org.example.backend.f1.driver;

import lombok.RequiredArgsConstructor;
import org.example.backend.f1.team.F1TeamService;
import org.example.backend.f1.team.dto.F1TeamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DriverController {

}
