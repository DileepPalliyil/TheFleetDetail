package in.thefleet.thefleetdetail.model;

public class Services {
    private int Fleet_ID;
    private int Meter_Reading;
    private String Service_Date;
    private String Location_Name;
    private String Vendor_Name;
    private int Service_ID;

    public int getService_ID() {return Service_ID;}

    public void setService_ID(int service_ID) {
        Service_ID = service_ID;
    }

    public String getLocation_Name() {
        return Location_Name;
    }

    public void setLocation_Name(String location_Name) {
        Location_Name = location_Name;
    }




    public String getVendor_Name() {
        return Vendor_Name;
    }

    public void setVendor_Name(String vendor_Name) {
        Vendor_Name = vendor_Name;
    }


    public int getFleet_ID() {
        return Fleet_ID;
    }

    public void setFleet_ID(int fleet_ID) {
        Fleet_ID = fleet_ID;
    }

    public int getMeter_Reading() {
        return Meter_Reading;
    }

    public void setMeter_Reading(int meter_Reading) {
        Meter_Reading = meter_Reading;
    }

    public String getService_Date() {
        return Service_Date;
    }

    public void setService_Date(String service_Date) {Service_Date = service_Date;}


}
