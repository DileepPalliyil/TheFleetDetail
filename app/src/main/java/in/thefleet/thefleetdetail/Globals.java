package in.thefleet.thefleetdetail;

public class Globals{
    private static Globals instance;

    // Global variable
    private String regIdSelected;
    private String serIdSelected;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private double totalPrice;

    // Restrict the constructor from being instantiated
    private Globals(){}


    public String getRegIdSelected() {return this.regIdSelected;}

    public void setRegIdSelected(String r) {this.regIdSelected = r;}

    public String getSerIdSelected() {return this.serIdSelected;}

    public void setSerIdSelected(String s) {this.serIdSelected = s;}

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}