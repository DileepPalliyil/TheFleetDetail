package in.thefleet.thefleetdetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.thefleet.thefleetdetail.model.Record;
import in.thefleet.thefleetdetail.parsers.FileJSONParser;
import in.thefleet.thefleetdetail.parsers.RecordJSONParser;

public class FleetRecord extends AppCompatActivity {
    ProgressBar pb;
    private List<Record> recordList;
    List<String> list;
    ListView recordView;
    private String fileName = null;
    RecordListAdapter adp;

    public static final String VIEW_REQ = "VIEW_REQ";
    public static final String FILLING_URL = "FILLING_URL";
    public static final String SERVICE_URL = "SERVICE_URL";
    public static final String SERVICE_ITEM_URL = "SERVICE_ITEM_URL";
    public static final String TAG = "FleetRecord";

    private SharedPreferences fleetId;
    private SharedPreferences groupNm;
    private SharedPreferences simValue;
    private String glgroup;
    private String glfleetId;
    private String glSim;

    String[] itemheader={
            "Insurance",
            "Service",
            "Fuel",
            "Fitness",
            "Permit"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_record);

        list=new ArrayList<String>();
        recordView = (ListView) findViewById(R.id.listView);

        pb = (ProgressBar) findViewById(R.id.progressBar2);
        pb.setVisibility(View.INVISIBLE);

        simValue = getSharedPreferences(MainActivity.PHONE_SIM, Context.MODE_PRIVATE);
        fleetId = getSharedPreferences(MainActivity.FLEET_ID, Context.MODE_PRIVATE);
        groupNm = getSharedPreferences(MainActivity.GROUP_NAME, Context.MODE_PRIVATE);
        glSim = simValue.getString("simValue","NA");
        glfleetId = fleetId.getString("fleetId","NA");
        glgroup = groupNm.getString("groupNm","NA");

        requestData("http://thefleet.in/fleetmasterservice.svc/getFleetsRecord/"+ glSim+"/"+glfleetId);
        Log.d("Fleet Record","http://thefleet.in/fleetmasterservice.svc/getFleetsRecord/"+ glSim+"/"+glfleetId);
        //Loading Image
        ImageView image = (ImageView) findViewById(R.id.fImage);
        Picasso.with(getApplicationContext())
                .load(MainActivity.PHOTOS_BASE_URL +glgroup +"/" +getIntent().getStringExtra(MainActivity.FLEET_PHOTO))
                .placeholder(R.drawable.ic_reload_white_48dp)
                .resize(400,300) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(image);

