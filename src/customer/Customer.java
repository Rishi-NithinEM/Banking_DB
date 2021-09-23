package customer;


import banking.Address;
import com.google.longrunning.Operation;
import main.DBManager;
import main.Operations;

import java.io.IOException;
import java.util.Scanner;

public class Customer {

    private int customerID;
    private String customerPassword;
    private String firstName;
    private String lastName;
    private long addressID;
    private long phoneNum;
    private String dob;


    public int getCustomerID() {
        return this.customerID;
    }

    public void setCustomerID(int newCustomerID) {
        this.customerID = newCustomerID;
    }

    public void setPassword(String newPassword) {
        this.customerPassword = newPassword;
    }

    public String getPassword() {
        return this.customerPassword;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    public long getAddressID() {
        return this.addressID;
    }

    public void setAddressID(long newAddressID) {
        this.addressID = newAddressID;
    }

    public void setPhoneNumber(long newPhoneNum) {
        this.phoneNum = newPhoneNum;
    }

    public long getPhoneNumber() {
        return this.phoneNum;
    }

    public String getDOB() {
        return this.dob;
    }

    public void setDOB(String newDOB) {
        this.dob = newDOB;
    }

    public Customer() {
    }



    public void createNewCustomer() throws IOException {   //Used to get details for creating a new Customer
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n\nEnter the following details\nFirst Name : ");
            this.setFirstName(sc.nextLine());
            if (!Operations.checkName(this.getFirstName().toLowerCase())) {
                System.out.println("Enter only alphabets");
                continue;
            }
            System.out.println("Last Name : ");
            this.setLastName(sc.nextLine());
            System.out.println("Phone number : ");
            String num = sc.nextLine();
            if (Operations.checkPhoneNumber(num))
                this.setPhoneNumber(Long.parseLong(num));
            else {
                System.out.println("Enter 10 digit number");
                System.out.println("That starts with 7,8 or 9");
                continue;
            }
            System.out.println("Date of birth as dd/mm/yyyy");
            this.setDOB(sc.nextLine());
            if (!Operations.dobCheck(this.getDOB())) {
                System.out.println("Print Date in the correct format dd/mm/yyyy (eg: 31/03/1997)");
                continue;
            }
            System.out.println("Enter password");
            this.setPassword(sc.nextLine());
            if (this.getPassword().trim().equals("")) {
                System.out.println("password should not be empty");
                continue;
            }

            this.setAddressID(new Address().createNewAddress().getAddressID());
            if (DBManager.writeToDB(this)) {
                System.out.println("Added cust");
            } else {
                System.out.println("error in cust");
            }
            System.out.println("Your CustomerId is : " + this.getCustomerID());
            return;
        }
    }


}
