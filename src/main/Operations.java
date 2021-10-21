package main;

import accounts.Account;
import accounts.CurrentAccount;
import accounts.DepositAccount;
import accounts.SavingsAccount;
import banking.Transaction;
import customer.Customer;
import employee.Employee;

import java.io.IOException;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.util.regex.*;

public class Operations {



public static DataHandler dataHandler= new DBManager();


    public static boolean checkPhoneNumber(String str) {


        Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");

        Matcher match = ptrn.matcher(str);

        return (match.find() && match.group().equals(str));


    }

    public static boolean dobCheck(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean checkName(String name) {

        if (name.trim().equals(""))
            return false;

        for (char ch : name.toCharArray()) {
            if (ch < 97 || ch > 122)
                return false;

        }

        return true;
    }

    public static boolean isNumber(String st) {

        for (char ch : st.toCharArray()) {

            if (ch < 48 || ch > 57)
                return false;

        }

        return true;

    }



//    This is used to check whether they are a customer or not
//    if they are a customer then they can perform some online function

    public static void loginCustomer() throws IOException, ParseException, SQLException {


        Scanner sc = new Scanner(System.in);
        int id;
        while (true) {
            System.out.println("Enter customer id");
            String st = sc.nextLine();
            if (!isNumber(st.trim())) {
                System.out.println("customer id contains only numbers");
                continue;
            }
            id = Integer.parseInt(st.trim());
            break;
        }
        System.out.println("Enter customer password");
        String password = sc.nextLine();

        Customer cust = dataHandler.getCustomer(id);
        if (cust == null) {
            System.out.println("No account found");
        } else {

            if(cust.getPassword().equals(password)) {
                System.out.println("found " + cust.getFirstName() + ",s Account");
                performOnlineTransaction(cust);
                return;
            }else {

                System.out.println("Wrong Password ");
            }
        }

    }

//    This is used to check where they are employee or not
//    If they are employee each type of employees are given specific tasks

    public static void loginEmployee() throws IOException, ParseException {

            Scanner sc = new Scanner(System.in);
            int empID;
            while (true) {
                System.out.println("Enter Employee ID");
                String st = sc.nextLine();
                if (!isNumber(st.trim())) {
                    System.out.println("Employee ID contains only numbers");
                    continue;
                }
                empID = Integer.parseInt(st.trim());
                break;
            }
            System.out.println("Enter Employee Name");
            String empName = sc.nextLine();

            Employee ee = dataHandler.getEmployee(empID,empName);


            if (ee != null) {
                    System.out.println(ee.getEmployeeName() + "'s Employee account");
                    ee.employeeFunctions();
            } else {
                System.out.println("Invalid id or no employee found");
                return;
            }
    }


//    This function give unique tasks for Manager employee
//    So for manager it is he can create a new Customer and Create new account

    public static void managerFunctions() throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1 :create a new Customer");
            System.out.println("2 :create a new Account\n3 : Exit");

            String st = sc.nextLine();
            int num;
            if (!isNumber(st.trim())) {
                System.out.println("Enter numbers between 1 - 3 only");
                continue;
            } else {
                num = Integer.parseInt(st);
                if (num < 0 || num > 3) {
                    System.out.println("Enter number between 1 - 3 only");
                    continue;
                }
                if (num == 1) {
                    new Customer().createNewCustomer();
                } else if (num == 2) {

                    System.out.println("Enter your Customer Id");
                    st = sc.nextLine();
                    if (!isNumber(st.trim())) {
                        System.out.println("Please enter numbers only");
                    } else {
                        num = Integer.parseInt(st);

                        Customer cust = dataHandler.getCustomer(num);


                        if (cust != null) {

                            createNewAccount(cust);
                            break;
                        } else {
                            System.out.println("Sorry No Customer Found of that Id");
                            continue;
                        }

                    }

                } else {
                    return;
                }
            }


        }

    }



