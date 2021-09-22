package customer;


public class Customer {

    private int customerID;
    private String customerPassword;
    private String firstName;
    private String lastName;
    private long addressID;
    private long phoneNum;
    private String dob;
//    private HashMap<Integer, Account> customerAccounts= new HashMap<>();

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

}
