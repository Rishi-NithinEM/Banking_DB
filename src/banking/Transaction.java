package banking;

public class Transaction {

    private int customerId;
    private int senderAccNo;
    private int receiverAccNo;
    private long transactionID;
    private int transactionAmt;
    private String tranactionTime ;

    public int getSenderAccNo() {
        return this.senderAccNo;
    }

    public void setSenderAccNo(int senderAccNo) {
        this.senderAccNo = senderAccNo;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public int getReceiverAccNo() {
        return this.receiverAccNo;
    }

    public void setReceiverAccNo(int receiverAccNo) {
        this.receiverAccNo = receiverAccNo;
    }

    public long getTransactionID() {
        return this.transactionID;
    }

    public void setTransactionID(long transactionID) {
        this.transactionID = transactionID;
    }

    public int getTransactionAmt() {
        return this.transactionAmt;
    }

    public void setTransactionAmt(int transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public Transaction() {
    }


    public String getTranactionTime() { return this.tranactionTime; }

    public void setTranactionTime(String tranactionTime) { this.tranactionTime = tranactionTime; }

}
