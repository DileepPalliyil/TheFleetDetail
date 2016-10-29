package in.thefleet.thefleetdetail;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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

import in.thefleet.thefleetdetail.model.Services;
import in.thefleet.thefleetdetail.parsers.ServiceJSONParser;


public class ServiceFragment extends ListFragment {
    private String serviceUrl;
    List<Services> servicesList;
    ServiceAdapter adapter;
    ListView list;

    serviceIdListener activityCommander;
    public interface serviceIdListener{
        public void getServiceDetails(String service_id,String vender,String skm);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            activityCommander=(serviceIdListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.service_fragment,container,false);
        String serviceUrl = getArguments().getString("SERVICE_URL");

        requestServiceData(serviceUrl);

        return rootView;
    }

    private void requestServiceData(String serviceUrl) {

        StringRequest request = new StringRequest(serviceUrl,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        servicesList = ServiceJSONParser.parseFeed(response);

                         Log.d("ServiceFragment","Service list: "+servicesList.toString());
                        updateDisplay();
                        hideLoadingDialog();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // Toast.makeText(getContext(),"Volley:"+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "Network time out error in requestService",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getContext(), "Authentication error in requestService",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getContext(), "Server error in requestService",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getContext(), "Network error in requestService",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getContext(), "Parse error in requestService",
                                    Toast.LENGTH_LONG).show();
                        }
                        hideLoadingDialog();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        int socketTimeout = 60000;// seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, 2);
        request.setRetryPolicy(policy);
        queue.add(request);
        showLoadingDialog();
    }

    private void updateDisplay() {
        //Use FleetAdapter to display data
        adapter = new ServiceAdapter(getContext(), R.layout.item_services, servicesList);
        setListAdapter(adapter);
        //Retain listfragment instance across configuration changes
        setRetainInstance(true);
    }

    public void onListItemClick(ListView l,View view, int position, long id) {
        view.setSelected(true);
        ViewGroup viewGroup=(ViewGroup) view;
        TextView ven = (TextView) viewGroup.findViewById(R.id.textVender);
        TextView sid = (TextView) viewGroup.findViewById(R.id.textSID);
        TextView skm = (TextView) viewGroup.findViewById(R.id.textSKM);
        activityCommander.getServiceDetails(sid.getText().toString(),ven.getText().toString(),skm.getText().toString());
    }

    public void showLoadingDialog() {

        LoadingDialogFragment fragment = (LoadingDialogFragment) getFragmentManager().findFragmentByTag(LoadingDialogFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new LoadingDialogFragment();
            fragment.setCancelable(false);
            getFragmentManager().beginTransaction()
                    .add(fragment, LoadingDialogFragment.FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        }
    }

    public void hideLoadingDialog() {
        LoadingDialogFragment fragment = (LoadingDialogFragment) getFragmentManager().findFragmentByTag(LoadingDialogFragment.FRAGMENT_TAG);
        if (fragment != null) {
            // fragment.dismissAllowingStateLoss();
            getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

}
