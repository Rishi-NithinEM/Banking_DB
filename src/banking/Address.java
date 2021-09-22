package banking;

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
}
