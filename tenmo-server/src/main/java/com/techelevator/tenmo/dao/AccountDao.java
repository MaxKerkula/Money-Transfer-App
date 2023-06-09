package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

    Account getAccountByUserId(Integer userId);

    void updateBalance(Integer userId, Double amount);

    Double getBalance(Integer userId);
}
