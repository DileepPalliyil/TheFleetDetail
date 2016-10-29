package in.thefleet.thefleetdetail.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.thefleet.thefleetdetail.Globals;
import in.thefleet.thefleetdetail.model.ServiceItem;


public class ServiceItemJSONParser {


    public static List<ServiceItem> parseFeed(String content) {
        double totalPrice =0;
        //Proceed only if url retrieves data for not getting null pointer exception
        if (content != null) {

            try {
                JSONObject jobj = new JSONObject(content);

                List<ServiceItem> serviceItemsList = new ArrayList<>();
                JSONArray ar = jobj.optJSONArray("getServiceItemsReportResult");
                for (int i = 0; i < ar.length(); i++) {

                    JSONObject obj = ar.getJSONObject(i);
                    ServiceItem serviceItem = new ServiceItem();

                    serviceItem.setDescription(obj.getString("Description"));
                    serviceItem.setPrice(obj.getInt("Price"));
                    totalPrice = totalPrice+obj.getInt("Price");

                    serviceItemsList.add(serviceItem);
                }

                Globals g = Globals.getInstance();
                g.setTotalPrice(totalPrice);

                return serviceItemsList;

            }catch(JSONException e){
                e.printStackTrace();
                Log.d("ServicesItemJSONParser", e.getMessage());
                return null;
            }

        } else {
            return null;
        }

    }
}
