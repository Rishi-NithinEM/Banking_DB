package employee;

import banking.Address;
import main.DBManager;
import main.Operations;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Employee {

    private int empID;
    private String empName;
    private long phoneNum;
    private String dob;
    private int salary;
    private long addressID;
    private EmployeeType empType;

    public int getEmployeeID() {
        return this.empID;
    }

    public void setEmployeeID(int empID) {
        this.empID = empID;
    }

    public String getEmployeeName() {
        return this.empName;
    }

    public void setEmployeeName(String empName) {
        this.empName = empName;
    }

    public EmployeeType getEmployeeType() {
        return this.empType;
    }

    public void setEmployeeType(String val) {
        this.empType = EmployeeType.valueOf(val);
    }

    public void setEmployeeType(int index) {
        this.empType = EmployeeType.values()[index];
    }

    public long getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDob() {
        return this.dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public long getEmployeeAddressID() {
        return this.addressID;
    }

    public void setEmployeeAddressID(long newEmployeeID) {
        this.addressID = newEmployeeID;
    }

    public Employee() {
    }

    public static enum EmployeeType {
        Manager,
        Accountant,
        Cashier;

        private EmployeeType() {
        }
    }


    public void createNewEmployee() throws IOException {
        Scanner sc = new Scanner(System.in);
        String[] empTypes = new String[]{"Manager", "Accountant", "Cashier"};
        while (true) {
            System.out.println("\n\nEnter the following details to create an employee");
            System.out.println("Enter the name");
            this.setEmployeeName(sc.nextLine());
            if (!Operations.checkName(this.getEmployeeName().toLowerCase())) {
                System.out.println("Enter only alphabets");
                continue;
            }

            System.out.println("Enter the employee type");

            int typeNum;
            for (typeNum = 0; typeNum < empTypes.length; ++typeNum) {
                System.out.println(typeNum + 1 + " for " + empTypes[typeNum]);
            }
            String st = sc.nextLine().trim();
            if (!Operations.isNumber(st)) {
                System.out.println("Enter numbers only");
                continue;
            } else {
                if (Integer.parseInt(st) < 0 || Integer.parseInt(st) > 3) {
                    System.out.println("Enter number between 1 - 3 only");
                    continue;
                }
            }
            typeNum = Integer.parseInt(st);
            this.setEmployeeType(typeNum - 1);
            System.out.println("Enter Phone number");
            st = sc.nextLine();
            if (!Operations.checkPhoneNumber(st)) {
                System.out.println("Enter 10 digit number");
                System.out.println("That starts with 7,8 or 9");
                continue;
            }
            this.setPhoneNum(Long.parseLong(st));
            System.out.println("Enter the date of birth as dd/mm/yyyy");
            this.setDob(sc.nextLine());
            if (!Operations.dobCheck(this.getDob())) {
                System.out.println("Print Date in the correct format dd/mm/yyyy (eg: 31/03/1997)");
                continue;
            }


            System.out.println("Enter the salary");
            st = sc.nextLine().trim();
            if (!Operations.isNumber(st)) {
                System.out.println("Enter only digits");
                continue;
            }
            this.setSalary(Integer.parseInt(st));
            this.setEmployeeAddressID(new Address().createNewAddress().getAddressID());

            if (DBManager.writeToDB(this)) {
                System.out.println("Added emp");
            } else {
                System.out.println("error in emp");
            }

            System.out.println("Your Employee ID : " + this.getEmployeeID());
            return;
        }
    }


    public void employeeFunctions() throws IOException, ParseException {


        Employee.EmployeeType st = this.getEmployeeType();

        switch (st) {
            case Manager:
                Operations.managerFunctions();
                break;
            case Accountant:
                Operations.accountantFunctions();
                break;
            case Cashier:
                System.out.println("No Function for the cashier");
                break;

        }


    }

}
