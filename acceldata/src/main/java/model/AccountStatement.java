package model;

public class AccountStatement {

    private Long epoch;
    private TransactionType transactionType;
    private double amount;
    private String remark;
    private String creditFromDetails;
    private String debitFromDetails;

    public Long getEpoch() {
        return epoch;
    }

    public void setEpoch(Long epoch) {
        this.epoch = epoch;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreditFromDetails() {
        return creditFromDetails;
    }

    public void setCreditFromDetails(String creditFromDetails) {
        this.creditFromDetails = creditFromDetails;
    }

    public String getDebitFromDetails() {
        return debitFromDetails;
    }

    public void setDebitFromDetails(String debitFromDetails) {
        this.debitFromDetails = debitFromDetails;
    }
}


