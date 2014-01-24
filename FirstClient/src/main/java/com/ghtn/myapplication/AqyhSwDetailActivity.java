package com.ghtn.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ghtn.util.ConstantUtil;
import com.ghtn.util.StringUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 14-1-21.
 */
public class AqyhSwDetailActivity extends SwipeBackActivity {

    private AsyncHttpClient client = new AsyncHttpClient();

    private static final String TAG = "AqyhSwDetailActivity";

    private ProgressBar progressBar;
    private ScrollView scrollView;
    private TextView errorMsg;

    private TextView swinputid;
    private TextView typename;
    private TextView zyname;
    private TextView levelname;
    private TextView banci;
    private TextView pctime;
    private TextView pcpname;
    private TextView pcpnameNow;
    private TextView status;
    private TextView jctypeDesc;
    private TextView islearn;
    private TextView remarks;
    private TextView maindeptname;
    private TextView zrkqname;
    private TextView swpname;
    private TextView placename;
    private TextView swcontent;
    private TextView isfine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqyh_sw_detail);

        setTitle("三违详细信息");

        progressBar = (ProgressBar) findViewById(R.id.progress);
        scrollView = (ScrollView) findViewById(R.id.sw_detail_scrollView);
        errorMsg = (TextView) findViewById(R.id.errorMsg);

        // 初始化textView
        swinputid = (TextView) findViewById(R.id.swinputid);
        typename = (TextView) findViewById(R.id.typename);
        zyname = (TextView) findViewById(R.id.zyname);
        levelname = (TextView) findViewById(R.id.levelname);
        banci = (TextView) findViewById(R.id.banci);
        pctime = (TextView) findViewById(R.id.pctime);
        pcpname = (TextView) findViewById(R.id.pcpname);
        pcpnameNow = (TextView) findViewById(R.id.pcpnameNow);
        status = (TextView) findViewById(R.id.status);
        jctypeDesc = (TextView) findViewById(R.id.jctypeDesc);
        islearn = (TextView) findViewById(R.id.islearn);
        remarks = (TextView) findViewById(R.id.remarks);
        maindeptname = (TextView) findViewById(R.id.maindeptname);
        zrkqname = (TextView) findViewById(R.id.zrkqname);
        swpname = (TextView) findViewById(R.id.swpname);
        placename = (TextView) findViewById(R.id.placename);
        swcontent = (TextView) findViewById(R.id.swcontent);
        isfine = (TextView) findViewById(R.id.isfine);


        // 如果id > 0, 通过服务器得到详细数据
        int id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            // 通过id从服务器得到详细数据
            String url = ConstantUtil.BASE_URL + "/swinput/" + id;
            progressBar.setVisibility(View.VISIBLE);
            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    progressBar.setVisibility(View.GONE);
                    if (statusCode == 200 && response != null && response.length() > 0) {
                        try {
                            swinputid.setText(response.getInt("swinputid") + "");
                            typename.setText(StringUtil.getStringValue(response.getString("typename")));
                            zyname.setText(StringUtil.getStringValue(response.getString("zyname")));
                            levelname.setText(StringUtil.getStringValue(response.getString("levelname")));
                            banci.setText(StringUtil.getStringValue(response.getString("banci")));
                            pctime.setText(StringUtil.getStringValue(response.getString("pctime")));
                            pcpname.setText(StringUtil.getStringValue(response.getString("pcpname")));
                            pcpnameNow.setText(StringUtil.getStringValue(response.getString("pcpnameNow")));
                            status.setText(StringUtil.getStringValue(response.getString("status")));
                            jctypeDesc.setText(StringUtil.getStringValue(response.getString("jctypeDesc")));
                            islearn.setText(StringUtil.getStringValue(response.getString("islearn")));
                            remarks.setText(StringUtil.getStringValue(response.getString("remarks")));
                            maindeptname.setText(StringUtil.getStringValue(response.getString("maindeptname")));
                            zrkqname.setText(StringUtil.getStringValue(response.getString("zrkqname")));
                            swpname.setText(StringUtil.getStringValue(response.getString("swpname")));
                            placename.setText(StringUtil.getStringValue(response.getString("placename")));
                            swcontent.setText(StringUtil.getStringValue(response.getString("swcontent")));
                            isfine.setText(StringUtil.getStringValue(response.getString("isfine")));

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String errorText = getResources().getString(R.string.request_status_error);
                        errorMsg.setText(String.format(errorText, statusCode));
                        errorMsg.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    errorMsg.setText(R.string.request_error);
                    errorMsg.setVisibility(View.VISIBLE);

                    Log.e(TAG, e.toString());

                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(this, "id错误!!", Toast.LENGTH_LONG).show();
        }

        scrollView.setOnTouchListener(this);
    }
}
