package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAllAccounts();

    Account getAccountById(Integer accountId);

    Account getAccountByUserId(Integer userId);

    BigDecimal getBalance(Integer userId);

    void updateBalance(Account account);

}


