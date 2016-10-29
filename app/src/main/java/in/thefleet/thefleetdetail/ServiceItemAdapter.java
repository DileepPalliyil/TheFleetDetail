package in.thefleet.thefleetdetail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.thefleet.thefleetdetail.model.ServiceItem;

public class ServiceItemAdapter extends ArrayAdapter<ServiceItem> {
    private Context context;
    private List<ServiceItem> serviceItemsList;
    public static ArrayList<Double> valueslist;


    public ServiceItemAdapter(Context context, int resource, List<ServiceItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.serviceItemsList = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_service_detail, parent, false);

        //Display fleet values in the TextView widget
        ServiceItem serviceItem = serviceItemsList.get(position);
        TextView de = (TextView) view.findViewById(R.id.textSDesc);
        if ( (!serviceItem.getDescription().isEmpty()) && (serviceItem.getDescription().trim().length()>=25)) {
            de.setText(serviceItem.getDescription().trim().substring(0, 25));
        }else {
            de.setText(serviceItem.getDescription().trim());
        }
        TextView spr = (TextView) view.findViewById(R.id.textSPrice);
        spr.setText("Rs."+String.valueOf(serviceItem.getPrice()));


    return view;

    }


}
