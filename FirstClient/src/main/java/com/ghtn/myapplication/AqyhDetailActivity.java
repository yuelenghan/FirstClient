package com.ghtn.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ghtn.util.ConstantUtil;
import com.ghtn.util.StringUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Administrator on 14-1-20.
 */
public class AqyhDetailActivity extends ActionBarActivity
        implements OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final String TAG = "AqyhDetailActivity";

    private ListView listView;
    private AsyncHttpClient client = new AsyncHttpClient();
    private String url;

    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;

    private String baseInfo;
    private int typeId;

    private PullToRefreshLayout mPullToRefreshLayout;

    private ProgressBar progressBar;
    private TextView errorMsg;

    private int visibleThreshold = 10;
    private int currentPage;

    private boolean scrollLoaded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqyh_detail);

        listView = (ListView) findViewById(R.id.detailList);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

        baseInfo = getIntent().getStringExtra("baseInfo");
        typeId = getIntent().getIntExtra("typeId", 0);

        if (baseInfo != null && baseInfo.equals("yh")) {
            setTitle("隐患");
            url = ConstantUtil.BASE_URL + "/yhinput";
        }
        if (baseInfo != null && baseInfo.equals("sw")) {
            setTitle("三违");
            url = ConstantUtil.BASE_URL + "/swinput";
        }
        if (baseInfo != null && baseInfo.equals("rjxx")) {
            setTitle("入井信息");
            url = ConstantUtil.BASE_URL + "/kqRecord";
        }

        progressBar.setVisibility(View.VISIBLE);
        client.setTimeout(10000);
        currentPage = 1;
        int start = (currentPage - 1) * ConstantUtil.DETAIL_PAGE_SIZE;
        // 初始化dataList
        initDataList();
        client.get(url + "/typeId/" + typeId + "/start/" + start + "/limit/" + ConstantUtil.DETAIL_PAGE_SIZE,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        progressBar.setVisibility(View.GONE);

                        if (statusCode == 200 && response != null && response.length() > 0) {
                            errorMsg.setVisibility(View.GONE);
                            // 向dataList中增加数据
                            addDataToList(response);
                        } else {
                            errorMsg.setText("请求错误, 状态码:" + statusCode);
                            errorMsg.setVisibility(View.VISIBLE);
                        }

                        dataListChanged();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject errorResponse) {
                        errorMsg.setText("请求服务器失败!!");
                        errorMsg.setVisibility(View.VISIBLE);

                        dataListChanged();

                        Log.e(TAG, e.toString());

                        progressBar.setVisibility(View.GONE);
                    }
                });

        // 设置下拉刷新
        ActionBarPullToRefresh.from(this)
                // Mark All Children as pullable
                .allChildrenArePullable()
                        // Set the OnRefreshListener
                .listener(this)
                        // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);

        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(this);
    }

    private void initDataList() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        } else {
            dataList.clear();
        }
    }

    /**
     * dataList改变时,调用此方法
     */
    private void dataListChanged() {
        if (simpleAdapter != null && listView.getAdapter() != null) {
            // 把数据变更的情况告知数据适配器
            simpleAdapter.notifyDataSetChanged();
        } else {
            if (baseInfo != null && baseInfo.equals("yh")) {
                simpleAdapter = new SimpleAdapter(AqyhDetailActivity.this,
                        dataList,
                        R.layout.aqyh_yh_detail_listview_item,
                        new String[]{"detailId", "detailBanci", "detailIntime", "detailRemarks"},
                        new int[]{R.id.detailId, R.id.detailBanci, R.id.detailIntime, R.id.detailRemarks});
            }

            if (baseInfo != null && baseInfo.equals("sw")) {
                simpleAdapter = new SimpleAdapter(AqyhDetailActivity.this,
                        dataList,
                        R.layout.aqyh_sw_detail_listview_item,
                        new String[]{"detailId", "detailBanci", "detailIntime", "detailRemarks"},
                        new int[]{R.id.detailId, R.id.detailBanci, R.id.detailIntime, R.id.detailRemarks});
            }

            if (baseInfo != null && baseInfo.equals("rjxx")) {
                simpleAdapter = new SimpleAdapter(AqyhDetailActivity.this,
                        dataList,
                        R.layout.aqyh_rjxx_detail_listview_item,
                        new String[]{"detailId", "detailBanci", "detailIntime", "detailKqpnumber", "detailKqpname", "detailDatafromDesc"},
                        new int[]{R.id.detailId, R.id.detailBanci, R.id.detailIntime, R.id.detailKqpnumber, R.id.detailKqpname, R.id.detailDatafromDesc});
            }

            listView.setAdapter(simpleAdapter);
        }
    }

    @Override
    public void onRefreshStarted(View view) {
        initDataList();
        currentPage = 1;
        int start = (currentPage - 1) * ConstantUtil.DETAIL_PAGE_SIZE;
        client.get(url + "/typeId/" + typeId + "/start/" + start + "/limit/" + ConstantUtil.DETAIL_PAGE_SIZE,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // 刷新完成
                        mPullToRefreshLayout.setRefreshComplete();

                        if (statusCode == 200 && response != null && response.length() > 0) {
                            errorMsg.setVisibility(View.GONE);

                            // 向dataList中增加数据
                            addDataToList(response);
                        } else {
                            errorMsg.setText("请求错误, 状态码:" + statusCode);
                            errorMsg.setVisibility(View.VISIBLE);
                        }
                        dataListChanged();
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject errorResponse) {
                        mPullToRefreshLayout.setRefreshComplete();

                        errorMsg.setText("请求服务器失败!!");
                        errorMsg.setVisibility(View.VISIBLE);

                        Log.e(TAG, e.toString());

                        dataListChanged();
                    }
                });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // setOnScrollListener之后就会进入这个方法, 此时totalItemCount为0, 加载满页时才需要分页
        if (totalItemCount >= currentPage * ConstantUtil.DETAIL_PAGE_SIZE
                && scrollLoaded
                && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
            Log.d(TAG, "需要分页!!!!!!");
            scrollLoaded = false;

            currentPage++;
            int start = (currentPage - 1) * ConstantUtil.DETAIL_PAGE_SIZE;

            client.get(url + "/typeId/" + typeId + "/start/" + start + "/limit/" + ConstantUtil.DETAIL_PAGE_SIZE,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            scrollLoaded = true;
                            if (statusCode == 200 && response != null && response.length() > 0) {
                                // 向dataList中增加数据
                                addDataToList(response);

                                dataListChanged();
                            } else {
                                Log.e(TAG, "<<<<<<<<<<<<<<<< statusCode = " + statusCode + " >>>>>>>>>>>>>>>>>>>>>>>>>>");
                                Toast.makeText(AqyhDetailActivity.this, "请求新数据失败!!!", Toast.LENGTH_LONG).show();
                                currentPage = currentPage - 1; // 回到加载前的数值
                            }
                        }

                        @Override
                        public void onFailure(Throwable e, JSONObject errorResponse) {
                            Log.e(TAG, e.toString());
                            Toast.makeText(AqyhDetailActivity.this, "请求新数据失败!!!", Toast.LENGTH_LONG).show();
                            scrollLoaded = true;
                            currentPage = currentPage - 1; // 回到加载前的数值
                        }
                    });

        }
    }

    private List<Map<String, Object>> addDataToList(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();

                if (baseInfo != null && baseInfo.equals("yh")) {
                    map.put("detailId", "id: " + json.getInt("yhputinid"));
                    map.put("detailBanci", "班次: " + json.getString("banci"));
                    map.put("detailIntime", "时间: " + json.getString("intime"));
                    map.put("detailRemarks", "描述: " + StringUtil.processLongStr(json.getString("remarks"), 20));
                }

                if (baseInfo != null && baseInfo.equals("sw")) {
                    map.put("detailId", "id: " + json.getInt("swinputid"));
                    map.put("detailBanci", "班次: " + json.getString("banci"));
                    map.put("detailIntime", "时间: " + json.getString("intime"));
                    map.put("detailRemarks", "描述: " + StringUtil.processLongStr(json.getString("remarks"), 20));
                }

                if (baseInfo != null && baseInfo.equals("rjxx")) {
                    map.put("detailId", "id: " + json.getInt("rjid"));
                    map.put("detailBanci", "班次: " + json.getString("kqbenci"));
                    map.put("detailIntime", "时间: " + json.getString("kqtime"));

                    map.put("detailKqpnumber", "人员id: " + json.getString("kqpnumber"));
                    map.put("detailKqpname", "姓名: " + json.getString("kqpname"));
                    map.put("detailDatafromDesc", "数据来源: " + json.getString("datafromDesc"));
                }

                dataList.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "json错误!!");
            }
        }

        return dataList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idText = (String) ((TextView) view.findViewById(R.id.detailId)).getText();
//        Toast.makeText(AqyhDetailActivity.this, StringUtil.getIntValue(idText)+"", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("id", StringUtil.getIntValue(idText));

        if (baseInfo != null && baseInfo.equals("yh")) {
            intent.setClass(AqyhDetailActivity.this, AqyhYhDetailActivity.class);
        }
        if (baseInfo != null && baseInfo.equals("sw")) {
            intent.setClass(AqyhDetailActivity.this, AqyhSwDetailActivity.class);
        }
        if (baseInfo != null && baseInfo.equals("rjxx")) {
            intent.setClass(AqyhDetailActivity.this, AqyhRjxxDetailActivity.class);
        }

        startActivity(intent);

    }
}
