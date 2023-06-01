package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping
public class AccountController {
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final TransferDao transferDao;

    public AccountController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @GetMapping("/account")
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @GetMapping("/account/{accountId}")
    public Account getAccountByAccountId(@PathVariable Integer accountId) {
        return accountDao.getAccountById(accountId);
    }

    @GetMapping("/account/user/{userId}")
    public Account getAccountByUserId(@PathVariable Integer userId) {
        return accountDao.getAccountByUserId(userId);
    }

    @GetMapping("/balance")
    public BigDecimal getAccountBalance(Principal principal) {
        return accountDao.getBalance(userDao.findIdByUsername(principal.getName()));
    }

    @PutMapping("/balance/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountBalanceById(@PathVariable Integer accountId,
                                         @RequestBody Account account) {
        accountDao.updateBalance(account);
    }

}
