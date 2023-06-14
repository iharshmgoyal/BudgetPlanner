package com.example.budgetplanner.Models;

public class AccountDetails {
    String accountName, lastBalance, lastUpdatedAt;

    public AccountDetails(String accountName, String lastBalance, String lastUpdatedAt) {
        this.accountName = accountName;
        this.lastBalance = lastBalance;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(String lastBalance) {
        this.lastBalance = lastBalance;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
