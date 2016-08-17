package in.thefleet.thefleetdetail.parsers;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.thefleet.thefleetdetail.model.File;

public class FileJSONParser {

    public static String parseFeed(String content) {
        //Proceed only if url retrieves data for not getting null pointer exception
        if (content != null) {

            try {
                JSONObject jobj = new JSONObject(content);
                String insFileName = new String();
                JSONArray ar = jobj.optJSONArray("getRecordResult");
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject obj = ar.getJSONObject(i);
                    File file = new File();
                    insFileName= file.setFile_Name(obj.getString("pdf_Name"));
                }

                return insFileName;

            }catch(JSONException e){

                e.printStackTrace();
                Log.e("FileJSONParser", Log.getStackTraceString(e));
                return null;
            }

        } else {
            return null;
        }

    }
}
