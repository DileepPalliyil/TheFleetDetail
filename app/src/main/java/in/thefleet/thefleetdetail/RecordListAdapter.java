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

public class RecordListAdapter extends ArrayAdapter<String> {

    //private final Activity context;
    private Context context;
    private final List<String>  itemname;
 //   private final Integer[] imgid;
    private final String[] itemheader;

    public RecordListAdapter(Context context, List<String> itemname,  String[] itemheader) {
        super(context, R.layout.item_record, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
       // this.imgid=imgid;
        this.itemheader=itemheader;
    }

    public View getView(int position, View view, ViewGroup parent) {
       // LayoutInflater inflater=context.getLayoutInflater();

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.item_record, null,true);

        TextView txtDetails = (TextView) rowView.findViewById(R.id.textRDetails);
        ImageView imageRecord = (ImageView) rowView.findViewById(R.id.recordIcon);
        TextView textRecord = (TextView) rowView.findViewById(R.id.textRecord);

        txtDetails.setText(itemname.get(position));
        imageRecord.setImageResource(R.drawable.ic_launcher_file);
        textRecord.setText(itemheader[position]);
        return rowView;
    };
}
