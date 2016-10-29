package in.thefleet.thefleetdetail.model;

public class Filling {
    private String Station_Name;
    private int Opening_KM;
    private int Closing_KM;
    private String Driver_Name;
    private String Filling_Date;
    private int Fleet_ID;
    private int Station_Invoice;
    private int HSD_Qty;

    public String getLocation_Name() {
        return Location_Name;
    }

    public void setLocation_Name(String location_Name) {
        Location_Name = location_Name;
    }

    private String Location_Name;

    public String getStation_Name() {
        return Station_Name;
    }

    public void setStation_Name(String station_Name) {
        Station_Name = station_Name;
    }

    public int getOpening_KM() {
        return Opening_KM;
    }

    public void setOpening_KM(int opening_KM) {
        Opening_KM = opening_KM;
    }

    public int getClosing_KM() {
        return Closing_KM;
    }

    public void setClosing_KM(int closing_KM) {
        Closing_KM = closing_KM;
    }

    public String getDriver_Name() {
        return Driver_Name;
    }

    public void setDriver_Name(String driver_Name) {
        Driver_Name = driver_Name;
    }

    public String getFilling_Date() {
        return Filling_Date;
    }

    public void setFilling_Date(String filling_Date) {
        Filling_Date = filling_Date;
    }

    public int getFleet_ID() {
        return Fleet_ID;
    }

    public void setFleet_ID(int fleet_ID) {
        Fleet_ID = fleet_ID;
    }

    public int getStation_Invoice() {
        return Station_Invoice;
    }

    public void setStation_Invoice(int station_Invoice) {
        Station_Invoice = station_Invoice;
    }

    public int getHSD_Qty() {
        return HSD_Qty;
    }

    public void setHSD_Qty(int HSD_Qty) {
        this.HSD_Qty = HSD_Qty;
    }
}
