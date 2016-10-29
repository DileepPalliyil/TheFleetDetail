package in.thefleet.thefleetdetail.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.thefleet.thefleetdetail.model.Services;

public class ServiceJSONParser {

    public static List<Services> parseFeed(String content) {
        //Proceed only if url retrieves data for not getting null pointer exception
        if (content != null) {

            try {
                JSONObject jobj = new JSONObject(content);

                List<Services> servicesList = new ArrayList<>();
                JSONArray ar = jobj.optJSONArray("getServiceReportResult");
                for (int i = 0; i < ar.length(); i++) {

                    JSONObject obj = ar.getJSONObject(i);
                    Services services = new Services();

                    services.setFleet_ID(obj.getInt("Fleet_ID"));
                    services.setMeter_Reading(obj.getInt("Meter_Reading"));
                    services.setService_Date(obj.getString("Service_Date"));
                    services.setVendor_Name(obj.getString("Vendor_Name"));
                    services.setLocation_Name(obj.getString("Location_Name"));
                    services.setService_ID(obj.getInt("Service_ID"));

                    servicesList.add(services);
                }

                return servicesList;

            }catch(JSONException e){
                Log.d("ServicesJSONParser", "In Exception");
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }

    }
}
