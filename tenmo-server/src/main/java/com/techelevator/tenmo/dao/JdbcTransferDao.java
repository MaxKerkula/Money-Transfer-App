package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfers(Integer userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id," +
                "t.account_from, t.account_to, t.amount " +
                "FROM transfer t " +
                "JOIN account a ON t.account_from = a.account_id OR t.account_to = a.account_id " +
                "WHERE a.user_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,  userId);
        while (rows.next()) {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(Integer userId, Integer transferId) {
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id,\n" +
                "t.account_from, t.account_to, t.amount\n" +
                "FROM transfer t\n" +
                "JOIN account a ON t.account_from = a.account_id OR t.account_to = a.account_id \n" +
                "WHERE a.user_id = ? AND t.transfer_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId, transferId);
        if (rows.next()) {
            return mapRowToTransfer(rows);
        }
        return null;
    }

    @Override
    public Transfer createTransfer(Integer userId, Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
         Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransfer_type_id(), transfer.getTransfer_status_id(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
        return getTransferById(userId, transferId);
    }

    @Override
    public void updateTransferStatus(Integer transferId, TransferStatus newStatus) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, newStatus.getTransferStatusId(), transferId);
    }

    @Override
    public List<Transfer> getPendingTransfers(Integer userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount\n" +
                "FROM transfer t\n" +
                "JOIN account a ON t.account_from = a.account_id OR t.account_to = a.account_id\n" +
                "WHERE a.user_id = ? AND t.transfer_status_id = 1;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId);
        while (rows.next()) {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public String getAccountFromUsername(Integer userId) {
        String sql = "SELECT username " +
                "FROM tenmo_user " +
                "WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(sql, String.class, userId);
    }

    @Override
    public String getTransferTypeById(Integer transferTypeId) {
        String sql = "SELECT transfer_type_desc FROM transfer_type WHERE transfer_type_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, transferTypeId);
    }

    @Override
    public String getTransferStatusById(Integer transferStatusId) {
        String sql = "SELECT transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, transferStatusId);
    }

    private final Account mapRowToAccount(SqlRowSet rowSet) {
            Account account = new Account();
            account.setAccountId(rowSet.getInt("account_id"));
            account.setUserId(rowSet.getInt("user_id"));
            account.setBalance(rowSet.getBigDecimal("balance"));
            return account;
        }

    private final TransferType mapRowToTransferType(SqlRowSet rowSet) {
            TransferType transferType = new TransferType();
            transferType.setTransferTypeId(rowSet.getInt("transfer_type_id"));
            transferType.setTransferTypeDesc(rowSet.getString("transfer_type_desc"));
            return transferType;
        }

    private final TransferStatus mapRowToTransferStatus(SqlRowSet rowSet){
            TransferStatus transferStatus = new TransferStatus();
            transferStatus.setTransferStatusId(rowSet.getInt("transfer_status_id"));
            transferStatus.setTransferStatusDesc(rowSet.getString("transfer_status_desc"));
            return transferStatus;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_Id"));
        transfer.setAccount_from(rowSet.getInt("account_From"));
        transfer.setAccount_to(rowSet.getInt("account_To"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        return transfer;
    }

}












