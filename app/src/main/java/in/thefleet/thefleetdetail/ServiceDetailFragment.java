package in.thefleet.thefleetdetail;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import in.thefleet.thefleetdetail.model.ServiceItem;
import in.thefleet.thefleetdetail.parsers.ServiceItemJSONParser;

public class ServiceDetailFragment extends ListFragment {

    private static String serviceItemUrl;
    List<ServiceItem> servicesItemList;
    ServiceItemAdapter adapter;
    String vendername;
    String sKm;

   // ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.service_detail_fragment, container, false);


        return rootView;
    }


    public void setServiceItemUrl(String itemUrl){
        serviceItemUrl = itemUrl;
        requestServiceItemData(serviceItemUrl);
    }
    public void setVenderName (String venderName){
        vendername=venderName;
    }
    public void setskm (String skm){
        sKm = skm;
    }

    private void requestServiceItemData(String serviceItemUrl) {

        StringRequest request = new StringRequest(serviceItemUrl,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        servicesItemList = ServiceItemJSONParser.parseFeed(response);

                        updateDisplay();
                        hideLoadingDialog();


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                       // Toast.makeText(getContext(),"Volley:"+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), "Network time out error in requestServiceItem",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getContext(), "Authentication error in requestServiceItem",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getContext(), "Server error in requestServiceItem",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getContext(), "Network error in requestServiceItem",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getContext(), "Parse error in requestServiceItem",
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
        Globals g = Globals.getInstance();
        TextView tp = (TextView) getActivity().findViewById(R.id.textTotalPrice);
        tp.setText("Total Service Charge:Rs."+String.valueOf(g.getTotalPrice()));
        View lv = getActivity().findViewById(R.id.line);
        lv.setVisibility(View.VISIBLE);
        //Use FleetAdapter to display data
        adapter = new ServiceItemAdapter(getContext(), R.layout.item_services, servicesItemList);
        setListAdapter(adapter);
        TextView ve = (TextView) getActivity().findViewById(R.id.textVender2);

        if ( (!vendername.isEmpty()) && (vendername.trim().length()>=30)) {

            ve.setText(vendername.trim().substring(0, 30)+"("+sKm+")");
        }else {
            ve.setText(vendername+"("+sKm+")");
        }

        //Retain listfragment instance across configuration changes
        setRetainInstance(true);

    }

    public void showLoadingDialog() {

        LoadingDialogFragment fragment = (LoadingDialogFragment) getFragmentManager().findFragmentByTag(LoadingDialogFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new LoadingDialogFragment();
            fragment.setCancelable(false);
            getFragmentManager().beginTransaction()
                    .add(fragment, LoadingDialogFragment.FRAGMENT_TAG)
                    .commitAllowingStateLoss();

            // fragment.show(getSupportFragmentManager().beginTransaction(), LoadingDialogFragment.FRAGMENT_TAG);
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
