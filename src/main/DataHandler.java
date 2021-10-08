package main;

import accounts.Account;
import banking.Address;
import banking.Transaction;
import customer.Customer;
import employee.Employee;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataHandler {

    private static DBManager dbManager = new DBManager();

    public static void createCustomer(Customer cust){
        if(!dbManager.writeToDB(cust)){
            System.out.println("Customer not created");
            return;
        }
        System.out.println("New Customer created");

    }

    public static void createEmployee(Employee emp){
        if(!dbManager.writeToDB(emp)){
            System.out.println("Employee not created");
            return;
        }
        System.out.println("New Employee created");
    }

    public static void createAccount(Account acc){
        if(!dbManager.writeToDB(acc)){
            System.out.println("Account not created");
            return;
        }
        System.out.println("New Account created");
    }

    public static void createAddress(Address add){
        if(!dbManager.writeToDB(add)){
            System.out.println("Address not created");
            return;
        }
        System.out.println("New Address created");
    }

    public static Customer getCustomer(int id){
        if(dbManager.getCustomer(id)!=null){
            return dbManager.getCustomer(id);
        }
        return null;
    }

    public static Employee getEmployee(int id , String  name){
        if(dbManager.getEmployee(id,name)!=null){
            return dbManager.getEmployee(id, name);
        }
        return null;
    }

    public static Account getAccount(int accNo){
        if(dbManager.getAccount(accNo)!=null){
            return dbManager.getAccount(accNo);
        }
        return null;
    }

    public static List<Account> getAllAccounts(String type) {
        if(dbManager.getAllAccounts(type)!=null){
            return dbManager.getAllAccounts(type);
        }
        return null;
    }

    public static boolean isOwner(int sender_acc, int customerID) {
        if(dbManager.isOwner(sender_acc,customerID)){
            return true;
        }
        return false;
    }

    public static boolean addTransaction(Transaction tt, int customerID) {
        if(dbManager.writeToDB(tt, customerID)){
            return true;
        }
        return false;
    }

    public static List<Integer> getBeneficiary(int sender_acc_no) {

        List<Integer> all = new ArrayList<>();
        if (dbManager.getBeneficiary(sender_acc_no).isEmpty()) {
            return all;
        }
        all = dbManager.getBeneficiary(sender_acc_no);
        return all;
    }

    public static boolean addBenefiary(int sender_acc , int receiver_acc) {
        return dbManager.writeToDB(sender_acc, receiver_acc);
    }

    public static List<Transaction> getAllTransaction(int custId){
        return dbManager.getAllTransaction(custId);
    }

    public static List<Customer> getAllCustomer(String colmn , int order){
        return dbManager.getAllCustomer(colmn, order);
    }

    public static void amountReceived(int accno,Customer cust){
        dbManager.amountReceived(accno,cust);
    }

    public static List<Transaction> getTransationsbetweenTime(Date from , Date to) throws ParseException{
        return dbManager.getTransationsbetweenTime(from,to);
    }
}
