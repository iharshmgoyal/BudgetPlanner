package com.example.budgetplanner.Models;

public class Transaction {
    String transactionName, transactionReason, transactionDescription, transactionType, transactionAccount, transactionAmount, userID, lastBalance;
    long transactionCreatedAt;

    public Transaction() {

    }

    public Transaction(String transactionName, String transactionReason, String transactionDescription, String transactionType, String transactionAccount, String transactionAmount, String userID, String lastBalance, long transactionCreatedAt) {
        this.transactionName = transactionName;
        this.transactionReason = transactionReason;
        this.transactionDescription = transactionDescription;
        this.transactionType = transactionType;
        this.transactionAccount = transactionAccount;
        this.transactionAmount = transactionAmount;
        this.userID = userID;
        this.lastBalance = lastBalance;
        this.transactionCreatedAt = transactionCreatedAt;
    }

    public String getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(String lastBalance) {
        this.lastBalance = lastBalance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTransactionReason() {
        return transactionReason;
    }

    public void setTransactionReason(String transactionReason) {
        this.transactionReason = transactionReason;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionAccount() {
        return transactionAccount;
    }

    public void setTransactionAccount(String transactionAccount) {
        this.transactionAccount = transactionAccount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public long getTransactionCreatedAt() {
        return transactionCreatedAt;
    }

    public void setTransactionCreatedAt(long transactionCreatedAt) {
        this.transactionCreatedAt = transactionCreatedAt;
    }
}
