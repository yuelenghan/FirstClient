package com.ghtn.myapplication;

import android.content.Intent;
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
import java.util.List;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Administrator on 14-1-9.
 */
public class SwFragmentTab extends Fragment implements OnRefreshListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private List<Map<String, Object>> dataList;
    private static final String TAG = "SwFragmentTab";

    private SimpleAdapter simpleAdapter;

    private ProgressBar progressBar;

    private PullToRefreshLayout mPullToRefreshLayout;

    private AsyncHttpClient client = new AsyncHttpClient();
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swtab_aqyh, container, false);

        if (view != null) {
            listView = (ListView) view.findViewById(R.id.baseInfoList);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }

        progressBar.setVisibility(View.VISIBLE);
        client.get(ConstantUtil.BASE_URL + "/baseInfo/102", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                dataList = new ArrayList<>();
                if (statusCode == 200 && response != null && response.length() > 0) {
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

                    simpleAdapter = new SimpleAdapter(getActivity(),
                            dataList,
                            R.layout.listview_item,
                            new String[]{"infoid", "infoname"},
                            new int[]{R.id.tv_item_id, R.id.tv_item_name});

                    listView.setAdapter(simpleAdapter);

                    progressBar.setVisibility(View.GONE);
                }

            }
        });

        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_layout);

        ActionBarPullToRefresh.from(getActivity())
                // Mark All Children as pullable
                .allChildrenArePullable()
                        // Set the OnRefreshListener
                .listener(this)
                        // Finally commit the setup to our PullToRefreshLayout
                .setup(mPullToRefreshLayout);


        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onRefreshStarted(View view) {
        client.get(ConstantUtil.BASE_URL + "/baseInfo/102", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                dataList.clear(); // 清空dataList
                if (statusCode == 200 && response != null && response.length() > 0) {
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

                    // 把数据变更的情况告知数据适配器
                    simpleAdapter.notifyDataSetChanged();

                    // 刷新完成
                    mPullToRefreshLayout.setRefreshComplete();

                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idText = (String) ((TextView) view.findViewById(R.id.tv_item_id)).getText();

        Intent intent = new Intent();
        intent.putExtra("baseInfo", "sw");
        intent.putExtra("typeId", Integer.parseInt(idText));
        intent.setClass(getActivity(), AqyhDetailActivity.class);

        startActivity(intent);
    }
}
