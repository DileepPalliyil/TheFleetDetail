package in.thefleet.thefleetdetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class RecordViewBL extends AppCompatActivity  {


    ProgressDialog prDialog;

    private SharedPreferences groupNm;
    private String glgroup;
    private SharedPreferences regNo;

    private String req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_view_bl);

        groupNm = getSharedPreferences(MainActivity.GROUP_NAME, Context.MODE_PRIVATE);
        glgroup = groupNm.getString("groupNm","NA");

        req = getIntent().getStringExtra(FleetRecord.VIEW_REQ);

        ActionBar actionBar = getSupportActionBar();
        regNo = getSharedPreferences(MainActivity.FLEET_REGNO, Context.MODE_PRIVATE);

        actionBar.setTitle(req);

        callPDFView();

    }
    private void callPDFView() {
        try {

            if (req != null) {
                String pdf = MainActivity.PHOTOS_BASE_URL + glgroup + "/" + req;
                WebView webView=new WebView(RecordViewBL.this);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.setWebViewClient(new MyWebViewClient());
                webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+ pdf);
                setContentView(webView);
            } else {
                Toast.makeText(getApplicationContext(),"No file retrieved for this vehicle.",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CallPDFView", Log.getStackTraceString(e));
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(RecordViewBL.this);
            prDialog.setMessage("Please wait ...");
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (prDialog != null) {
                prDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Toast.makeText(getApplicationContext(), "Your Internet Connection May not be active Or " + error , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    }
