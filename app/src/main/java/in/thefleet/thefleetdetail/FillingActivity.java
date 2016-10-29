package in.thefleet.thefleetdetail;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import java.util.List;

import in.thefleet.thefleetdetail.model.Filling;
import in.thefleet.thefleetdetail.parsers.FillingJSONParser;

public class FillingActivity extends AppCompatActivity {
    private String fillingUrl;
    ProgressDialog prDialog;
    List<Filling> fillingList;
    ProgressBar pb;
    FillingAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Globals g = Globals.getInstance();
        setTitle(g.getRegIdSelected()+"-Fuel filling details");

        pb = (ProgressBar) findViewById(R.id.progressBar3);
        pb.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        fillingUrl = getIntent().getStringExtra(FleetRecord.FILLING_URL);
        requestFillingData(fillingUrl);
    }

    private void requestFillingData(String fillingUrl) {

        StringRequest request = new StringRequest(fillingUrl,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        fillingList = FillingJSONParser.parseFeed(response);
                        updateDisplay();
                        pb.setVisibility(View.INVISIBLE);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // Toast.makeText(getApplicationContext(),"Volley:"+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "Network time out error in requestFilling",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authetication error in requestFilling",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Server error in requestFilling",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network error in requestFilling",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "Parse error in requestFilling",
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

    private void updateDisplay() {
        //Use FleetAdapter to display data
        adapter = new FillingAdapter(this, R.layout.item_filling, fillingList);
        list = (ListView) findViewById(R.id.listFilling);
        list.setAdapter(adapter);
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