//    This function is used to give unique tasks for Accountant employee
//    So for Accountant the functions are see all types of account created and see through all the transactions made
//    And also to create a new employee


    public static void accountantFunctions() throws IOException, ParseException {

        while (true) {

            Scanner sc = new Scanner(System.in);

            System.out.println("\n\nEnter\n1 : to view all accounts");
            System.out.println("2 : to view all savings account");
            System.out.println("3 : to view all current account");
            System.out.println("4: to view all Customer");
            System.out.println("5 : to create an employee");
            System.out.println("6 : to view all Transactions");
            System.out.println("7 : exit");
            String accType = "";
            int opt = 0;
            String st = sc.nextLine();
            try {
                opt = Integer.parseInt(st);
            } catch (Exception e) {
                System.out.println("Enter numbers between 1 - 7 only");
                continue;
            }
            switch (opt) {
                case 1:
                    accType = "all";
                    break;
                case 2:
                    accType = "Savings";
                    break;
                case 3:
                    accType = "Current";
                    break;
                case 4:
                    getAlLCustomer();
                    break;
                case 5:
                    new Employee().createNewEmployee();
                    break;
                case 6: {
                    Iterator itr =dataHandler.getAllTransaction(-1).listIterator();
                    while(itr.hasNext()){
                        ((Transaction)itr.next()).printTransaction();
                    }
                    break;
                }
                case 7:
                    return;
                default:
                    System.out.println("Enter number between 1 - 6 only");
                    continue;
            }


            if (accType != "") {
                List<Account> allAccs = dataHandler.getAllAccounts(accType);
                Iterator var5 = allAccs.iterator();

                while (var5.hasNext()) {
                    Account i = (Account) var5.next();
                    System.out.println(i.getCustomerID() + " " + i.getAccountNo() + " " + i.getAccType());
                }
            }
        }
    }


    public static void getAlLCustomer(){

        Scanner sc= new Scanner(System.in);
        System.out.println("1: order by customer name");
        System.out.println("2: order by customer id");
        int num= sc.nextInt();
        int order;
        String colmn ;
        if(num==1){
            colmn="cust_fname";
            System.out.println("1: Ascending order");
            System.out.println("2: Descending order");
            order=sc.nextInt();
            if(order==1){
               printAllCustomer(dataHandler.getAllCustomer(colmn,1));
            }else {
                printAllCustomer(dataHandler.getAllCustomer(colmn, -1));
            }
        }else if(num==2){

            colmn="cust_id";
            System.out.println("1: Ascending order");
            System.out.println("2: Descending order");
            order=sc.nextInt();
            if(order==1){
                printAllCustomer(dataHandler.getAllCustomer(colmn,1));
            }else {
                System.out.println("sss");
                printAllCustomer(dataHandler.getAllCustomer(colmn, -1));
            }

        }else{
            System.out.println("enter only numbers");
            return;
        }

    }

    public static void printAllCustomer(List<Customer> customerList){

        Iterator itr = customerList.listIterator();
        Customer cust;
        System.out.println("zz");
        while(itr.hasNext()){
            cust = (Customer) itr.next();
            System.out.println("Customer id: "+cust.getCustomerID());
            System.out.println("Customer first name: "+cust.getFirstName());
            System.out.println("Customer last name: "+cust.getLastName());
            System.out.println("Customer phone number: "+cust.getPhoneNumber()+"\n");
        }

    }



//    this function is used to create a new account for a customer

    public static Account createNewAccount(Customer cust)  {
        Scanner sc = new Scanner(System.in);
        Account ac;

        String[] accTypes = new String[]{"Savings", "Current", "Deposit"};
        System.out.println("Account type");

        int temp;
        for (temp = 0; temp < accTypes.length; ++temp) {
            System.out.println(temp + 1 + " " + accTypes[temp]);
        }

        temp = Integer.parseInt(sc.nextLine());

        switch (temp) {
            case 1:
                ac = new SavingsAccount();
                break;
            case 2:
                ac = new CurrentAccount();
                break;
            case 3:
                ac = new DepositAccount();
                break;
            default:
                return null;
        }

        ac.setAccType(temp - 1);
        ac.setCustomerID(cust.getCustomerID());
        System.out.println("PIN <= 9999");

        while (true) {
            ac.setPin(Integer.parseInt(sc.nextLine()));
            if (ac.getPin() <= 9999)
                break;
        }
        while (true) {
            String st;
            System.out.println("\nBranch :");
            System.out.println("1 Madurai\n2 Chennai\n3 Coimbatore");
            st = sc.nextLine().trim();
            if (!isNumber(st)) {
                System.out.println("Enter only number");
                continue;
            } else {
                if (Integer.parseInt(st) < 0 || Integer.parseInt(st) > 3) {
                    System.out.println("Enter number between 1 - 3 only");
                    continue;
                } else {

                    ac.setBranchName(Integer.parseInt(st) - 1);
                    break;
                }
            }
        }
//        ac.setAccountNo(fileHandling.getLastAccNo());
//        fileHandling.addAccounttoFile(ac);

        DataHandler.createAccount(ac);


        System.out.println("Your Account Number :" + ac.getAccountNo() + "\nIFSC_CODE: " + ac.getIfscCode());
        return ac;
    }


