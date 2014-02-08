package com.ghtn.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ghtn.util.ConstantUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Administrator on 14-1-22.
 */
public class FragmentTab extends Fragment implements OnRefreshListener, AdapterView.OnItemClickListener {
    private ListView listView;

    private TextView errorMsg;

    private String TAG = "FragmentTab";

    private ArrayList<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;

    private ProgressBar progressBar;

    private PullToRefreshLayout mPullToRefreshLayout;

    private AsyncHttpClient client = new AsyncHttpClient();

    private int resource;
    private String url;
    private String baseInfo;

    private boolean dataRemote = true;

    public FragmentTab() {
        System.out.println("创建一个Tab!!");
    }

    public FragmentTab(String baseInfo) {
        this.baseInfo = baseInfo;
        if (baseInfo.equals("yh")) {
            this.TAG = "YhFragmentTab";
            this.resource = R.layout.fragment_yhtab_aqyh;
            this.url = ConstantUtil.BASE_URL + "/baseInfo/1";
        }
        if (baseInfo.equals("sw")) {
            this.TAG = "SwFragmentTab";
            this.resource = R.layout.fragment_swtab_aqyh;
            this.url = ConstantUtil.BASE_URL + "/baseInfo/102";
        }
        if (baseInfo.equals("rjxx")) {
            this.TAG = "RjxxFragmentTab";
            this.resource = R.layout.fragment_rjxxtab_aqyh;
            this.dataRemote = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(resource, container, false);

        if (view != null) {
            listView = (ListView) view.findViewById(R.id.baseInfoList);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            errorMsg = (TextView) view.findViewById(R.id.errorMsg);
            mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_layout);
        }

        progressBar.setVisibility(View.VISIBLE);
        client.setTimeout(10000); // 10秒超时

        initDataList();

        if (dataRemote) {
            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    progressBar.setVisibility(View.GONE);

                    if (statusCode == 200 && response != null && response.length() > 0) {
                        errorMsg.setVisibility(View.GONE);
                        // 准备listView数据
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json = response.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("infoid", json.getInt("infoid"));
                                map.put("infoname", json.getString("infoname"));

                                dataList.add(map);
                            } catch (JSONException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    } else {
                        String errorText = getResources().getString(R.string.request_status_error);
                        errorMsg.setText(String.format(errorText, statusCode));
                        errorMsg.setVisibility(View.VISIBLE);
                    }

                    dataListChanged();

                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    errorMsg.setText(R.string.request_error);
                    errorMsg.setVisibility(View.VISIBLE);

                    dataListChanged();

                    Log.e(TAG, e.toString());

                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("infoid", 1);
            map.put("infoname", "正常");
            dataList.add(map);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("infoid", 2);
            map2.put("infoname", "代班");
            dataList.add(map2);

            progressBar.setVisibility(View.GONE);

            dataListChanged();
        }


        // 设置下拉刷新
        ActionBarPullToRefresh.from(getActivity())
                // Mark All Children as pullable
                .allChildrenArePullable()
                        // Set the OnRefreshListener
                .listener(this)
                        // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);

        // 设置点击事件
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onRefreshStarted(View view) {
        progressBar.setVisibility(View.VISIBLE);
        initDataList();

        if (dataRemote) {
            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, JSONArray response) {
                    // 刷新完成
                    mPullToRefreshLayout.setRefreshComplete();
                    progressBar.setVisibility(View.GONE);

                    if (statusCode == 200 && response != null && response.length() > 0) {
                        errorMsg.setVisibility(View.GONE);
                        // 准备listView数据
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json = response.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("infoid", json.getInt("infoid"));
                                map.put("infoname", json.getString("infoname"));

                                dataList.add(map);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "json错误!!");
                            }
                        }
                    } else {
                        String errorText = getResources().getString(R.string.request_status_error);
                        errorMsg.setText(String.format(errorText, statusCode));
                        errorMsg.setVisibility(View.VISIBLE);
                    }

                    dataListChanged();
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    mPullToRefreshLayout.setRefreshComplete();
                    progressBar.setVisibility(View.GONE);

                    errorMsg.setText(R.string.request_error);
                    errorMsg.setVisibility(View.VISIBLE);

                    Log.e(TAG, e.toString());

                    dataListChanged();
                }
            });
        } else {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(2000);
                        Map<String, Object> map = new HashMap<>();
                        map.put("infoid", 1);
                        map.put("infoname", "正常");
                        dataList.add(map);

                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("infoid", 2);
                        map2.put("infoname", "代班");
                        dataList.add(map2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    mPullToRefreshLayout.setRefreshComplete();
                    errorMsg.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    dataListChanged();
                }
            }.execute();
        }

    }

    /**
     * 初始化dataList
     */
    private void initDataList() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        } else {
            dataList.clear(); // 清空dataList
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
            simpleAdapter = new SimpleAdapter(getActivity(),
                    dataList,
                    R.layout.listview_item,
                    new String[]{"infoid", "infoname"},
                    new int[]{R.id.tv_item_id, R.id.tv_item_name});

            listView.setAdapter(simpleAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idText = (String) ((TextView) view.findViewById(R.id.tv_item_id)).getText();

        Intent intent = new Intent();
        intent.putExtra("baseInfo", baseInfo);
        intent.putExtra("typeId", Integer.parseInt(idText));
        intent.setClass(getActivity(), AqyhDetailActivity.class);

        startActivity(intent);

        // 覆写切换动画，第一个参数进入, 第二个参数推出
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}
