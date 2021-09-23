package main;

import accounts.Account;
import accounts.CurrentAccount;
import accounts.DepositAccount;
import accounts.SavingsAccount;
import banking.Address;
import banking.Transaction;
import customer.Customer;
import employee.Employee;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operations {






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


    public static void isValidCustomer() throws IOException, ParseException, SQLException {


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

        Customer cust = DBManager.getCustomer(id);
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


    public static void isValidEmployee() throws IOException, ParseException {

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

            Employee ee = DBManager.getEmployee(empID,empName);


            if (ee != null) {
                    System.out.println(ee.getEmployeeName() + "'s Employee account");
                    ee.employeeFunctions();
            } else {
                System.out.println("Invalid id or no employee found");
                return;
            }
    }








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

                        Customer cust = DBManager.getCustomer(num);


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


    public static void accountantFunctions() throws IOException, ParseException {

        while (true) {

            Scanner sc = new Scanner(System.in);

            System.out.println("\n\nEnter\n1 : to view all accounts");
            System.out.println("2 : to view all savings account");
            System.out.println("3 : to view all current account");
            System.out.println("4 : to create an employee");
            System.out.println("5 : to view all Transactions");
            System.out.println("6 : exit");
            String accType = "";
            int opt = 0;
            String st = sc.nextLine();
            try {
                opt = Integer.parseInt(st);
            } catch (Exception e) {
                System.out.println("Enter numbers between 1 - 6 only");
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
                    new Employee().createNewEmployee();
                    break;
                case 5: {
                    DBManager.getAllTransaction(-1);
                    break;
                }
                case 6:
                    return;
                default:
                    System.out.println("Enter number between 1 - 6 only");
                    continue;
            }


            if (accType != "") {
                List<Account> allAccs = DBManager.getAllAccounts(accType);
                Iterator var5 = allAccs.iterator();

                while (var5.hasNext()) {
                    Account i = (Account) var5.next();
                    System.out.println(i.getCustomerID() + " " + i.getAccountNo() + " " + i.getAccType());
                }
            }
        }
    }


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
        if (ac instanceof DepositAccount) {
            System.out.println("Enter the deposit period in months");
            ((DepositAccount) ac).setTermsInMonth(Integer.parseInt(sc.nextLine()));
        }
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

        if (DBManager.writeToDB(ac)) {
            System.out.println("Added account");
        } else {
            System.out.println("error in account");
        }


        System.out.println("Your Account Number :" + ac.getAccountNo() + "\nIFSC_CODE: " + ac.getIfscCode());
        return ac;
    }


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
            System.out.println("3 Check Transactions\n4 Exit");
            st = sc.nextLine();
            try {
                ch = Integer.parseInt(st);
            } catch (Exception e) {
                System.out.println("Enter numbers between 1 - 4 only");
                continue;
            }

            TransactionFunctions transaction = new TransactionFunctions();

            switch (ch) {
                case 1:
                    transaction.transfer(cust, tr);
                    break;
                case 2:
                    transaction.checkBalance(cust);
                    break;
                case 3:
                    DBManager.getAllTransaction(cust.getCustomerID());
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Enter number between 1- 4 only");
                    continue;


            }
        }
    }



}

