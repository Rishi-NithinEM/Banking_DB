package accounts;

import banking.Branch;

public class Account {

    private int customerID;
    private int accountNo;
    private int balance;
    private int pin;
    private AccountType accType;
    private String branchName;
    private String ifscCode;


    public Account() {
    }

    public int getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setBranchName(int index) {
        this.branchName = String.valueOf(Branch.BrachName.values()[index]);
        setIfscCode(Branch.BrachName.values()[index].getIFSCCode());
    }

    public void setBrachName(String val) {
        this.branchName = Branch.BrachName.valueOf(val).toString();
    }

    public void setIfscCode(String val){
        this.ifscCode = val;
    }

    public String getIfscCode(){
        return this.ifscCode;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public int getAccountNo() {
        return this.accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public AccountType getAccType() {
        return this.accType;
    }

    public void setAccType(int index) {
        this.accType = AccountType.values()[index];
    }

    public void setAccType(String val) {
        this.accType = AccountType.valueOf(val);
    }



    public int getPin() {
        return this.pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }



    public static enum AccountType {
        Savings,
        Current,
        Deposit;

        private AccountType() {
        }
    }

}
