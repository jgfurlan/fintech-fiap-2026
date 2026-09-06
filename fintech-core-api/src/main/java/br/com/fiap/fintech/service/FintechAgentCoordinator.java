package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.ActionItemDto;
import br.com.fiap.fintech.dto.BudgetInsightResponse;
import br.com.fiap.fintech.entity.UserProfileEntity;
import br.com.fiap.fintech.exception.FintechBadRequestException;
import br.com.fiap.fintech.exception.FintechNotFoundException;
import br.com.fiap.fintech.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FintechAgentCoordinator {

    private final UserProfileRepository userProfileRepository;
    private final FintechBudgetVelocityWorker worker;
    private final FintechBedrockService bedrockService;

    public FintechAgentCoordinator(
            UserProfileRepository userProfileRepository,
            FintechBudgetVelocityWorker worker,
            FintechBedrockService bedrockService
    ) {
        this.userProfileRepository = userProfileRepository;
        this.worker = worker;
        this.bedrockService = bedrockService;
    }

    public BudgetInsightResponse analyzeBudget(String userId) {
        return analyzeBudget(userId, LocalDate.now());
    }

    public BudgetInsightResponse analyzeBudget(String userId, LocalDate reportDate) {
        UserProfileEntity profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new FintechNotFoundException("No seed profile found for user_id=" + userId));

        if (profile.getTransactions() == null || profile.getTransactions().isEmpty()) {
            throw new FintechBadRequestException("User " + userId + " has no transaction data to analyze.");
        }

        FintechBudgetVelocityWorker.WorkerResult result = worker.calculateInsight(
                profile.getTransactions(),
                profile.getBudgets(),
                reportDate
        );

        List<ActionItemDto> actionItems = new ArrayList<>();
        if (result.worstDeviation().compareTo(BigDecimal.ZERO) > 0) {
            String category = result.worstCategory().isEmpty() ? "general" : result.worstCategory();
            BigDecimal totalSpent = result.worstProjectedTotal().subtract(result.worstDeviation());
            actionItems = bedrockService.getBudgetInsight(category, result.worstDeviation(), totalSpent);
        }

        return new BudgetInsightResponse(
                userId,
                reportDate,
                profile.getCurrency() != null ? profile.getCurrency() : "BRL",
                result.worstProjectedTotal(),
                result.budgetTotal(),
                result.worstDeviationPct(),
                result.worstDeviation(),
                result.threatLevel().getValue(),
                actionItems,
                Instant.now()
        );
    }
}
