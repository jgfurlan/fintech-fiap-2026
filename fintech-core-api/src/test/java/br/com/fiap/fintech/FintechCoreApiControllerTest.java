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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FintechCoreApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FintechAuthService authService;

    @Test
    @DisplayName("GET /api returns 200 with service message and version")
    void testRootEndpoint() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Fintech FIAP 2026 Core API"))
                .andExpect(jsonPath("$.version").value("0.1.0"));
    }

    @Test
    @DisplayName("GET /api/health returns 200 with status ok and supabase reachability")
    void testHealthEndpoint() throws Exception {
        when(authService.checkSupabaseHealth())
                .thenReturn(new br.com.fiap.fintech.dto.SupabaseHealth(true, null, true));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.supabase").exists())
                .andExpect(jsonPath("$.supabase.reachable").value(true));
    }

    @Test
    @DisplayName("GET /api/me without Authorization header returns 401 with missing header message")
    void testMeWithoutAuthHeader() throws Exception {
        when(authService.getAuthenticatedUser(null))
                .thenThrow(new FintechAuthException("Missing Authorization header"));

        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Missing Authorization header"));
    }

    @Test
    @DisplayName("GET /api/me with invalid token returns 401 with detail")
    void testMeWithInvalidToken() throws Exception {
        when(authService.getAuthenticatedUser("Bearer invalid_token"))
                .thenThrow(new FintechAuthException("Invalid token (Supabase returned 401)"));

        mockMvc.perform(get("/api/me").header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Invalid token (Supabase returned 401)"));
    }

    @Test
    @DisplayName("GET /api/me with valid token returns 200 with user id and email")
    void testMeWithValidToken() throws Exception {
        when(authService.getAuthenticatedUser("Bearer valid_token"))
                .thenReturn(new UserResponse("test_uuid", "test@example.com"));

        mockMvc.perform(get("/api/me").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test_uuid"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
