package in.thefleet.thefleetdetail.model;

public class Record {
    private String Insh_End_Date;
    private String Fitness_To_Date;
    private String Permit_To_Date;
    private String Tax_To_Date;
    private String Filling_Date;
    private int Filling_Closing_KM;
    private String Service_Date;
    private String RouteFrom;
    private String Ins_Company;
    private double Ins_Premium;
    private double Sum_Insured;
    private String Ins_Policy;

    public String getIns_Company() {
        return Ins_Company;
    }

    public void setIns_Company(String ins_Company) {
        Ins_Company = ins_Company;
    }

    public double getIns_Premium() {
        return Ins_Premium;
    }

    public void setIns_Premium(double ins_Premium) {
        Ins_Premium = ins_Premium;
    }

    public double getSum_Insured() {
        return Sum_Insured;
    }

    public void setSum_Insured(double sum_Insured) {
        Sum_Insured = sum_Insured;
    }

    public String getIns_Policy() {
        return Ins_Policy;
    }

    public void setIns_Policy(String ins_Policy) {
        Ins_Policy = ins_Policy;
    }


    public String getInsh_End_Date() {
        return Insh_End_Date;
    }

    public void setInsh_End_Date(String insh_End_Date) {
        Insh_End_Date = insh_End_Date;
    }

    public String getFitness_To_Date() {
        return Fitness_To_Date;
    }

    public void setFitness_To_Date(String fitness_To_Date) {
        Fitness_To_Date = fitness_To_Date;
    }

    public String getPermit_To_Date() {
        return Permit_To_Date;
    }

    public void setPermit_To_Date(String permit_To_Date) {
        Permit_To_Date = permit_To_Date;
    }

    public String getTax_To_Date() {
        return Tax_To_Date;
    }

    public void setTax_To_Date(String tax_To_Date) {
        Tax_To_Date = tax_To_Date;
    }

    public String getFilling_Date() {
        return Filling_Date;
    }

    public void setFilling_Date(String filling_Date) {
        Filling_Date = filling_Date;
    }

    public int getFilling_Closing_KM() {
        return Filling_Closing_KM;
    }

    public void setFilling_Closing_KM(int filling_Closing_KM) {
        Filling_Closing_KM = filling_Closing_KM;
    }

    public String getService_Date() {
        return Service_Date;
    }

    public void setService_Date(String service_Date) {
        Service_Date = service_Date;
    }

    public String getRouteFrom() {
        return RouteFrom;
    }

    public void setRouteFrom(String routeFrom) {
        RouteFrom = routeFrom;
    }
}
