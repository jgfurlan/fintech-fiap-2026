package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.BudgetInsightResponse;
import br.com.fiap.fintech.dto.UserResponse;
import br.com.fiap.fintech.service.FintechAgentCoordinator;
import br.com.fiap.fintech.service.FintechAuthService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/agent")
public class FintechAgentController {

    private final FintechAuthService authService;
    private final FintechAgentCoordinator coordinator;

    public FintechAgentController(
            FintechAuthService authService,
            FintechAgentCoordinator coordinator
    ) {
        this.authService = authService;
        this.coordinator = coordinator;
    }

    @GetMapping("/budget-insight")
    public ResponseEntity<BudgetInsightResponse> getBudgetInsight(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        UserResponse user = authService.getAuthenticatedUser(authorization);
        LocalDate targetDate = date != null ? date : LocalDate.now();
        BudgetInsightResponse response = coordinator.analyzeBudget(user.id(), targetDate);
        return ResponseEntity.ok(response);
    }
}
