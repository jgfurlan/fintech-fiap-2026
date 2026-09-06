package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.ActionItemDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FintechBedrockService {

    private static final Logger log = LoggerFactory.getLogger(FintechBedrockService.class);
    private static final String MODEL_ID = "anthropic.claude-3-sonnet-20240229-v1:0";

    private final String region;
    private final ObjectMapper objectMapper;
    private final FintechBudgetVelocityWorker worker;
    private BedrockRuntimeClient bedrockClient;

    public FintechBedrockService(
            @Value("${aws.region:us-east-1}") String region,
            ObjectMapper objectMapper,
            FintechBudgetVelocityWorker worker
    ) {
        this.region = region;
        this.objectMapper = objectMapper;
        this.worker = worker;
        try {
            this.bedrockClient = BedrockRuntimeClient.builder()
                    .region(Region.of(region))
                    .build();
        } catch (Exception e) {
            log.info("AWS Bedrock client initialized without direct credentials. Fallback mode enabled.");
            this.bedrockClient = null;
        }
    }

    public List<ActionItemDto> getBudgetInsight(String category, BigDecimal deviation, BigDecimal totalSpent) {
        if (bedrockClient == null) {
            return fallback(category, deviation);
        }

        String prompt = String.format("""
            You are a financial advisor AI. A user has exceeded their budget in the '%s' category.
            
            Details:
            - Category: %s
            - Total Spent So Far: $%s
            - Deviation from Budget: $%s
            
            Generate exactly 2 actionable, concrete suggestions for the user to save money and get back on track.
            Each suggestion must include:
            1. The specific action.
            2. Estimated projected savings (as a number).
            3. The reason why this helps.
            
            Return the response ONLY as a JSON list of objects with keys: "action", "projected_savings", "reason".
            """, category, category, totalSpent, deviation);

        try {
            Map<String, Object> bodyMap = Map.of(
                    "anthropic_version", "bedrock-2023-05-31",
                    "max_tokens", 500,
                    "messages", List.of(Map.of("role", "user", "content", prompt))
            );

            String requestBody = objectMapper.writeValueAsString(bodyMap);

            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId(MODEL_ID)
                    .contentType("application/json")
                    .accept("application/json")
                    .body(SdkBytes.fromUtf8String(requestBody))
                    .build();

            InvokeModelResponse response = bedrockClient.invokeModel(request);
            String responseBody = response.body().asString(StandardCharsets.UTF_8);

            JsonNode root = objectMapper.readTree(responseBody);
            String text = root.path("content").get(0).path("text").asText();

            List<Map<String, Object>> items = objectMapper.readValue(text, new TypeReference<>() {});
            List<ActionItemDto> result = new ArrayList<>();
            for (Map<String, Object> item : items) {
                String action = (String) item.get("action");
                Object savingsObj = item.get("projected_savings");
                BigDecimal savings = new BigDecimal(String.valueOf(savingsObj)).setScale(2, RoundingMode.HALF_UP);
                String reason = (String) item.get("reason");
                result.add(new ActionItemDto(action, savings, reason));
            }
            return result;
        } catch (Exception e) {
            log.warn("Error calling AWS Bedrock, falling back to heuristic action items: {}", e.getMessage());
            return fallback(category, deviation);
        }
    }

    private List<ActionItemDto> fallback(String category, BigDecimal deviation) {
        return worker.getFallbackActionItems(category, deviation);
    }
}