        recordView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileUrl;
                String fileItemUrl;
                if (list.get(position).substring(0,3).equals("Ins")){
                    fileUrl = "http://thefleet.in/fleetmasterservice.svc/getInsuranceRecord/"+glSim+"/"+glfleetId;
                    requestFileName(fileUrl);
                } else if (list.get(position).substring(0,3).equals("Fit")) {
                    fileUrl = "http://thefleet.in/fleetmasterservice.svc/getFitnessRecord/"+glSim+"/"+glfleetId;
                    requestFileName(fileUrl);
                } else if (list.get(position).substring(0,3).equals("Per")) {
                    fileUrl = "http://thefleet.in/fleetmasterservice.svc/getPermitRecord/"+glSim+"/"+glfleetId;
                    requestFileName(fileUrl);
                }   else if (list.get(position).substring(0,3).equals("Tax")) {
                    fileUrl = "http://thefleet.in/fleetmasterservice.svc/getTaxRecord/"+glSim+"/"+glfleetId;
                    requestFileName(fileUrl);
                }else if (list.get(position).substring(0,3).equals("Aut")) {
                    fileUrl = "http://thefleet.in/fleetmasterservice.svc/getTaxRecord/"+glSim+"/"+glfleetId;
                    requestFileName(fileUrl);
                }else if (list.get(position).substring(0,3).equals("Fil")) {
                    fileUrl = "http://thefleet.in/fleetmasterservice.svc/getFillingReport/"+glSim+"/"+glfleetId;
                    Intent intent = new Intent(getApplicationContext(), FillingActivity.class);
                    intent.putExtra(FILLING_URL, fileUrl);
                    startActivity(intent);

                }else if (list.get(position).substring(0,3).equals("Ser")) {

                    fileUrl = "http://thefleet.in/fleetmasterservice.svc/getServiceReport/"+glSim+"/"+glfleetId;
                    fileItemUrl = "http://thefleet.in/fleetmasterservice.svc/getServiceItemsReport/"+glSim+"/"+glfleetId;
                    Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                    intent.putExtra(SERVICE_URL, fileUrl);
                    intent.putExtra(SERVICE_ITEM_URL, fileItemUrl);
                    startActivity(intent);

                }else {
                    fileUrl = null;
                }

            }
        });

    }

    private void requestFileName(String fileUrl) {
        if (fileUrl != null) {

            StringRequest request = new StringRequest(fileUrl,

                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("FleetRecordResponse",response);
                          fileName = FileJSONParser.parseFeed(response);
                          int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                          if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                              if (fileName != null) {
                                  Intent intent = new Intent(getApplicationContext(), RecordView.class);
                                  intent.putExtra(VIEW_REQ, fileName);
                                  startActivity(intent);
                                  pb.setVisibility(View.INVISIBLE);
                              } else {
                                  Toast.makeText(getApplicationContext(), "No file exists", Toast.LENGTH_LONG).show();
                              }
                          } else {
                              if (fileName != null) {
                                  Intent intent = new Intent(getApplicationContext(), RecordViewBL.class);
                                  intent.putExtra(VIEW_REQ, fileName);
                                  startActivity(intent);
                                  pb.setVisibility(View.INVISIBLE);
                              } else {
                                  Toast.makeText(getApplicationContext(),"No file exists",Toast.LENGTH_LONG).show();
                              }
                          }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                          //  Toast.makeText(getApplicationContext(), "Volley:" + error.getMessage(), Toast.LENGTH_LONG).show();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), "Network time out error in requestFileName",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(), "Authetication error in requestFileName",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(getApplicationContext(), "Server error in requestFileName",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "Network error in requestFileName",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "Parse error in requestFileName",
                                        Toast.LENGTH_LONG).show();
                            }
                            pb.setVisibility(View.INVISIBLE);
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(this);
            int socketTimeout = 60000;// seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, 2);
            request.setRetryPolicy(policy);
            queue.add(request);
            pb.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getApplicationContext(), "Url is null", Toast.LENGTH_LONG).show();
        }
    }


    private void requestData(String uri) {

        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        recordList = RecordJSONParser.parseFeed(response);
                        updateDisplay();
                        pb.setVisibility(View.INVISIBLE);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(getApplicationContext(),"Volley:"+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "Network time out error in request data",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authetication error in request data",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Server error in request data",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network error in request data",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "Parse error in request data",
                                    Toast.LENGTH_LONG).show();
                        }
                        pb.setVisibility(View.INVISIBLE);

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        int socketTimeout = 60000;// seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, 2);
        request.setRetryPolicy(policy);
        queue.add(request);
        pb.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_record, menu);
        ActionBar actionBar = getSupportActionBar();
        String regNo = getIntent().getStringExtra(MainActivity.FLEET_REGNO);
        String model = getIntent().getStringExtra(MainActivity.FLEET_MODEL);
        actionBar.setTitle(regNo+"-"+model);
        return true;
    }

    private void updateDisplay() {
        String InsFrom;
        if (!recordList.isEmpty()) {

            if (isNullOrBlank(recordList.get(0).getInsh_End_Date())) {
                recordList.get(0).getInsh_End_Date();
                list.add("Insurance Date: NA");
            } else {
                Log.d("Fleet Record ", recordList.get(0).getInsh_End_Date());
                if (recordList.get(0).getIns_Company().trim().length()>=12){
                     InsFrom = recordList.get(0).getIns_Company().trim().substring(0, 12);
                }else {
                     InsFrom = recordList.get(0).getIns_Company().trim();
                }
                list.add("Insurance Expiry Date:" + recordList.get(0).getInsh_End_Date() + "\n" +
                        "Policy Number: " + recordList.get(0).getIns_Policy() + "\n" +
                        "Insured From: " + InsFrom + "\n" +
                        "Insured Sum: " + recordList.get(0).getSum_Insured() + "\n" +
                        "Premium: " + recordList.get(0).getIns_Premium());
            }

            if (isNullOrBlank(recordList.get(0).getService_Date())) {
                list.add("Service Date: NA");

            } else {
                list.add("Service Date: " + recordList.get(0).getService_Date());

            }


            if (isNullOrBlank(recordList.get(0).getFilling_Date())) {
                list.add("Filling Date: NA ");
            } else {
                list.add("Filling Date: " + recordList.get(0).getFilling_Date() + "\n" +
                        "Filling Reading(KM): " + recordList.get(0).getFilling_Closing_KM());
            }


        /*if (isNullOrBlank(String.valueOf(recordList.get(0).getFilling_Closing_KM()))) {
            list.add("Filling Reading(KM): NA");
        } else {
            list.add("Filling Reading(KM): "+recordList.get(0).getFilling_Closing_KM());
        }*/


            if (isNullOrBlank(recordList.get(0).getFitness_To_Date())) {
                list.add("Fitness Date: NA");
            } else {
                list.add("Fitness Date: " + recordList.get(0).getFitness_To_Date());
            }


            if (isNullOrBlank(recordList.get(0).getPermit_To_Date())) {
                list.add("Permit Date: NA");
            } else {
                list.add("Permit Date: " + recordList.get(0).getPermit_To_Date());
            }

            adp = new RecordListAdapter(getApplicationContext(), list,  itemheader);
            recordView.setAdapter(adp);
        }
    }

    public static boolean isNullOrBlank(String s)
    {
        return (s==null || s.trim().equals(""));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
