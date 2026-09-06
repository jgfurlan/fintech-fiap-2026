package br.com.fiap.fintech.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "supabase")
public record FintechSupabaseProperties(
        String url,
        String anonKey
) {
    public boolean isConfigured() {
        return url != null && !url.isBlank() && !url.equals("http://placeholder");
    }
}
