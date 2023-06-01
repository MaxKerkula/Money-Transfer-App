package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping
public class TransferController {
    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final Integer PENDING = 1;
    private final Integer APPROVED = 2;
    private final Integer REJECTED = 3;
    private final Integer SEND = 1;
    private final Integer REQUEST = 2;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping("/transfer")
    public List<Transfer> listAllTransfers(Principal principal) {
        Integer userId = userDao.findIdByUsername(principal.getName());
        List<Transfer> transfers = transferDao.getAllTransfers(userId);
        return transfers;
    }


    @GetMapping("/transfer/{transferId}")
    public Transfer getTransferById(@PathVariable Integer transferId, Principal principal) {
        Integer userId = userDao.findIdByUsername(principal.getName());
        Transfer transfer = transferDao.getTransferById(userId, transferId);
        return transfer;
    }

    @PostMapping("/transfer")
    public Transfer createTransfer(@RequestBody TransferDto transfer, Principal principal) {
        TransferDto mytransfer = transfer;

        User user = userDao.findByUsername(principal.getName());
        Account fromAccount = accountDao.getAccountByUserId(transfer.getAccount_from());
        Account toAccount = accountDao.getAccountByUserId(transfer.getAccount_to());
        Transfer newTransfer = new Transfer();
        boolean validateSender = fromAccount.getUserId() == user.getId();
        boolean validateReceiver = toAccount.getUserId() != user.getId();
        boolean validateTransferType = transfer.getTransfer_type_id() == 2;

        if (validateTransferType && !validateSender && !validateReceiver){
            System.out.println("Should not do transfer");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot create transfer for another user");
        }

        if (fromAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

            BigDecimal fromAccountBalanceUpdate = fromAccount.getBalance().subtract(transfer.getAmount());
            BigDecimal toAccountBalanceUpdate = toAccount.getBalance().add(transfer.getAmount());
            fromAccount.setBalance(fromAccountBalanceUpdate);
            toAccount.setBalance(toAccountBalanceUpdate);

            accountDao.updateBalance(fromAccount);
            accountDao.updateBalance(toAccount);

            Transfer transferToProcess = new Transfer(0, fromAccount.getAccountId(), toAccount.getAccountId(), transfer.getAmount(), transfer.getTransfer_type_id());


            if (transfer.getTransfer_type_id() == SEND) {
                transferToProcess.setTransfer_status_id(APPROVED);
            } else if (transfer.getTransfer_type_id() == REQUEST) {
                transferToProcess.setTransfer_status_id(PENDING);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transfer type");
            }

            newTransfer = transferDao.createTransfer(user.getId(), transferToProcess);

            if (newTransfer == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer creation failed");
            }

            return newTransfer;
        }

        @PostMapping("/transfer/request")
        public Transfer createTransferRequest (@RequestBody TransferDto transfer, Principal principal){

            User user = userDao.findByUsername(principal.getName());
            Account fromAccount = accountDao.getAccountByUserId(transfer.getAccount_from());
            Account toAccount = accountDao.getAccountByUserId(transfer.getAccount_to());
            Transfer newTransfer = new Transfer();
            boolean validateSender = fromAccount.getUserId() != user.getId();
            boolean validateReceiver = toAccount.getUserId() == user.getId();
            boolean validateTransferType = transfer.getTransfer_type_id() == 1;

            if (validateTransferType && !validateSender && !validateReceiver){
                System.out.println("Should not do transfer");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot create transfer for another user");
            }

            if (fromAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
            }

            BigDecimal fromAccountBalanceUpdate = fromAccount.getBalance().subtract(transfer.getAmount());
            BigDecimal toAccountBalanceUpdate = toAccount.getBalance().add(transfer.getAmount());
            fromAccount.setBalance(fromAccountBalanceUpdate);
            toAccount.setBalance(toAccountBalanceUpdate);

            accountDao.updateBalance(fromAccount);
            accountDao.updateBalance(toAccount);

            Transfer transferToProcess = new Transfer(0, fromAccount.getAccountId(), toAccount.getAccountId(), transfer.getAmount(), transfer.getTransfer_type_id());

            if (transfer.getTransfer_type_id() == SEND) {
                transferToProcess.setTransfer_status_id(APPROVED);
            } else if (transfer.getTransfer_type_id() == REQUEST) {
                transferToProcess.setTransfer_status_id(PENDING);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transfer type");
            }

            newTransfer = transferDao.createTransfer(user.getId(), transferToProcess);

            if (newTransfer == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer creation failed");
            }

            return newTransfer;
        }

        @PutMapping("/transfer/update/{transferId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void updateTransferStatus (@PathVariable Integer transferId, Principal
        principal, @RequestBody TransferStatus transferStatus){
            User user = userDao.findByUsername(principal.getName());
            Transfer transfer = transferDao.getTransferById(user.getId(), transferId);
            if (transfer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
            }
            if (transferStatus.getTransferStatusId() == APPROVED) {
                if (transfer.getTransfer_status_id() != PENDING) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot approve transfer with status other than PENDING");
                }
            } else if (transferStatus.getTransferStatusId() == REJECTED) {
                if (transfer.getTransfer_status_id() != PENDING) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot reject transfer with status other than PENDING");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transfer status");
            }
            TransferStatus updatedStatus = new TransferStatus();
            updatedStatus.setTransferStatusId(transferStatus.getTransferStatusId());
            transferDao.updateTransferStatus(transfer.getTransferId(), updatedStatus);
        }

        @GetMapping("transfer/pending")
        public List<Transfer> getPendingTransfers (Principal principal){
            Integer userId = userDao.findIdByUsername(principal.getName());
            List<Transfer> transfers = transferDao.getPendingTransfers(userId);
            return transfers;
        }

    @GetMapping("/transfer/accountFrom/{userId}")
    public String getAccountFromUsername(@PathVariable Integer userId) {
        return transferDao.getAccountFromUsername(userId);
    }

    @GetMapping("/transferType/{transferTypeId}")
    public String getTransferTypeById(@PathVariable Integer transferTypeId) {
        return transferDao.getTransferTypeById(transferTypeId);
    }

    @GetMapping("/transferStatus/{transferStatusId}")
    public String getTransferStatusById(@PathVariable Integer transferStatusId) {
        return transferDao.getTransferStatusById(transferStatusId);
    }




}