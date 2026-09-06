package br.com.fiap.fintech;

import br.com.fiap.fintech.dto.UserResponse;
import br.com.fiap.fintech.exception.FintechAuthException;
import br.com.fiap.fintech.service.FintechAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FintechAgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FintechAuthService authService;

    @Test
    @DisplayName("GET /api/agent/budget-insight without auth header returns 401")
    void testInsightWithoutAuth() throws Exception {
        when(authService.getAuthenticatedUser(null))
                .thenThrow(new FintechAuthException("Missing Authorization header"));

        mockMvc.perform(get("/api/agent/budget-insight"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Missing Authorization header"));
    }

    @Test
    @DisplayName("GET /api/agent/budget-insight with valid token returns complete BudgetInsightResponse")
    void testInsightWithValidToken() throws Exception {
        when(authService.getAuthenticatedUser("Bearer valid_token"))
                .thenReturn(new UserResponse("seed-user-001", "ellie.chen@example.com"));

        mockMvc.perform(get("/api/agent/budget-insight")
                        .param("date", "2026-05-31")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value("seed-user-001"))
                .andExpect(jsonPath("$.report_date").value("2026-05-31"))
                .andExpect(jsonPath("$.currency").value("BRL"))
                .andExpect(jsonPath("$.threat_level").exists())
                .andExpect(jsonPath("$.projected_month_end_total").isNumber())
                .andExpect(jsonPath("$.budget_total").isNumber())
                .andExpect(jsonPath("$.deviation_amount").isNumber())
                .andExpect(jsonPath("$.action_items").isArray());
    }
}
