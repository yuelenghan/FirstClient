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
public class AqyhRjxxDetailActivity extends ActionBarActivity {

    private AsyncHttpClient client = new AsyncHttpClient();

    private TextView rjid;
    private TextView kqpnumber;
    private TextView kqpname;
    private TextView kqtypeDesc;
    private TextView datafromDesc;
    private TextView kqtime;
    private TextView kqdept;
    private TextView kqbenci;
    private TextView downtime;
    private TextView uptime;
    private TextView worktime;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqyh_rjxx_detail);

        setTitle("入井详细信息");

        progressBar = (ProgressBar) findViewById(R.id.progress);

        // 初始化textView
        rjid = (TextView) findViewById(R.id.rjid);
        kqpnumber = (TextView) findViewById(R.id.kqpnumber);
        kqpname = (TextView) findViewById(R.id.kqpname);
        kqtypeDesc = (TextView) findViewById(R.id.kqtypeDesc);
        datafromDesc = (TextView) findViewById(R.id.datafromDesc);
        kqtime = (TextView) findViewById(R.id.kqtime);
        kqdept = (TextView) findViewById(R.id.kqdept);
        kqbenci = (TextView) findViewById(R.id.kqbenci);
        downtime = (TextView) findViewById(R.id.downtime);
        uptime = (TextView) findViewById(R.id.uptime);
        worktime = (TextView) findViewById(R.id.worktime);

        // 如果id > 0, 通过服务器得到详细数据
        int id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            // 通过id从服务器得到详细数据
            String url = ConstantUtil.BASE_URL + "/kqRecord/" + id;
            progressBar.setVisibility(View.VISIBLE);
            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200 && response != null && response.length() > 0) {
                        try {
                            rjid.setText(response.getInt("rjid") + "");
                            kqpnumber.setText(StringUtil.getStringValue(response.getString("kqpnumber")));
                            kqpname.setText(StringUtil.getStringValue(response.getString("kqpname")));
                            kqtypeDesc.setText(StringUtil.getStringValue(response.getString("kqtypeDesc")));
                            datafromDesc.setText(StringUtil.getStringValue(response.getString("datafromDesc")));
                            kqtime.setText(StringUtil.getStringValue(response.getString("kqtime")));
                            kqdept.setText(StringUtil.getStringValue(response.getString("kqdept")));
                            kqbenci.setText(StringUtil.getStringValue(response.getString("kqbenci")));
                            downtime.setText(StringUtil.getStringValue(response.getString("downtime")));
                            uptime.setText(StringUtil.getStringValue(response.getString("uptime")));
                            worktime.setText(StringUtil.getStringValue(response.getString("worktime")));

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(AqyhRjxxDetailActivity.this, "请求服务器发生错误或者没有找到数据!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "id错误!!", Toast.LENGTH_LONG).show();
        }
    }
}
