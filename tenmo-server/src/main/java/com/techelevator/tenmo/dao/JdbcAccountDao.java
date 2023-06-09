package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDAO implements AccountDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(Integer userId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

    @Override
    public void updateBalance(Integer userId, Double amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, amount, userId);
    }

    @Override
    public Double getBalance(Integer userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()) {
            return results.getDouble("balance");
        }
        return null;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}
