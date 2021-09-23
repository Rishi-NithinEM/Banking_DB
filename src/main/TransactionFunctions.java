package main;

import accounts.Account;
import banking.Transaction;
import customer.Customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class TransactionFunctions {


    public void transfer(Customer cust, Transaction tr) throws IOException, SQLException {
        Scanner sc = new Scanner(System.in);
        tr.setCustomerId(cust.getCustomerID());
        int accNo;
        Account acc;
        int raccNo;
        Account racc;
        while (true) {
            System.out.println("Sender Account no");
            try {
                accNo = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Enter only numbers");
                continue;
            }

            tr.setSenderAccNo(accNo);
            if(!DBManager.isOwner(accNo,cust.getCustomerID())){
                System.out.println("Account not owned");
                return;
            }
            acc = DBManager.getAccount(accNo);

            if (!DBManager.getBeneficiary(accNo).isEmpty()) {

                System.out.println("Select a beneficiary Account: \n");

                Object account[] = DBManager.getBeneficiary(accNo).toArray();
                int i = 1;
                for (Object s : account) {
                    System.out.println(i++ + "  " + s);
                }
                System.out.println(i + "  Add new beneficiary account");
                int val = 0;
                try {
                    val = sc.nextInt();
                } catch (Exception e) {
                    System.out.println("Enter only number between 1 to " + account.length + 1);
                    continue;
                }

                if (val <= account.length && val > 0) {

                    raccNo = Integer.parseInt(account[val - 1].toString());
                    racc = DBManager.getAccount(raccNo);
                    tr.setReceiverAccNo(racc.getAccountNo());

                    System.out.println("Enter transaction amt");
                    int amt = sc.nextInt();

                    if (amt < 1) {
                        System.out.println("Invalid Amount");
                    } else {

                        tr.setTransactionAmt(amt);
                        DBManager.writeToDB(tr,cust.getCustomerID());
                    }

                    break;

                } else {
                    if (val == account.length + 1) {
                        System.out.println("Adding new beneficiary\n");
                        addBeneficiary(acc);
                        continue;
                    } else {
                        System.out.println("Enter only number between 1 to " + account.length);
                        continue;
                    }
                }
            } else {
                System.out.println("Adding new beneficiary\n");
                addBeneficiary(acc);
                continue;
            }

        }
    }

    public Account addBeneficiary(Account acc) throws IOException, SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Beneficiary Account number");
        Account racc;
        int accnNo = Integer.parseInt(sc.nextLine());
        if (accnNo != acc.getAccountNo()) {
            racc = DBManager.getAccount(accnNo);
            if (racc != null) {
                System.out.println("Enter Beneficiary Account IFSC code");
                String code = sc.nextLine();

                if (racc.getIfscCode().equals(code)) {

                    if(DBManager.writeToDB(acc.getAccountNo(), racc.getAccountNo())){
                        System.out.println("New beneficiary Added");
                        return racc;
                    }

                } else {
                    System.out.println("Wrong IFSC code");
                    return null;
                }
            } else
                System.out.println("Sorry account does not exist");
        } else {
            System.out.println("Cant enter same account number or Account already exists");
            return null;
        }

        return null;
    }


    public void checkBalance(Customer cust) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Account no");
        int accNo = Integer.parseInt(sc.nextLine());
        Account acc = DBManager.getAccount(accNo);
        if (acc != null) {
            if (DBManager.isOwner(accNo, cust.getCustomerID())) {
                System.out.println("Enter Pin :");
                int pin = Integer.parseInt(sc.nextLine());
                if (acc.getPin() == pin) {
                    System.out.println("Balance : " + acc.getBalance());
                } else {
                    System.out.println("Wrong Pin");
                }
            } else {
                System.out.println("Account not owned");
            }
        } else {
            System.out.println("Invalid account");
        }
        return;
    }



}
