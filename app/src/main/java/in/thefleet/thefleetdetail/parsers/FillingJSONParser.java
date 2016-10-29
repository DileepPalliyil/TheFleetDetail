package in.thefleet.thefleetdetail.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.thefleet.thefleetdetail.model.Filling;

public class FillingJSONParser {

    public static List<Filling> parseFeed(String content) {
        //Proceed only if url retrieves data for not getting null pointer exception
        if (content != null) {

            try {
                JSONObject jobj = new JSONObject(content);

                List<Filling> fillingList = new ArrayList<>();
                JSONArray ar = jobj.optJSONArray("FillingReportResult");
                for (int i = 0; i < ar.length(); i++) {

                    JSONObject obj = ar.getJSONObject(i);
                    Filling filling = new Filling();

                    filling.setFleet_ID(obj.getInt("Fleet_ID"));
                    filling.setStation_Name(obj.getString("Station_Name"));
                    filling.setOpening_KM(obj.getInt("Opening_KM"));
                    filling.setClosing_KM(obj.getInt("Closing_KM"));
                    filling.setHSD_Qty(obj.getInt("HSD_Qty"));
                    filling.setFilling_Date(obj.getString("Filling_Date"));
                    filling.setStation_Invoice(obj.getInt("Station_Invoice"));
                    filling.setDriver_Name(obj.getString("Driver_Name"));
                    filling.setLocation_Name(obj.getString("Location_Name"));

                    fillingList.add(filling);
                }

                return fillingList;

            }catch(JSONException e){
                Log.d("FillingJSONParser", "In Exception");
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }

    }
}
