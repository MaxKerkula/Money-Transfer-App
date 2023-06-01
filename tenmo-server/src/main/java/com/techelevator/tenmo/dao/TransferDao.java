package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfers(Integer userId);

    Transfer getTransferById(Integer userId, Integer transferId);

    Transfer createTransfer(Integer userId, Transfer transfer);

    List<Transfer> getPendingTransfers(Integer userId);

    void updateTransferStatus(Integer transferId, TransferStatus status);
    String getAccountFromUsername(Integer userId);

    String getTransferTypeById(Integer transferTypeId);
    String getTransferStatusById(Integer transferStatusId);

}