package in.thefleet.thefleetdetail;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import in.thefleet.thefleetdetail.model.Fleet;
import in.thefleet.thefleetdetail.online.isOnline;
import in.thefleet.thefleetdetail.parsers.FleetJSONParser;
import in.thefleet.thefleetdetail.phone.TelephonyInfo;

public class MainActivity extends AppCompatActivity implements Filterable {

    public static final String PHOTOS_BASE_URL =
            "http://thefleet.in/Fleetmasterservice/";
    public static final String FLEETS_BASE_URL =
            "http://thefleet.in/Fleetmasterservice.svc/getFleets/";
    public static final String FLEET_ID = "FLEET_ID";
    public static final String FLEET_REGNO = "FLEET_REGNO";
    public static final String FLEET_MAKE = "FLEET_MAKE";
    public static final String FLEET_MODEL = "FLEET_MODEL";
    public static final String FLEET_IMAGE = "FLEET_IMAGE";
    public static final String GROUP_NAME = "GROUP_NAME";
    public static final String FLEET_PHOTO = "FLEET_PHOTO";
    public static final String PHONE_SIM = "PHONE_SIM";

    public String fleetUrl = null;

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    public static final String TAG = "MainActivity";

    ProgressBar pb;
    List<Fleet> fleetList;
    ListView list;
    FleetAdapter adapter;
    String imsiSIM;
    Toolbar toolbar;

