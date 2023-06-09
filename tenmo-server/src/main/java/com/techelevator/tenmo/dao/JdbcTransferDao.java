package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {

    List<Transfer> getAllTransfers(Integer userId);

    Transfer getTransferById(Integer userId, Integer transferId);

    Transfer createTransfer(Integer userId, Transfer transfer);

    List<Transfer> getPendingTransfers(Integer userId);

    void updateTransferStatus(Integer transferId, Integer statusId);
}
