package banking;

public class Branch {


    public static enum BrachName {

        Madurai("HDFC_1"),
        Chennai("HDFC_2"),
        Coimbatore("HDFC_3");


        private String IFSC;

        BrachName(String st) {
            this.IFSC = st;
        }

        public String getIFSCCode() {
            return this.IFSC;
        }
    }

}
