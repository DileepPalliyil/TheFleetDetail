package in.thefleet.thefleetdetail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ServiceActivity extends AppCompatActivity implements ServiceFragment.serviceIdListener{

    private String serviceUrl;
    private String serviceItemUrl;

    public static final String SERVICE_URL = "SERVICE_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        serviceUrl = getIntent().getStringExtra(FleetRecord.SERVICE_URL);
        serviceItemUrl = getIntent().getStringExtra(FleetRecord.SERVICE_ITEM_URL);

        ServiceFragment serviceFragment=(ServiceFragment)getSupportFragmentManager().findFragmentByTag("servicefragment");
        if(serviceFragment==null){
            Bundle bundle = new Bundle();
            bundle.putString(SERVICE_URL,serviceUrl);
            serviceFragment=new ServiceFragment();
            serviceFragment.setArguments(bundle);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.list_container,serviceFragment,"servicefragment");
            transaction.commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Globals g = Globals.getInstance();
        setTitle(g.getRegIdSelected()+"-Service Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //This gets called by service fragment when user clicks the list item
    @Override
    public void getServiceDetails(String service_id,String vender,String skm) {
        ServiceDetailFragment detailFragment =
                (ServiceDetailFragment)getSupportFragmentManager().findFragmentById(R.id.detailcontainer);
        detailFragment.setServiceItemUrl(serviceItemUrl+"/"+service_id);
        detailFragment.setVenderName(vender);
        detailFragment.setskm(skm);
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
