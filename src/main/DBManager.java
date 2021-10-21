package main;

import accounts.Account;
import accounts.CurrentAccount;
import accounts.DepositAccount;
import accounts.SavingsAccount;
import banking.Address;
import banking.Transaction;
import employee.Employee;
import customer.Customer;

import java.io.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DBManager extends DataHandler{

    public static void createTable() throws SQLException, FileNotFoundException {  // This is used to the create tables


        File createFile = new File("create.txt");
        try {
            if (!createFile.exists()) {
                createFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Scanner sc = new Scanner(createFile);
        String creationquery;

        while (sc.hasNextLine()) {
            creationquery = sc.nextLine();
          ConnectionHandler ch =  new ConnectionHandler(creationquery);
//            System.out.println("added");
        }


    }

    public boolean writeToDB(Customer cust) {   //To write a Customer to DB
        String query = "select max(cust_id) from customer;";
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            int customerID = 0;
            while (ch.next()) {
                customerID = ch.getInt(1);
                customerID += 1;
//                System.out.println("Your new custId = " + customerID);
            }
            cust.setCustomerID(customerID);
            query = "insert into customer values(" + customerID + ",";
            query += "'" + cust.getFirstName() + "','" + cust.getLastName() + "','" + cust.getPassword() + "',";
            query += cust.getAddressID() + "," + cust.getPhoneNumber() + ",'" + cust.getDOB() + "');";
//            System.out.println(query);
            ch.execute(query);
            System.out.println("Your new custId is " + customerID);
            System.out.println("Your new AddressId is " + cust.getAddressID());
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Phone no already exists to another user, plz enter a different one ");
            return false;
        } catch (SQLException e) {
            System.out.println(e + " caught at customer add to db");
        }
        return false;
    }

    public boolean writeToDB(Employee emp) {   //To write a employee to DB
        String query = "select max(emp_id) from employee";
        try (ConnectionHandler rs = new ConnectionHandler(query)) {
            int empID = 0;
            while (rs.next()) {
                empID = rs.getInt(1);
                empID += 1;
            }
            emp.setEmployeeID(empID);
//            System.out.println("New emp ID is " + empID);
            query = "insert into employee values(" + empID + ",'" + emp.getEmployeeName() + "','";
            query += emp.getEmployeeType() + "'," + emp.getPhoneNum() + ",'" + emp.getDob() + "'," + emp.getSalary() + "," + emp.getEmployeeAddressID() + ");";
//            System.out.println(query);
            rs.execute(query);
            System.out.println("New emp ID is " + empID);
            System.out.println("Your new AddressId is " + emp.getEmployeeAddressID());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Phone no already exists to another user, plz enter a different one ");
            return false;
        } catch (SQLException e) {
            System.out.println(e + " caught at add employee db");
            return false;
        }
        return true;
    }

    public boolean writeToDB(Account ac) {   //To write a account to DB
        System.out.println("\nAdding an acc");
        String lastAcc = "select max(acc_no) from account;";
        try (ConnectionHandler ch = new ConnectionHandler(lastAcc)) {
            int accountNo = 0;
            while (ch.next()) {
                accountNo = ch.getInt(1);
                accountNo += 1;
            }
            ac.setAccountNo(accountNo);
//            System.out.println("New Account no is " + accountNo);
            String query = "insert into account values(" + ac.getCustomerID() + ",";
            query += accountNo + ",'" + ac.getBranchName() + "','" + ac.getIfscCode() + "'," + ac.getPin() + ",'" + ac.getAccType() + "'," + ac.getBalance() + ");";
//            System.out.println(query);
            ch.execute(query);
            boolean added = false;
            switch (ac.getAccType().toString()) {
                case "Savings":
                    added = DBManager.writeToDB((SavingsAccount) ac);
                    break;
                case "Current":
                    added = DBManager.writeToDB((CurrentAccount) ac);
                    break;
                case "Deposit":
                    added = DBManager.writeToDB((DepositAccount) ac);
                    break;
                default:
                    break;
            }
            if (!added) {
                ch.execute("delete from account where acc_no =" + accountNo);
            }
        } catch (SQLException e) {
            System.out.println(e + " caught at acc");
            return false;
        }
        return true;
    }

    private static boolean writeToDB(SavingsAccount sb) {  //To write a specific type of account to DB
        String query = "insert into savings_account values(";
        query += sb.getAccountNo() + "," + sb.getWithdrawLimit() + ")";
//        System.out.println(query);
        System.out.println("Adding SB");
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
        } catch (SQLException e) {
            System.out.println(e + " caught savings acc");
            return false;
        }
        System.out.println("SB created");
        return true;

    }

    private static boolean writeToDB(CurrentAccount ca) {  //To write a specific type of account to DB
        System.out.println("Adding CA");
        String query = "insert into current_account values(";
        query += ca.getAccountNo() + "," + ca.getCreditLimit() + "," + ca.getWithdrawLimit() + ");";
//        System.out.println(query);
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
        } catch (SQLException sqle) {
            System.out.println(sqle + "caught current acc");
            return false;
        }
        System.out.println("CA created");
        return true;

    }

    private static boolean writeToDB(DepositAccount da) {  //To write a specific type of account to DB
        System.out.println("Adding DA");
        String query = "insert into current_account values(";
        query += da.getAccountNo() + "," + da.getInterestRate() + ",'";
        query += da.getDepositDate() + "'," + da.getTermsInMonth() + ");";
//        System.out.println(query);
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            ch.execute(query);
        } catch (SQLException sqle) {
            System.out.println(sqle + "caught deposit acc");
            return false;
        }
        System.out.println("DA created");
        return true;
    }

    public boolean writeToDB(Address ad) {  //To write a specific address to DB
        String query = "select max(id) from address";
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            long addressID = 0;
            while (ch.next()) {
                addressID = ch.getLong(1);
                addressID += 1;
//                System.out.println("new AddressID is " + addressID);
            }
            ad.setAddressID(addressID);
            query = "insert into address values(" + addressID + ",";
            query += ad.getBuildingNo() + ",'" + ad.getArea() + "','";
            query += ad.getCity() + "','" + ad.getState() + "'," + ad.getPincode() + ")";
//            System.out.println(query);
            ch.execute(query);
            return true;
        } catch (Exception e) {
            System.out.println(e + " caught at address");
        }
        return false;
    }

    public static Address getAddress(int addressID) {   //To get a specific address from DB
        String query = "select * from address where id = " + addressID;
        Address ad = new Address();
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            if (ch.next()) {
                ad.setAddressID(ch.getLong(1));
                ad.setBuildingNo(ch.getInt(2));
                ad.setArea(ch.getString(3));
                ad.setCity(ch.getString(4));
                ad.setState(ch.getInt(5));
                ad.setPincode(ch.getInt(6));
            } else {
                ad = null;
            }
        } catch (SQLException e) {
            System.out.println(e + " get address DBM");
            ad = null;
        }
        return ad;
    }

    public Customer getCustomer(int customerID) {   //To get a specific customer from DB
        Customer newCust = null;
        String query = "select * from customer where cust_id = " + customerID;
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            if (ch.next()) {
                newCust = new Customer();
                newCust.setCustomerID(ch.getInt(1));
                newCust.setFirstName(ch.getString(2));
                newCust.setLastName(ch.getString(3));
                newCust.setPassword(ch.getString(4));
                newCust.setAddressID(ch.getLong(5));
                newCust.setPhoneNumber(ch.getLong(6));
                newCust.setDOB(ch.getString(7));

                return newCust;
            }
        } catch (SQLException e) {
            newCust = null;
            System.out.println(e + " getCustomer DBM");
        }
        return null;
    }

    public Employee getEmployee(int empID, String name) {  //To get a specific employee from DB

        String query = "select * from employee where emp_id = " + empID + " and emp_name = '" + name + "';";
//        System.out.println(query);
        Employee emp = null;
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            emp = new Employee();
            if (ch.next()) {
                emp.setEmployeeID(ch.getInt(1));
                emp.setEmployeeName(ch.getString(2));
                emp.setEmployeeType(ch.getObject(3).toString());
                emp.setPhoneNum(ch.getLong(4));
                emp.setDob(ch.getString(5));
                emp.setSalary(ch.getInt(6));
                emp.setEmployeeAddressID(ch.getLong(7));
            }
        } catch (SQLException sqle) {
            emp = null;
            System.out.println(sqle + " get employee DBM");
        }
        return emp;
    }

    public static Account getAccount(int accNo) {   // To get a single account from DB
        Account ac = null;
        String query = "select * from account where acc_no = " + accNo + ";";
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            if (ch.next()) {
                ac = new Account();
                ac.setCustomerID(ch.getInt(1));
                ac.setAccountNo(ch.getInt(2));
                ac.setBrachName(ch.getString(3));
                ac.setIfscCode(ch.getString(4));
                ac.setPin(ch.getInt(5));
                ac.setAccType(ch.getObject(6).toString());
                ac.setBalance(ch.getInt(7));
            }
        } catch (SQLException sqle) {
            ac = null;
            System.out.println(sqle + " caught at get account DBM");
        }
        return ac;
    }

    public List<Account> getAllAccounts(String type) {   // To get all accounts and also specific type
        List<Account> all = new ArrayList<>();
        String query = "select acc_no from account";
        if (!type.equalsIgnoreCase("all")) {
            query += " where acc_type = '" + type + "'";
        }
        query += ";";
        System.out.println(query);
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            while (ch.next()) {
                Account ac = new DBManager().getAccount(ch.getInt(1));
                all.add(ac);
            }
            return all;
        } catch (SQLException sqe) {
            System.out.println(sqe + " getAllaccount");
            return all;
        }
    }

    public static boolean isOwner(int sender_acc, int customerID) {  // This function is used to check whether the given account is his or not

        String query = "select * from account where cust_id = " + customerID + " and acc_no = " + sender_acc;

        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            return ch.next();
        } catch (SQLException sqle) {
            System.out.println(sqle + " caught not owned");
        }
        return false;
    }

    public boolean writeToDB(Transaction tt, int customerID) {  // This function is used to write the transaction values into DB
        Scanner sc = new Scanner(System.in);
//        System.out.println("wrdb tr");
        long transactionID = 0;
        String query, update = "";
        if (!new DBManager().isOwner(tt.getSenderAccNo(), customerID)) {
            System.out.println("Sender acc not owned");
            return false;
        }

        System.out.println("Enter pin");
        int pin = sc.nextInt();
        query = "select pin from account where acc_no = " + tt.getSenderAccNo() + ";";
        try (ConnectionHandler rs = new ConnectionHandler(query)) {
            while (rs.next()) {
                if (rs.getInt(1) != pin) {
                    System.out.println("Wrong pin");
                    return false;
                }
            }


            query = "select max(transaction_id) from transaction";
            rs.execute(query);
            while (rs.next()) {
                transactionID = rs.getLong(1);
                transactionID += 1;
//                System.out.println("Transaction ID " + transactionID);
            }

            rs.execute("start transaction");
            tt.setTranactionTime(new Date().toString());
            String insertUpdate = "insert into transaction values(";

            if (new DBManager().getAccount(tt.getSenderAccNo()).getBalance() < tt.getTransactionAmt()) {
                System.out.println("Transaction amt exceeds balance");
                return false;
            }

            System.out.println("Transfer");
            insertUpdate += tt.getSenderAccNo() + "," + tt.getReceiverAccNo() + ",";
            update = "update account set account_balance = account_balance-" + tt.getTransactionAmt();
            update += " where acc_no = " + tt.getSenderAccNo() + ";";
//            System.out.println(update);
            rs.execute(update);
            update = "update account set account_balance = account_balance+" + tt.getTransactionAmt();
            update += " where acc_no = " + tt.getReceiverAccNo() + ";";
//            System.out.println(update);
            rs.execute(update);

            insertUpdate += transactionID + "," + tt.getTransactionAmt() + ",";
            insertUpdate += customerID + ",'" + tt.getTranactionTime() + "');";
//            System.out.println(insertUpdate);
            rs.execute(insertUpdate);
            rs.execute("commit");
            tt.printTransaction();
        } catch (SQLException sqle) {
            System.out.println(sqle + " caught transaction DBM");
            return false;
        }
        return true;
    }

    public static List<Integer> getBeneficiary(int sender_acc_no) {   // This function is used to get all the beneficiary account of the sender

        List<Integer> all = new ArrayList<>();

        String query = "select receiver_acc_no from beneficiary where sender_acc_no =" + sender_acc_no;
        try (ConnectionHandler rs = new ConnectionHandler(query)) {
            while (rs.next()) {
                all.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e + " caught at get beneficiary");
        }

        return all;
    }

    public boolean writeToDB(int send_acc, int receive_acc) {  // This function is used to add beneficary accounts into DB
        String query = "insert into beneficiary values(" + send_acc + "," + receive_acc + ")";
        try (ConnectionHandler rs = new ConnectionHandler(query)) {
        } catch (SQLException e) {
            System.out.println(e + " caught at adding beneficiary");
            System.out.println("Already that account exists");
            return false;
        }
        System.out.println("Beneficiary added");
        return true;
    }

    public List<Transaction> getAllTransaction(int cust_Id) {  //This function is used to get all transaction made by a customer

        String query;
        if (cust_Id != -1) {
            query = "select * from transaction where cust_id =" + cust_Id;
        } else {
            query = "select * from transaction";
        }
        List<Transaction> transactionList = new ArrayList<>();
        boolean b = false;
        try (ConnectionHandler rs = new ConnectionHandler(query)) {
            Transaction tt;
            while (rs.next()) {
                tt = new Transaction();

                tt.setSenderAccNo(rs.getInt(1));
                tt.setReceiverAccNo(rs.getInt(2));
                tt.setTransactionID(rs.getLong(3));
                tt.setTransactionAmt(rs.getInt(4));
                tt.setTranactionTime(rs.getString(6));
                tt.setCustomerId(rs.getInt(5));
                transactionList.add(tt);
//                tt.printTransaction();
                b = true;

            }
        } catch (SQLException e) {
            System.out.println(e + " caught at get all transaction");
        }
        if (!b) {
            System.out.println("No transactions made");
        }
        return transactionList;
    }

    public static boolean isCustomerTableEmpty() {

        String query = "select COUNT(*) from customer";

        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            while (ch.next()) {
                if (ch.getInt(1) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e + " Caught at empty cust");
        }

        return false;
    }

    public static boolean isEmployeeTableEmpty() {

        String query = "select COUNT(*) from employee";

        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            while (ch.next()) {
                if (ch.getInt(1) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e + " Caught at empty emp");
        }

        return false;
    }

    public List<Customer> getAllCustomer(String colmn,int num){
        String order ="";
        if(num==-1){
            order="DESC";
        }else {
            order="ASC";
        }
        List<Customer> customerList = new ArrayList<>();
        String query = "select * from customer order by "+colmn+" "+order+";";
        try(ConnectionHandler ch = new ConnectionHandler(query)){

            Customer newCust;
            while (ch.next()) {
                newCust = new Customer();
                newCust.setCustomerID(ch.getInt(1));
                newCust.setFirstName(ch.getString(2));
                newCust.setLastName(ch.getString(3));
                newCust.setPassword(ch.getString(4));
                newCust.setAddressID(ch.getLong(5));
                newCust.setPhoneNumber(ch.getLong(6));
                newCust.setDOB(ch.getString(7));

                customerList.add(newCust);
            }

        }catch (SQLException e){
            System.out.println(e+" caught at get all cust");
        }
        return customerList;
    }

    public void amountReceived(int accno,Customer customer){

        if(!isOwner(accno,customer.getCustomerID())){
            System.out.println("Not Owner");
            return;
        }
        String query = "select * from transaction where receiver_acc_no = "+accno;
        boolean b=false;
        try(ConnectionHandler rs = new ConnectionHandler(query)){
            Transaction tt ;

            while(rs.next()){
                tt = new Transaction();

                tt.setSenderAccNo(rs.getInt(1));
                tt.setReceiverAccNo(rs.getInt(2));
                tt.setTransactionID(rs.getLong(3));
                tt.setTransactionAmt(rs.getInt(4));
                tt.setTranactionTime(rs.getString(6));
                tt.setCustomerId(rs.getInt(5));

                tt.printTransaction();
                b = true;
            }

        }catch (Exception e){
            System.out.println(e+" Caught at received Amount");
        }
        if(!b){
            System.out.println("No Transactions made yet");
        }
    }

    public List<Transaction> getTransationsbetweenTime(Date from , Date to) throws ParseException {

        List<Transaction> transactionList = getAllTransaction(-1);
        List<Transaction> transactions = new ArrayList<>();
        Transaction tt;
        Iterator itr = transactionList.listIterator();
        while(itr.hasNext()){
            tt = (Transaction) itr.next();
            String dt=tt.getTranactionTime();
            dt=dt.replaceAll("IST","");
            SimpleDateFormat formatter5 = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");

            Date d1 = formatter5.parse(dt);
//            System.out.println(d1+" "+d1.after(from)+" "+d1.before(to)+" "+from+" "+to);

            if(d1.after(from) && d1.before(to)){
                transactions.add(tt);
            }
        }

        return transactions;
    }



}
