package main;

import accounts.Account;
import accounts.CurrentAccount;
import accounts.DepositAccount;
import accounts.SavingsAccount;
import banking.Address;
import banking.Transaction;
import employee.Employee;
import customer.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class DBManager {

    public static void createTable() throws SQLException {
        String custquery = "create table if not exists customer(\n" +
                "\tcust_id int unsigned auto_increment,\n" +
                "\tcust_fname varchar(35) not null,\n" +
                "\tcust_lname varchar(35) not null,\n" +
                "\tpass_word varchar(40) not null,\n" +
                "\taddress_id bigint unsigned not null,\n" +
                "\tphone_no bigint not null unique,\n" +
                "\tcust_dob varchar(10) not null,\n" +
                "\tconstraint pk_cust_id primary key(cust_id),\n" +
                "\tconstraint fk_customer_address_id foreign key(address_id) references address(id))";

        String addquery = "create table if not exists address(\n" +
                "\tid bigint unsigned auto_increment,\n" +
                "\tbuilding_no smallint not null,\n" +
                "\tarea varchar(40) not null,\n" +
                "\tcity varchar(25) not null,\n" +
                "\tstate varchar(20) not null,\n" +
                "\tpincode mediumint not null,\n" +
                "\tconstraint pk_address_id primary key(id))";

        String empquery = "create table if not exists employee(\n" +
                "\temp_id int unsigned auto_increment,\n" +
                "\temp_name varchar(35) not null,\n" +
                "\temp_type ENUM('Manager','Accountant','Cashier') not null,\n" +
                "\tphone_no bigint not null unique,\n" +
                "\tdob varchar(20) not null,\n" +
                "\tsalary int unsigned not null,\n" +
                "\taddress_id bigint unsigned,\n" +
                "\tconstraint pk_emp_id primary key(emp_id),\n" +
                "\tconstraint fk_employee_address_id foreign key(address_id) references address(id))";

        String accquery = "create table if not exists account(\n" +
                "\tcust_id int unsigned not null,\n" +
                "\tacc_no int unsigned auto_increment,\n" +
                "\tbranch_name varchar(25) not null,\n" +
                "\tifsc_code varchar(7) not null,\n" +
                "\tpin smallint not null,\n" +
                "\tacc_type  ENUM('Savings', 'Current','Deposit') not null,\n" +
                "\taccount_balance int unsigned not null,\n" +
                "\tconstraint pk_acc_no primary key(acc_no),\n" +
                "\tconstraint fk_account_cust_id foreign key(cust_id) references customer(cust_id));";

        String savquery = "create table if not exists savings_account(\n" +
                "\tacc_no int unsigned not null,\n" +
                "\twithdraw_limit mediumint unsigned,\n" +
                "\tconstraint pk_savings_acc_no primary key(acc_no),\n" +
                "\tconstraint fk_savings_acc_no foreign key(acc_no) references account(acc_no))";

        String curquery = "create table if not exists current_account(\n" +
                "\tacc_no int unsigned not null,\n" +
                "\tcredit_limit int unsigned not null,\n" +
                "\twithdraw_limit int unsigned,\n" +
                "\tconstraint pk_current_acc_no primary key(acc_no),\n" +
                "\tconstraint fk_current_acc_no foreign key(acc_no) references account(acc_no))";

        String depquery = "create table if not exists deposit_account(\n" +
                "\tacc_no int unsigned not null,\n" +
                "\tinterest double,\n" +
                "\tdeposit_date date,\n" +
                "\tterm_in_month smallint,\n" +
                "\tconstraint pk_deposit_acc_no primary key(acc_no),\n" +
                "\tconstraint fk_deposit_acc_no foreign key(acc_no) references account(acc_no))";

        String tranquery = "create table if not exists transaction(\n" +
                "\tsender_acc_no int unsigned,\n" +
                "\treceiver_acc_no int unsigned,\n" +
                "\ttransaction_id bigint unsigned auto_increment,\n" +
                "\ttransaction_amt int,\n" +
                "\ttransaction_time varchar(50),\n" +
                "\tcustomer_id int unsigned not null,\n" +
                "\tconstraint pk_transaction_id primary key(transaction_id),\n" +
                "\tconstraint fk_transaction_sender_acc_no foreign key(sender_acc_no) references account(acc_no),\n" +
                "\tconstraint fk_transaction_receiver_acc_no foreign key(receiver_acc_no) references account(acc_no))";

        String ben = "create table if not exists beneficiary(\n" +
                "\tsender_acc_no int unsigned,\n" +
                "\treceiver_acc_no int unsigned,\n" +
                "\tconstraint fk_beneficiary_sender_acc_no foreign key(sender_acc_no) references account(acc_no),\n" +
                "\tconstraint fk_beneficiary_receiver_acc_no foreign key(receiver_acc_no) references account(acc_no));";


        ConnectionHandler ch = new ConnectionHandler(addquery);

        ch.execute(custquery);
        ch.execute(empquery);
        ch.execute(accquery);
        ch.execute(savquery);
        ch.execute(curquery);
        ch.execute(depquery);
        ch.execute(tranquery);
        ch.execute(ben);

    }

    public static boolean writeToDB(Customer cust) {
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

            return true;
        } catch (SQLException e) {
            System.out.println(e + " caught at customer add to db");
        }
        return false;
    }

    public static boolean writeToDB(Employee emp) {
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
        } catch (SQLException e) {
            System.out.println(e + " caught at add employee db");
        }
        return true;
    }

    public static boolean writeToDB(Account ac) {
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

    private static boolean writeToDB(SavingsAccount sb) {
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

    private static boolean writeToDB(CurrentAccount ca) {
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

    private static boolean writeToDB(DepositAccount da) {
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

    public static boolean writeToDB(Address ad) {
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

    public static Address getAddress(int addressID) {
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

    public static Customer getCustomer(int customerID) {
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

    public static Employee getEmployee(int empID, String name) {
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

    public static Account getAccount(int accNo) {
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

    public static List<Account> getAllAccounts(String type) {
        List<Account> all = new ArrayList<>();
        String query = "select acc_no from account";
        if (!type.equalsIgnoreCase("all")) {
            query += " where acc_type = '" + type + "'";
        }
        query += ";";
        System.out.println(query);
        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            while (ch.next()) {
                Account ac = getAccount(ch.getInt(1));
                all.add(ac);
            }
            return all;
        } catch (SQLException sqe) {
            System.out.println(sqe + " getAllaccount");
            return null;
        }
    }

    public static boolean isOwner(int sender_acc, int customerID) {

        String query = "select * from account where cust_id = " + customerID + " and acc_no = " + sender_acc;

        try (ConnectionHandler ch = new ConnectionHandler(query)) {
            return ch.next();
        } catch (SQLException sqle) {
            System.out.println(sqle + " caught not owned");
        }
        return false;
    }

    public static boolean writeToDB(Transaction tt, int customerID) {
        Scanner sc = new Scanner(System.in);
        System.out.println("wrdb tr");
        long transactionID = 0;
        String query, update = "";
        if (!isOwner(tt.getSenderAccNo(), customerID)) {
            System.out.println("Sender acc not owned");
            return false;
        } else {
            System.out.println("Owned");
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

            insertUpdate += transactionID + "," + tt.getTransactionAmt() + ",'";
            insertUpdate +=  tt.getTranactionTime() + "',"+customerID+");";
//            System.out.println(insertUpdate);
            rs.execute(insertUpdate);
            rs.execute("commit");
            Operations.printTransaction(tt);
        } catch (SQLException sqle) {
            System.out.println(sqle + " caught transaction DBM");
            return false;
        }
        return true;
    }

    public static List<Integer> getBeneficiary(int sender_acc_no){

        List<Integer> all= new ArrayList<>();

        String query = "select receiver_acc_no from beneficiary where sender_acc_no ="+sender_acc_no;
        try(ConnectionHandler rs = new ConnectionHandler(query)){
            while(rs.next()){
                all.add(rs.getInt(1));
            }
        }catch (SQLException e){
            System.out.println(e+" caught at get beneficiary");
        }

        return all;
    }

    public static boolean writeToDB(int send_acc , int receive_acc) throws SQLException {
        String query = "insert into beneficiary values("+send_acc+","+receive_acc+")";
        try(ConnectionHandler rs = new ConnectionHandler(query)){
        }catch (SQLException e){
            System.out.println(e + " caught at adding beneficiary");
            return false;
        }
        System.out.println("Beneficiary added");
        return true;
    }

    public static void getAllTransaction(int cust_Id){

        String query;
        if(cust_Id!=-1) {
            query= "select * from transaction where customer_id =" + cust_Id;
        }else {
            query = "select * from transaction";
        }

        boolean b = false;
        try (ConnectionHandler rs = new ConnectionHandler(query)){
            Transaction tt;
            while(rs.next()){
                tt = new Transaction();

                tt.setSenderAccNo(rs.getInt(1));
                tt.setReceiverAccNo(rs.getInt(2));
                tt.setTransactionID(rs.getLong(3));
                tt.setTransactionAmt(rs.getInt(4));
                tt.setTranactionTime(rs.getString(5));
                tt.setCustomerId(cust_Id);

                Operations.printTransaction(tt);
                b=true;

            }
        }catch (SQLException e){
            System.out.println(e + " caught at get all transaction");
        }
        if(!b){
            System.out.println("No transactions made");
        }

    }


}
