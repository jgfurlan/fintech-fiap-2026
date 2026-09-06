package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.HealthResponse;
import br.com.fiap.fintech.dto.RootResponse;
import br.com.fiap.fintech.dto.SupabaseHealth;
import br.com.fiap.fintech.dto.UserResponse;
import br.com.fiap.fintech.service.FintechAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FintechCoreApiController {

    private final FintechAuthService authService;

    public FintechCoreApiController(FintechAuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<RootResponse> root() {
        return ResponseEntity.ok(new RootResponse("Fintech FIAP 2026 Core API", "0.1.0"));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        SupabaseHealth supabaseHealth = authService.checkSupabaseHealth();
        return ResponseEntity.ok(new HealthResponse("ok", supabaseHealth));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        UserResponse user = authService.getAuthenticatedUser(authorization);
        return ResponseEntity.ok(user);
    }
}
