package employee;

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

}
