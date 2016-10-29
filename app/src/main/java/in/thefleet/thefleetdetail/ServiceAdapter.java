package in.thefleet.thefleetdetail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.thefleet.thefleetdetail.model.Services;

public class ServiceAdapter extends ArrayAdapter<Services> {
    private Context context;
    private List<Services> servicesList;

    public ServiceAdapter(Context context, int resource, List<Services> objects) {
        super(context, resource, objects);
        this.context = context;
        this.servicesList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_services, parent, false);

        //Display fleet values in the TextView widget
        Services services = servicesList.get(position);
        TextView ve = (TextView) view.findViewById(R.id.textVender);
        if ( (!services.getVendor_Name().isEmpty()) && (services.getVendor_Name().trim().length()>=17)) {
            ve.setText(services.getVendor_Name().trim().substring(0, 17)+"-"+services.getLocation_Name().trim());
        }else {
            ve.setText(services.getVendor_Name().trim()+"-"+services.getLocation_Name().trim());
        }
        TextView sdt = (TextView) view.findViewById(R.id.textSDate);
        sdt.setText(services.getService_Date());
        TextView skm = (TextView) view.findViewById(R.id.textSKM);
        skm.setText(Integer.toString( services.getMeter_Reading()));
        TextView sid = (TextView) view.findViewById(R.id.textSID);
        sid.setText( String.valueOf(services.getService_ID()));

        ImageView servImage = (ImageView) view.findViewById(R.id.imageServices);
        servImage.setImageResource(R.drawable.ic_launcher);


        return view;
    }
}
