package in.thefleet.thefleetdetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;


public class RecordView extends AppCompatActivity implements DownloadFile.Listener {
    LinearLayout root;
    PDFPagerAdapter adapter;
    RemotePDFViewPager remotePDFViewPager;
    ProgressBar pb;

    private SharedPreferences groupNm;
    private SharedPreferences regNo;
    private String glgroup;

    private String req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);

        groupNm = getSharedPreferences(MainActivity.GROUP_NAME, Context.MODE_PRIVATE);
        glgroup = groupNm.getString("groupNm","NA");

        pb = (ProgressBar) findViewById(R.id.progressBar3);
        pb.setVisibility(View.VISIBLE);

        root = (LinearLayout) findViewById(R.id.activity_record_view);

        req = getIntent().getStringExtra(FleetRecord.VIEW_REQ);


        ActionBar actionBar = getSupportActionBar();
        regNo = getSharedPreferences(MainActivity.FLEET_REGNO, Context.MODE_PRIVATE);

        actionBar.setTitle(req);

        callPDFView();
    }

    private void callPDFView() {
        try {

            if (req != null) {
                remotePDFViewPager =
                        new RemotePDFViewPager(getBaseContext(), MainActivity.PHOTOS_BASE_URL + glgroup + "/" + req, this);
                remotePDFViewPager.setId(R.id.pdfViewPager);
            } else {
                Toast.makeText(getApplicationContext(),"No file retrieved for this vehicle.",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CallPDFView", Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }
    public static void open(Context context) {
        Intent i = new Intent(context, RecordView.class);
        context.startActivity(i);
    }


    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager,
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        pb.setVisibility(View.VISIBLE);
    }
}

