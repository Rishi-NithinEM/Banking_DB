package accounts;

import java.util.Date;

public class DepositAccount extends Account {

    private double interestRate;
    private Date depositDate;
    private int termsInMonth;

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public Date getDepositDate() {
        return this.depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public int getTermsInMonth() {
        return this.termsInMonth;
    }

    public void setTermsInMonth(int termsInMonth) {
        this.termsInMonth = termsInMonth;
    }

    public DepositAccount() {
        this.setBalance(1000);
        this.setInterestRate(0.45D);
        this.setDepositDate(new Date());
    }

}
