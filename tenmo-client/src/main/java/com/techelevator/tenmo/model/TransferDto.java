package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TransferDto {

    private int account_from;
    private int account_to;
    private BigDecimal amount;
    private int transfer_type_id;

    public TransferDto(int account_from, int account_to, BigDecimal amount, int transfer_type_id) {
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
        this.transfer_type_id = transfer_type_id;
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

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferDto)) return false;
        TransferDto that = (TransferDto) o;
        return getAccount_from() == that.getAccount_from() && getAccount_to() == that.getAccount_to() && getTransfer_type_id() == that.getTransfer_type_id() && getAmount().equals(that.getAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccount_from(), getAccount_to(), getAmount(), getTransfer_type_id());
    }

    @Override
    public String toString() {
        return "TransferDto{" +
                "account_from=" + account_from +
                ", account_to=" + account_to +
                ", amount=" + amount +
                ", transfer_type_id=" + transfer_type_id +
                '}';
    }
}
