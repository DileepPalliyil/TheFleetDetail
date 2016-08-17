package in.thefleet.thefleetdetail;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
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
    protected LruCache<Integer,Bitmap> imageCache;


    AsyncTask<FleetAndView,Void,FleetAndView> iTask = null;

    public FleetAdapter(Context context, int resource, List<Fleet> objects) {
        super(context, resource, objects);
        this.context = context;
        this.fleetList = objects;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        imageCache = new LruCache<>(cacheSize);


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
        Bitmap bitmap = imageCache.get(fleet.getFleetID());
        if (bitmap != null){
            image.setImageBitmap(fleet.getBitmap());
        }else {
            FleetAndView container = new FleetAndView();
            container.fleet = fleet;
            container.view =view;
         if (isOnline.isConnectedFast(getContext())){
             Picasso.with(getContext())
                     .load(MainActivity.PHOTOS_BASE_URL + fleet.getGroupName()+"/" +fleet.getPhoto())
                     .placeholder(R.drawable.ic_reload_white_48dp)
                     .into(image);
           // ImageLoader loader = new ImageLoader();
           // loader.execute(container);
         }else {
             Toast.makeText(getContext(), "Slow connection! Images will not be loaded.", Toast.LENGTH_LONG).show();
         }
        }
        return view;
    }

    class FleetAndView {
        public Fleet fleet;
        public View view;
        public Bitmap bitmap;
    }


    /*private class ImageLoader extends AsyncTask<FleetAndView,Void,FleetAndView> {

        @Override
        protected FleetAndView doInBackground(FleetAndView... params) {
            FleetAndView container = params[0];
            Fleet fleet = container.fleet;

            try {
                String imageUrl = MainActivity.PHOTOS_BASE_URL + fleet.getGroupName()+"/" +fleet.getPhoto();
                Request request = new Request.Builder()
                        .url(imageUrl)
                        .build();

                Response response = client.newCall(request).execute();
                InputStream in = response.body().byteStream();

                //InputStream in = (InputStream) new URL(imageUrl).getContent();

                Bitmap bitmap = BitmapFactory.decodeStream(in);
                fleet.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FleetAndView result) {
            if (result != null) {
                ImageView image = (ImageView) result.view.findViewById(R.id.imageView1);
                image.setImageBitmap(result.bitmap);
                //  result.flower.setBitmap(result.bitmap);
                if(result.bitmap != null) {
                    imageCache.put(result.fleet.getFleetID(), result.bitmap);
                }
            }
        }
    }*/
}
