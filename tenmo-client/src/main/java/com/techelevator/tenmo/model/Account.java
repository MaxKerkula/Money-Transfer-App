package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private Integer accountId;
    private Integer userId;
    private BigDecimal balance;

    public Account() {
    }

    public Account(Integer accountId, Integer userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getUserId() {
        return userId;  //account.getUserId(rowSet.getInt("user_id"));
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getAccountId().equals(account.getAccountId()) && getUserId().equals(account.getUserId()) && getBalance().equals(account.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountId(), getUserId(), getBalance());
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';

    }
}
