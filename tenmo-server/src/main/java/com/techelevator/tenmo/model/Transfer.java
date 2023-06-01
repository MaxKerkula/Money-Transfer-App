package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Transfer {
    private int transferId;
    private int account_from;
    private int account_to;
    private BigDecimal amount;
    private int transfer_status_id;
    private int transfer_type_id;

    public Transfer(){}

    public Transfer(int transferId, int account_from, int account_to, BigDecimal amount, int transfer_type_id) {
        this.transferId = transferId;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
        this.transfer_status_id = transfer_status_id;
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getAccount_from() {
        return account_from;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer)) return false;
        Transfer transfer = (Transfer) o;
        return getTransferId() == transfer.getTransferId() && getAccount_from() == transfer.getAccount_from() && getAccount_to() == transfer.getAccount_to() && getTransfer_status_id() == transfer.getTransfer_status_id() && getTransfer_type_id() == transfer.getTransfer_type_id() && getAmount().equals(transfer.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransferId(), getAccount_from(), getAccount_to(), getAmount(), getTransfer_status_id(), getTransfer_type_id());
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", account_from=" + account_from +
                ", account_to=" + account_to +
                ", amount=" + amount +
                ", transfer_status_id=" + transfer_status_id +
                ", transfer_type_id=" + transfer_type_id +
                '}';
    }
}