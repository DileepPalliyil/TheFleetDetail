package in.thefleet.thefleetdetail;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.thefleet.thefleetdetail.model.Fleet;
import in.thefleet.thefleetdetail.online.isOnline;


public class FleetAdapter  extends ArrayAdapter<Fleet>  {

    private Context context;
    private List<Fleet> fleetList;
   // protected LruCache<Integer,Bitmap> imageCache;


    AsyncTask<FleetAndView,Void,FleetAndView> iTask = null;

    public FleetAdapter(Context context, int resource, List<Fleet> objects) {
        super(context, resource, objects);
        this.context = context;
        this.fleetList = objects;

        //final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        //final int cacheSize = maxMemory/8;
        //imageCache = new LruCache<>(cacheSize);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_fleet, parent, false);

        //Display fleet values in the TextView widget
        Fleet fleet = fleetList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(fleet.getRegNo());
        TextView tv1 = (TextView) view.findViewById(R.id.tvFleetMake);
        tv1.setText(fleet.getMakeName());
        TextView tv2 = (TextView) view.findViewById(R.id.tvFleetModel);
        tv2.setText(fleet.getModelName());

        //Display fleet photo in ImageView widget
        ImageView image = (ImageView) view.findViewById(R.id.imageView1);
        //Bitmap bitmap = imageCache.get(fleet.getFleetID());
        /*if (bitmap != null){
            image.setImageBitmap(fleet.getBitmap());
        }else {*/
            FleetAndView container = new FleetAndView();
            container.fleet = fleet;
            container.view =view;
         if (isOnline.isConnectedFast(getContext())){
             Log.d("FleetAdapter",MainActivity.PHOTOS_BASE_URL + fleet.getGroupName()+"/" +fleet.getPhoto());
             Picasso.with(getContext())
                     .load(MainActivity.PHOTOS_BASE_URL + fleet.getGroupName()+"/" +fleet.getPhoto())
                     .placeholder(R.drawable.ic_reload_white_48dp)
                     .into(image);
           // ImageLoader loader = new ImageLoader();
           // loader.execute(container);
         }else {
             Toast.makeText(getContext(), "Slow connection! Images will not be loaded.", Toast.LENGTH_LONG).show();
         }
        //}
        return view;
    }

    class FleetAndView {
        public Fleet fleet;
        public View view;
        public Bitmap bitmap;
    }

}
