package br.com.fiap.fintech.data;

import br.com.fiap.fintech.entity.BudgetSettingEntity;
import br.com.fiap.fintech.entity.TransactionEntity;
import br.com.fiap.fintech.entity.UserProfileEntity;
import br.com.fiap.fintech.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class FintechSeedDataLoader implements CommandLineRunner {

    private final UserProfileRepository userProfileRepository;

    public FintechSeedDataLoader(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void run(String... args) {
        // Seed primary user seed-user-001
        seedUser("seed-user-001", "Ellie Chen");
        // Also seed test_uuid for testing and frontend default
        seedUser("test_uuid", "Ellie Chen (Test)");
    }

    private void seedUser(String userId, String displayName) {
        if (userProfileRepository.existsById(userId)) {
            return;
        }

        UserProfileEntity profile = new UserProfileEntity(userId, displayName, "BRL");

        // Budgets
        profile.addBudget(new BudgetSettingEntity("dining", BigDecimal.valueOf(600.00), BigDecimal.valueOf(0.80)));
        profile.addBudget(new BudgetSettingEntity("groceries", BigDecimal.valueOf(800.00), BigDecimal.valueOf(0.80)));
        profile.addBudget(new BudgetSettingEntity("transport", BigDecimal.valueOf(300.00), BigDecimal.valueOf(0.80)));

        // Transactions: Dining
        int[] diningDays = {1, 2, 4, 5, 7, 9, 11, 12, 14, 15, 18, 20, 22, 23, 25, 26, 28, 29};
        for (int i = 0; i < diningDays.length; i++) {
            int day = diningDays[i];
            int amount = 85 + (day % 5) * 12;
            profile.addTransaction(new TransactionEntity(
                    1000 + i,
                    LocalDate.of(2026, 5, day),
                    BigDecimal.valueOf(amount),
                    "dining",
                    "Restaurant"
            ));
        }

        // Transactions: Groceries
        int[] groceriesDays = {3, 10, 17, 24, 31};
        for (int i = 0; i < groceriesDays.length; i++) {
            int day = groceriesDays[i];
            int amount = 180 + (day % 7) * 15;
            profile.addTransaction(new TransactionEntity(
                    2000 + i,
                    LocalDate.of(2026, 5, day),
                    BigDecimal.valueOf(amount),
                    "groceries",
                    "Grocery Store"
            ));
        }

        // Transactions: Transport
        int[] transportDays = {1, 8, 15, 22, 29};
        for (int i = 0; i < transportDays.length; i++) {
            int day = transportDays[i];
            profile.addTransaction(new TransactionEntity(
                    3000 + i,
                    LocalDate.of(2026, 5, day),
                    BigDecimal.valueOf(60.00),
                    "transport",
                    "Metro Pass"
            ));
        }

        userProfileRepository.save(profile);
    }
}
