package com.ghtn.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
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
public class AqyhYhDetailActivity extends ActionBarActivity {

    private AsyncHttpClient client = new AsyncHttpClient();

    private ProgressBar progressBar;

    private TextView yhputinid;
    private TextView typename;
    private TextView levelname;
    private TextView banci;
    private TextView intime;
    private TextView status;
    private TextView jctypeDesc;
    private TextView remarks;
    private TextView xqdate;
    private TextView xqbanci;
    private TextView maindeptname;
    private TextView zrdeptname;
    private TextView zrpername;
    private TextView placename;
    private TextView yhcontent;
    private TextView yqcs;
    private TextView isfine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqyh_yh_detail);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        // 初始化textView
        yhputinid = (TextView) findViewById(R.id.yhputinid);
        typename = (TextView) findViewById(R.id.typename);
        levelname = (TextView) findViewById(R.id.levelname);
        banci = (TextView) findViewById(R.id.banci);
        intime = (TextView) findViewById(R.id.intime);
        status = (TextView) findViewById(R.id.status);
        jctypeDesc = (TextView) findViewById(R.id.jctypeDesc);
        remarks = (TextView) findViewById(R.id.remarks);
        xqdate = (TextView) findViewById(R.id.xqdate);
        xqbanci = (TextView) findViewById(R.id.xqbanci);
        maindeptname = (TextView) findViewById(R.id.maindeptname);
        zrdeptname = (TextView) findViewById(R.id.zrdeptname);
        zrpername = (TextView) findViewById(R.id.zrpername);
        placename = (TextView) findViewById(R.id.placename);
        yhcontent = (TextView) findViewById(R.id.yhcontent);
        yqcs = (TextView) findViewById(R.id.yqcs);
        isfine = (TextView) findViewById(R.id.isfine);

        // 如果id > 0, 通过服务器得到详细数据
        int id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            // 通过id从服务器得到详细数据
            String url = ConstantUtil.BASE_URL + "/yhinput/" + id;
            progressBar.setVisibility(View.VISIBLE);
            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200 && response != null && response.length() > 0) {
                        try {
                            yhputinid.setText(response.getInt("yhputinid") + "");
                            typename.setText(StringUtil.getStringValue(response.getString("typename")));
                            levelname.setText(StringUtil.getStringValue(response.getString("levelname")));
                            banci.setText(StringUtil.getStringValue(response.getString("banci")));
                            intime.setText(StringUtil.getStringValue(response.getString("intime")));
                            status.setText(StringUtil.getStringValue(response.getString("status")));
                            jctypeDesc.setText(StringUtil.getStringValue(response.getString("jctypeDesc")));
                            remarks.setText(StringUtil.getStringValue(response.getString("remarks")));
                            xqdate.setText(StringUtil.getStringValue(response.getString("xqdate")));
                            xqbanci.setText(StringUtil.getStringValue(response.getString("xqbanci")));
                            maindeptname.setText(StringUtil.getStringValue(response.getString("maindeptname")));
                            zrdeptname.setText(StringUtil.getStringValue(response.getString("zrdeptname")));
                            zrpername.setText(StringUtil.getStringValue(response.getString("zrpername")));
                            placename.setText(StringUtil.getStringValue(response.getString("placename")));
                            yhcontent.setText(StringUtil.getStringValue(response.getString("yhcontent")));
                            yqcs.setText(StringUtil.getStringValue(response.getString("yqcs")));
                            isfine.setText(StringUtil.getStringValue(response.getString("isfine")));

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(AqyhYhDetailActivity.this, "请求服务器发生错误或者没有找到数据!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "id错误!!", Toast.LENGTH_LONG).show();
        }
    }
}