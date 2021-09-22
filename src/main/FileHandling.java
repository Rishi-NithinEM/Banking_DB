package main;

import accounts.Account;
import accounts.CurrentAccount;
import accounts.DepositAccount;
import accounts.SavingsAccount;
import banking.Address;
import banking.Transaction;
import customer.Customer;
import employee.Employee;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class FileHandling {

    public static String custPath = "customer.csv";
    public static String empPath = "employee.csv";
    public static String addPath = "address.csv";
    public static String accPath = "account.csv";
    public static String tranPath = "transaction.csv";
    public static String benePath = "beneficiary.csv";

    public static File custFile = new File(custPath);
    public static File empFile = new File(empPath);
    public static File addFile = new File(addPath);
    public static File accFile = new File(accPath);
    public static File tranFile = new File(tranPath);
    public static File beneFile = new File(benePath);


    static {
        try {
            if (!custFile.exists()) {
                custFile.createNewFile();
            }
            if (!empFile.exists()) {
                empFile.createNewFile();
            }
            if (!accFile.exists()) {
                accFile.createNewFile();
            }
            if (!addFile.exists()) {
                addFile.createNewFile();
            }
            if (!tranFile.exists()) {
                tranFile.createNewFile();
            }
            if (!beneFile.exists()) {
                beneFile.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addCustomertoFile(Customer cust) throws IOException {

        FileWriter fw = new FileWriter(custFile, true);

        fw.append(cust.getCustomerID() + "," + cust.getPassword() + "," + cust.getFirstName() + "," + cust.getLastName() + "," + cust.getPhoneNumber() + "," + cust.getDOB() + "," + cust.getAddressID() + "\n");
        fw.flush();
    }

    public void addEmployeetoFile(Employee emp) throws IOException {

        FileWriter fw = new FileWriter(empFile, true);

        fw.append(emp.getEmployeeID() + "," + emp.getEmployeeName() + "," + emp.getEmployeeType() + "," + emp.getPhoneNum() + "," + emp.getDob() + "," + emp.getSalary() + "," + emp.getEmployeeAddressID() + "\n");
        fw.flush();
    }

    public void addAddresstoFile(Address address) throws IOException {

        FileWriter fw = new FileWriter(addFile, true);

        fw.append(address.getAddressID() + "," + address.getBuildingNo() + "," + address.getArea() + "," + address.getCity() + "," + address.getState() + "," + address.getPincode() + "\n");
        fw.flush();
    }

    public void addAccounttoFile(Account acc) throws IOException {

        FileWriter fw = new FileWriter(accFile, true);

        fw.append(acc.getAccountNo() + "," + acc.getPin() + "," + acc.getCustomerID() + "," + acc.getBalance() + "," + acc.getAccType() + "," + acc.getBranchName() + "," + acc.getIfscCode() +"\n");
        fw.flush();
    }

    public void addBeneficiarytoFile(int accNo, int benNo) throws IOException {

        FileWriter fw = new FileWriter(beneFile, true);

        fw.append(accNo + "," + benNo + "\n");
        fw.flush();
    }

    public void addTransactiontoFile(Transaction tr) throws IOException {

        FileWriter fw = new FileWriter(tranFile, true);

        fw.append(tr.getTransactionID() + "," + tr.getCustomerId() + "," + tr.getSenderAccNo() + "," + tr.getReceiverAccNo() + "," + tr.getTransactionAmt() + "," + tr.getTranactionTime());
        fw.flush();
    }


    public Customer getCustomerfromFile(int custId) throws IOException {

        Customer cust = new Customer();

        BufferedReader frt = new BufferedReader(new FileReader(custFile));
        String st = frt.readLine();
        while (st != null) {

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[0]);
            if (Id == custId) {

                cust.setCustomerID(custId);
                cust.setPassword(s[1]);
                cust.setFirstName(s[2]);
                cust.setLastName(s[3]);
                cust.setPhoneNumber(Long.parseLong(s[4]));
                cust.setDOB(s[5]);
                cust.setAddressID(Long.parseLong(s[6]));

                return cust;

            }
            st = frt.readLine();

        }
        return null;
    }


    public Employee getEmployeefromFile(int empId) throws IOException {

        Employee emp = new Employee();

        BufferedReader frt = new BufferedReader(new FileReader(empFile));
        String st;
        while ((st = frt.readLine()) != null) {

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[0]);
            if (Id == empId) {

                emp.setEmployeeID(empId);
                emp.setEmployeeType(s[2]);
                emp.setEmployeeName(s[1]);
                emp.setPhoneNum(Long.parseLong(s[3]));
                emp.setDob(s[4]);
                emp.setSalary(Integer.parseInt(s[5]));
                emp.setEmployeeAddressID(Integer.parseInt(s[6]));

                return emp;

            }
        }
        return null;
    }


    public Account getAccountfromFile(int accNo) throws IOException {

        Account acc;


        BufferedReader frt = new BufferedReader(new FileReader(accFile));
        String st = frt.readLine();
        while (st != null) {

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[0]);

            if (accNo == Id) {

                String temp = s[4];

                switch (temp) {
                    case "Savings":
                        acc = new SavingsAccount();
                        break;
                    case "Current":
                        acc = new CurrentAccount();
                        break;
                    case "Deposit":
                        acc = new DepositAccount();
                        break;
                    default:
                        return null;
                }

                acc.setAccountNo(Id);
                acc.setPin(Integer.parseInt(s[1]));
                acc.setCustomerID(Integer.parseInt(s[2]));
                acc.setBalance(Integer.parseInt(s[3]));
                acc.setAccType(temp);
                acc.setBrachName(s[5]);
                acc.setIfscCode(s[6]);


                return acc;
            }
            st = frt.readLine();
        }

        return null;
    }

    public Transaction getTransactionfromFile(int custId) throws IOException, ParseException {

        Transaction tr = new Transaction();


        BufferedReader frt = new BufferedReader(new FileReader(tranFile));
        String st;
        while ((st = frt.readLine()) != null) {

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[1]);
            if (Id == custId) {

                tr.setTransactionID(Integer.parseInt(s[0]));
                tr.setCustomerId(custId);
                tr.setSenderAccNo(Integer.parseInt(s[2]));
                tr.setReceiverAccNo(Integer.parseInt(s[3]));
                tr.setTransactionAmt(Integer.parseInt(s[4]));
                tr.setTranactionTime(s[6]);

                return tr;

            }
        }


        return null;
    }

    public String getBeneficiaryfromFile(int accNo) throws IOException {
        String val="";

        BufferedReader frt = new BufferedReader(new FileReader(beneFile));
        String st;
        while ((st = frt.readLine()) != null) {

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[0]);
            if (Id == accNo) {

                val += s[1] + ";";

            }
        }
        return val;
    }


    public int getLastCustId() throws IOException {

        BufferedReader frt = new BufferedReader(new FileReader(custFile));
        String st;
        int id = 100;
        while ((st = frt.readLine()) != null) {

            id = Integer.parseInt(st.split(",")[0]);

        }
        return ++id;
    }

    public int getLastEmpId() throws IOException {

        BufferedReader frt = new BufferedReader(new FileReader(empFile));
        String st;
        int id = 0;
        while ((st = frt.readLine()) != null) {

            id = Integer.parseInt(st.split(",")[0]);

        }
        return ++id;
    }

    public int getLastAccNo() throws IOException {

        BufferedReader frt = new BufferedReader(new FileReader(accFile));
        String st;
        int id = 1000;
        while ((st = frt.readLine()) != null) {

            id = Integer.parseInt(st.split(",")[0]);

        }
        return ++id;
    }

    public int getLastAddId() throws IOException {

        BufferedReader frt = new BufferedReader(new FileReader(addFile));
        String st;
        int id = 10;
        while ((st = frt.readLine()) != null) {

            id = Integer.parseInt(st.split(",")[0]);

        }
        return ++id;
    }

    public int getLastTranId() throws IOException {

        BufferedReader frt = new BufferedReader(new FileReader(tranFile));
        String st;
        int id = 10;
        while ((st = frt.readLine()) != null) {

            id = Integer.parseInt(st.split(",")[0]);

        }
        return ++id;
    }


    public List<Customer> getCustomerList() throws IOException {


        List<Customer> customerList = new ArrayList<>();

        BufferedReader frt = new BufferedReader(new FileReader(custFile));
        String st;
        boolean b = false;
        Customer cust;
        while ((st = frt.readLine()) != null) {

            cust = new Customer();

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[0]);

            cust.setCustomerID(Id);
            cust.setPassword(s[1]);
            cust.setFirstName(s[2]);
            cust.setLastName(s[3]);
            cust.setPhoneNumber(Long.parseLong(s[4]));
            cust.setDOB(s[5]);
            cust.setAddressID(Long.parseLong(s[6]));

            customerList.add(cust);
            b = true;
            cust = null;

        }
        if (b)
            return customerList;
        return null;
    }

    public List<Employee> getEmployeeList() throws IOException {


        List<Employee> employeeList = new ArrayList<>();


        BufferedReader frt = new BufferedReader(new FileReader(empFile));
        String st;
        boolean b = false;
        Employee emp;
        while ((st = frt.readLine()) != null) {
            emp = new Employee();

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[0]);


            emp.setEmployeeID(Id);
            emp.setEmployeeType(s[2]);
            emp.setEmployeeName(s[1]);
            emp.setPhoneNum(Long.parseLong(s[3]));
            emp.setDob(s[4]);
            emp.setSalary(Integer.parseInt(s[5]));
            emp.setEmployeeAddressID(Integer.parseInt(s[6]));

            employeeList.add(emp);
            b = true;
            emp = null;

        }
        if (b)
            return employeeList;
        return null;

    }

    public List<Account> getAccountList() throws IOException {


        List<Account> accountList = new ArrayList<>();


        BufferedReader frt = new BufferedReader(new FileReader(accFile));
        String st;
        boolean b = false;
        Account acc;
        while ((st = frt.readLine()) != null) {

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[0]);

            String temp = s[4];

            switch (temp) {
                case "Savings":
                    acc = new SavingsAccount();
                    break;
                case "Current":
                    acc = new CurrentAccount();
                    break;
                case "Deposit":
                    acc = new DepositAccount();
                    break;
                default:
                    return null;
            }

            acc.setAccountNo(Id);
            acc.setPin(Integer.parseInt(s[1]));
            acc.setCustomerID(Integer.parseInt(s[2]));
            acc.setBalance(Integer.parseInt(s[3]));
            acc.setAccType(s[4]);
            acc.setBrachName(s[5]);
            acc.setIfscCode(s[6]);
            accountList.add(acc);
            b = true;

        }
        if (b)
            return accountList;
        return null;
    }

    public List<Transaction> getTransactionList() throws IOException, ParseException {

        List<Transaction> transactionList = new ArrayList();


        BufferedReader frt = new BufferedReader(new FileReader(tranFile));
        String st;
        boolean b = false;
        Transaction tr;
        while ((st = frt.readLine()) != null) {
            tr = new Transaction();

            String s[] = st.split(",");

            int Id = Integer.parseInt(s[1]);

            tr.setTransactionID(Integer.parseInt(s[0]));
            tr.setCustomerId(Id);
            tr.setSenderAccNo(Integer.parseInt(s[2]));
            tr.setReceiverAccNo(Integer.parseInt(s[3]));
            tr.setTransactionAmt(Integer.parseInt(s[4]));
            tr.setTranactionTime(s[6]);

            transactionList.add(tr);
            b = true;
            tr = null;


        }


        return null;
    }


    public void changeBalance(Account account) throws IOException {

        BufferedReader frt = new BufferedReader(new FileReader(accFile));
        String st;
        String old="";
        while ((st = frt.readLine()) != null) {

            String stp[] = st.split(",");



            if(account.getAccountNo() == Integer.parseInt(stp[0]))
            {
                stp[3]= Integer.toString(account.getBalance());
                old+=stp[0]+","+stp[1]+","+stp[2]+","+stp[3]+","+stp[4]+","+stp[5]+","+stp[6]+"\n";
            }else{
                old+=st+"\n";
            }

        }

        FileWriter fw = new FileWriter(accFile);

        fw.append(old);
        fw.flush();

    }

}