    FleetAdapter fadapter;
    List<Fleet> filteredList;
    FleetsFilter mFleetsFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_reload_white_48dp);

        //Refresh list on button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                 list.setAdapter(null);
                 getPhoneSims();
            }
        });

        // Identify phone sims and get data from JSON URL
        getPhoneSims();
    }

    private void getPhoneSims() {
        Toast.makeText(MainActivity.this, "In getPhoneSims call", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestReadPhoneStatePermission();
        } else {
            Log.i(TAG,
                    "READ_PHONE_STATE permission is already been granted.");
            doPermissionGrantedTasks();
        }
        //End of checking phone sims
    }

    private void requestReadPhoneStatePermission() {
        // READ_PHONE_STATE permission has not been granted yet. Request it directly.
        Toast.makeText(MainActivity.this, "In doReadPhoneStatePermission Request it directly", Toast.LENGTH_LONG).show();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Toast.makeText(MainActivity.this, "In onRequestPermissionResult", Toast.LENGTH_LONG).show();
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            //If permision granted start calling URL and get data
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doPermissionGrantedTasks();
            } else {
                Log.d(TAG, "Permission not granted");
                alertAlert(getString(R.string.permissions_not_granted_read_phone_state));
            }
        }
    }

    //If permission denayed, show message and close the app
    private void alertAlert(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setIcon(R.drawable.ic_alert_black_18dp)
                .show();
    }

    private void doPermissionGrantedTasks() {
        Toast.makeText(this, "In doPermission Granted Tasks", Toast.LENGTH_LONG).show();

        if (isOnline.isNetworkConnected(this)) {

            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
            boolean isSIMReady = telephonyInfo.isSIM1Ready();
            //if (isSIMReady) {
                SharedPreferences sv = PreferenceManager.getDefaultSharedPreferences(this);
                String simValue = sv.getString("simValue", "1");
                String fleetTypValue = sv.getString("fleetTypValue", "all");
                Toast.makeText(this, "Sim value:" + simValue, Toast.LENGTH_LONG).show();
                if (simValue.equals("1")) {
                    imsiSIM = telephonyInfo.getImsiSIM1();
                    fleetUrl = MainActivity.FLEETS_BASE_URL + imsiSIM + "/" + fleetTypValue + "/" + "all";
                    Log.d(TAG, fleetUrl);
                    requestData(fleetUrl);
                } else if (simValue.equals("2")) {
                    imsiSIM = telephonyInfo.getImsiSIM2();
                    fleetUrl = MainActivity.FLEETS_BASE_URL + imsiSIM + "/" + fleetTypValue + "/" + "all";
                    Log.d(TAG, fleetUrl);
                    requestData(fleetUrl);
                } else {
                    imsiSIM = "357327070825555";
                    fleetUrl = MainActivity.FLEETS_BASE_URL + "357327070825555" + "/" + fleetTypValue + "/" + "all";
                    Log.d(TAG, fleetUrl);
                    requestData(fleetUrl);
                }
           /* } else {
                Toast.makeText(this, "Sim is not ready or no access.Try with different sim", Toast.LENGTH_LONG).show();
            }*/
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }
    //Volley request
    private void requestData(String uri) {
        StringRequest request = new StringRequest(uri,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        fleetList = FleetJSONParser.parseFeed(response);
                        updateDisplay();
                        pb.setVisibility(View.INVISIBLE);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this,"Volley:"+error.getMessage(),Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.INVISIBLE);
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, FleetPreferenceActivity.class);
                startActivity(i);
                return true;
            default: return super.onOptionsItemSelected(item);

        }
    }


    protected void updateDisplay() {
        //Use FleetAdapter to display data
        adapter = new FleetAdapter(this, R.layout.item_fleet, fleetList);
        list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(adapter);
        if (!fleetList.isEmpty()) {
            String header = fleetList.get(0).getHeader_Name();
            toolbar.setTitle(header);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fleet fleet = fleetList.get(position);
                callRecordActivity(position,fleet);
            }
        });
    }

    private void callRecordActivity(int position,Fleet fleet) {
        SharedPreferences fleetId = getSharedPreferences(FLEET_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor idEditor = fleetId.edit();
        idEditor.putString("fleetId",String.valueOf(fleet.getFleetID()));
        idEditor.commit();

        SharedPreferences groupNm = getSharedPreferences(GROUP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor gnmEditor = groupNm.edit();
        gnmEditor.putString("groupNm",fleet.getGroupName());
        gnmEditor.commit();

        SharedPreferences simValue = getSharedPreferences(PHONE_SIM, Context.MODE_PRIVATE);
        SharedPreferences.Editor simEditor = simValue.edit();
        simEditor.putString("simValue",imsiSIM);
        simEditor.commit();

        Intent intent = new Intent(MainActivity.this, FleetRecord.class);

        intent.putExtra(FLEET_REGNO, String.valueOf(fleet.getRegNo()));
        intent.putExtra(FLEET_MAKE, fleet.getMakeName());
        intent.putExtra(FLEET_MODEL, fleet.getModelName());
        intent.putExtra(FLEET_PHOTO, fleet.getPhoto());
       // intent.putExtra(PHONE_SIM, imsiSIM);
        startActivity(intent);
    }

    @Override
    public Filter getFilter() {
        if (mFleetsFilter == null)
            mFleetsFilter = new FleetsFilter();

        return mFleetsFilter;
    }


    private class FleetsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = fleetList;
                results.count = fleetList.size();
            } else {
                ArrayList<Fleet> filteredFleets = new ArrayList<Fleet>();

                for (Fleet f : fleetList) {
                    if (f.getRegNo().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filteredFleets.add(f);
                    }
                }
                // Finally set the filtered values and size/count
                results.values = filteredFleets;
                results.count = filteredFleets.size();
            }
            return results;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Fleet>) results.values;
           // adapter.notifyDataSetChanged();
            fadapter = new FleetAdapter(MainActivity.this, R.layout.item_fleet, filteredList);
            list = (ListView) findViewById(android.R.id.list);
            list.setAdapter(fadapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fleet fleet = filteredList.get(position);
                    callRecordActivity(position,fleet);
                }
            });

        }

    }

    public void hideSoftKeyboard() {
        EditText myFilter = (EditText) findViewById(R.id.action_search);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(myFilter.getWindowToken(),0);

    }


    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
