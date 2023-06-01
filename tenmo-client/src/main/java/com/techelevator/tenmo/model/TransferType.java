package com.techelevator.tenmo.model;

import java.util.Objects;

public class TransferType {
    private int transferTypeId;
    private String transferTypeDesc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferType)) return false;
        TransferType that = (TransferType) o;
        return transferTypeId == that.transferTypeId && transferTypeDesc.equals(that.transferTypeDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferTypeId, transferTypeDesc);
    }

    @Override
    public String toString() {
        return "TransferType{" +
                "transferTypeId=" + transferTypeId +
                ", transferTypeDesc='" + transferTypeDesc + '\'' +
                '}';
    }

}
