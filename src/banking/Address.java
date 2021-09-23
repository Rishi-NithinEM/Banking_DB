package banking;

import main.DBManager;
import main.Operations;

import java.io.IOException;
import java.util.Scanner;

public class Address {

    private long addressID;
    private int buildingNo;
    private String area;
    private String city;
    private State state;
    private long pincode;

    public int getBuildingNo() {
        return this.buildingNo;
    }

    public void setBuildingNo(int buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return this.state;
    }

    public void setState(int index) {
        this.state = State.values()[index-1];
    }

    public long getPincode() {
        return this.pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public void setAddressID(long addressID) {
        this.addressID = addressID;
    }

    public long getAddressID() {
        return this.addressID;
    }

    public Address() {
    }

    private static enum State {
        STATE_1,
        STATE_2,
        STATE_3;

        private State() {
        }
    }


    public Address createNewAddress() throws IOException {
        Scanner sc = new Scanner(System.in);
        String st;
        while (true) {
            System.out.println("\nEnter the following details. flat no :");
            st = sc.nextLine().trim();
            if (!Operations.isNumber(st)) {
                System.out.println("Enter only number");
                continue;
            }
            this.setBuildingNo(Integer.parseInt(st));
            break;
        }
        System.out.println("Area or Street name");
        this.setArea(sc.nextLine());
        System.out.println("City");
        this.setCity(sc.nextLine());
        while (true) {
            System.out.println("State :");
            System.out.println("1 State_1\n2 State_2\n3 State_3");
            st = sc.nextLine().trim();
            if (!Operations.isNumber(st)) {
                System.out.println("Enter only number");
                continue;
            } else {
                if (Integer.parseInt(st) < 0 || Integer.parseInt(st) > 3) {
                    System.out.println("Enter number between 1 - 3 only");
                    continue;
                }
            }
            this.setState(Integer.parseInt(st));
            break;
        }
        while (true) {
            System.out.println("Postal code");
            st = sc.nextLine();
            if (st.length() != 6 || !Operations.isNumber(st)) {
                System.out.println("Postal code should be of 6 digits only");
                continue;
            }
            this.setPincode(Long.parseLong(st));
            break;
        }

        if (DBManager.writeToDB(this)) {
            System.out.println("Added address");
        } else {
            System.out.println("error in address");
        }

        // newAddress.setAddressID(fileHandling.getLastAddId());
        System.out.println("Your AddressId : " + this.getAddressID());
//        fileHandling.addAddresstoFile(newAddress);
        return this;
    }



}
