package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT account_id, user_id, balance " +
                "FROM account;");
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public Account getAccountById(Integer accountId) {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()) {
            return mapRowToAccount(rowSet);
        } else {
            return null;
        }
    }

    @Override
    public Account getAccountByUserId(Integer userId) {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (rowSet.next()) {
            return mapRowToAccount(rowSet);
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getBalance(Integer userId) {
        String sql = "SELECT balance " +
                "FROM account " +
                "WHERE user_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }

    @Override
    public void updateBalance(Account account) {
        String sql = "UPDATE account " +
                "SET balance = ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, account.getBalance(), account.getAccountId());
    }

    @Override
public void transferTEBucks(int fromAccountId, int toAccountId, BigDecimal amount) {
    String sql = "BEGIN TRANSACTION; " +
                 "UPDATE accounts SET balance = balance - ? WHERE account_id = ?; " +
                 "UPDATE accounts SET balance = balance + ? WHERE account_id = ?; " +
                 "COMMIT;";
    jdbcTemplate.update(sql, amount, fromAccountId, amount, toAccountId);
}

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
