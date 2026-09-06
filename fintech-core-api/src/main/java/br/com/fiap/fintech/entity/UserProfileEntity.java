package br.com.fiap.fintech.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "currency", nullable = false)
    private String currency = "BRL";

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TransactionEntity> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BudgetSettingEntity> budgets = new ArrayList<>();

    public UserProfileEntity() {}

    public UserProfileEntity(String userId, String displayName, String currency) {
        this.userId = userId;
        this.displayName = displayName;
        this.currency = currency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public List<BudgetSettingEntity> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<BudgetSettingEntity> budgets) {
        this.budgets = budgets;
    }

    public void addTransaction(TransactionEntity transaction) {
        transactions.add(transaction);
        transaction.setUserProfile(this);
    }

    public void addBudget(BudgetSettingEntity budget) {
        budgets.add(budget);
        budget.setUserProfile(this);
    }
}
