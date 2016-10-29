package in.thefleet.thefleetdetail.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.thefleet.thefleetdetail.model.Record;

public class RecordJSONParser {

        public static List<Record>  parseFeed(String records) {
            //Proceed only if url retrieves data for not getting null pointer exception
            if (records != null) {

                try {
                    JSONObject jobj = new JSONObject(records);
                    List<Record> recordset = new ArrayList<>();
                    JSONArray ar = jobj.optJSONArray("getFleetsRecordResult");

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        Record record = new Record();
                        record.setInsh_End_Date(obj.getString("Insh_End_Date"));
                        record.setIns_Company(obj.getString("Ins_Company"));
                        record.setIns_Policy(obj.getString("Ins_Policy"));
                        record.setIns_Premium(obj.getDouble("Ins_Premium"));
                        record.setSum_Insured(obj.getDouble("Sum_Insured"));
                        record.setFitness_To_Date(obj.getString("Fitness_To_Date"));
                        record.setPermit_To_Date(obj.getString("Permit_To_Date"));
                        record.setTax_To_Date(obj.getString("Tax_To_Date"));
                        record.setFilling_Date(obj.getString("Filling_Date"));
                        record.setFilling_Closing_KM(obj.getInt("Filling_Closing_KM"));
                        record.setService_Date(obj.getString("Service_Date"));
                        record.setRouteFrom(obj.getString("RouteFrom"));


                        recordset.add(record);
                    }

                    return recordset;

                }catch(JSONException e){

                    e.printStackTrace();
                    Log.e("Tag", "Description", e);
                    return null;
                }

            } else {
                return null;
            }

        }

    }


