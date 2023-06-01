package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TransferStatus {

    private int transferStatusId;
    private String transferStatusDesc;

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferStatus)) return false;
        TransferStatus that = (TransferStatus) o;
        return getTransferStatusId() == that.getTransferStatusId() && getTransferStatusDesc().equals(that.getTransferStatusDesc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransferStatusId(), getTransferStatusDesc());
    }

    @Override
    public String toString() {
        return "TransferStatus{" +
                "transferStatusId=" + transferStatusId +
                ", transferStatusDesc='" + transferStatusDesc + '\'' +
                '}';
    }
}
