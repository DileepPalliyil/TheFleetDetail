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

import in.thefleet.thefleetdetail.model.Filling;

/**
 * Created by DILEEP on 19-10-2016.
 */
public class FillingAdapter extends ArrayAdapter<Filling> {

    private Context context;
    private List<Filling> fillingList;

    public FillingAdapter(Context context, int resource, List<Filling> objects) {
        super(context, resource, objects);
        this.context = context;
        this.fillingList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        double receivedMileage = 0;
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_filling, parent, false);

        //Display fleet values in the TextView widget
        Filling filling = fillingList.get(position);
        TextView st = (TextView) view.findViewById(R.id.textStation);
        if ( filling.getStation_Name().trim().length()>=17) {
            st.setText(filling.getStation_Name().trim().substring(0, 17)+"-"+filling.getLocation_Name().trim());
        }else {
            st.setText(filling.getStation_Name().trim()+"-"+filling.getLocation_Name().trim());
            }
        TextView ok = (TextView) view.findViewById(R.id.textOKM);
        ok.setText(Integer.toString(filling.getOpening_KM()));
        TextView ck = (TextView) view.findViewById(R.id.textCKM);
        ck.setText(Integer.toString( filling.getClosing_KM()));
        TextView qt = (TextView) view.findViewById(R.id.textQTY);
        qt.setText(Integer.toString( filling.getHSD_Qty()));
        TextView dt = (TextView) view.findViewById(R.id.textFDate);
        dt.setText(filling.getFilling_Date());
        TextView mg = (TextView) view.findViewById(R.id.textMileage);
        receivedMileage = (filling.getClosing_KM()-filling.getOpening_KM())/filling.getHSD_Qty();
        mg.setText(Double.toString( receivedMileage));
        ImageView fuelImage = (ImageView) view.findViewById(R.id.imageFilling);
        fuelImage.setImageResource(R.drawable.ic_launcher_fill);
        TextView ur = (TextView) view.findViewById(R.id.textUser);
        ur.setText(filling.getDriver_Name().trim());

        return view;
    }
}