//    this function is used to perform online functions for a Customer
//    Here the customer can transfer amount to another account , check balance and see all transaction


    public static void performOnlineTransaction(Customer cust) throws IOException, ParseException, SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {

            Transaction tr = new Transaction();
            String[] types = new String[]{"Transfer", "Balance enquiry"};
            System.out.println("\n\nEnter transaction type");

            int ch;
            String st;
            for (ch = 0; ch < types.length; ++ch) {
                System.out.println(ch + 1 + " " + types[ch]);
            }
            System.out.println("3 Check Transactions\n4 Amount Received\n5 Check Transaction Between Dates\n6Exit");
            st = sc.nextLine();
            try {
                ch = Integer.parseInt(st);
            } catch (Exception e) {
                System.out.println("Enter numbers between 1 - 6 only");
                continue;
            }

            DBTransactionFunctions transaction = new DBTransactionFunctions();

            switch (ch) {
                case 1:
                    transaction.transfer(cust, tr);
                    break;
                case 2:
                    transaction.checkBalance(cust);
                    break;
                case 3:
                    Iterator itr = dataHandler.getAllTransaction(cust.getCustomerID()).listIterator();
                    while(itr.hasNext()){
                        ((Transaction)itr.next()).printTransaction();
                    }
                    break;
                case 4:
                    System.out.println("Enter your account number");
                    int num = sc.nextInt();
                    dataHandler.amountReceived(num,cust);
                    break;
                case 5:
                    printTransactionbetweendates(cust);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Enter number between 1- 6 only");
                    continue;


            }
        }
    }


    public static void printTransactionbetweendates(Customer cust) throws ParseException {

        Scanner sc = new Scanner(System.in);
        Date d1;
        Date d2;
        List<Transaction> tr;
        while(true) {
            System.out.println("Enter first date lesser than the secound\nFrom date as dd/mm/yyyy");
            String from = sc.nextLine();
            if (from.length() != 0) {
                if (!dobCheck(from)) {
                    System.out.println("Enter Date in the correct format dd/mm/yyyy (eg: 31/03/1997)");
                    continue;
                }
            }
            System.out.println("TO date as dd/mm/yyyy");
            String to = sc.nextLine();
            if (to.length() != 0) {
                if (!dobCheck(to)) {
                    System.out.println("Enter Date in the correct format dd/mm/yyyy (eg: 31/03/1997)");
                    continue;
                }
            }
            if (from.length() != 0 && to.length() != 0) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

                if(from.length()==0 && to.length()!=0){
                    d1 = formatter1.parse("01/01/1900");
                    d2 = formatter1.parse(to);
                }else if(to.length()==0 && from.length()!=0){
                    d1 = formatter1.parse(from);
                    d2 = new Date();
                }else {
                    d1 = formatter1.parse(from);
                    d2 = formatter1.parse(to);
                }
                if (d2.before(d1)) {
                    continue;
                } else
                    System.out.println("From date should be before to date");
                    break;
            }else{
                System.out.println("Both dates cant be null");
            }
        }
        tr = dataHandler.getTransationsbetweenTime(d1,d2);
        Iterator itr = tr.listIterator();
        boolean b = false;
        while(itr.hasNext()){

            Transaction tt= (Transaction)itr.next();
            if(tt.getCustomerId()==cust.getCustomerID()){
                tt.printTransaction();
                b=true;
            }
        }
        if(!b){
            System.out.println("No transaction made in that time zone");
        }

    }



}

