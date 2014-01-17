package com.ghtn.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

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
 * Created by Administrator on 14-1-9.
 */
public class YhFragmentTab extends Fragment implements OnRefreshListener, AbsListView.OnScrollListener {

    private ListView listView;

    private static final String TAG = "YhFragmentTab";

    private ArrayList<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;

    private ProgressBar progressBar;

    private PullToRefreshLayout mPullToRefreshLayout;

    private AsyncHttpClient client = new AsyncHttpClient();

    private int visibleThreshold = 10;
    private int currentPage = 1;
    private int previousTotal = 0;
    private boolean loading = true;

    private boolean scrollLoaded = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_yhtab_aqyh, container, false);

        if (view != null) {
            listView = (ListView) view.findViewById(R.id.baseInfoList);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }

        progressBar.setVisibility(View.VISIBLE);
        client.get(ConstantUtil.BASE_URL + "/baseInfo/1", new JsonHttpResponseHandler() {
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

        return view;
    }

    @Override
    public void onRefreshStarted(View view) {
        client.get(ConstantUtil.BASE_URL + "/baseInfo/1", new JsonHttpResponseHandler() {
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
       /* Log.d(TAG, "firstVisibleItem = " + firstVisibleItem + ", visibleItemCount = " + visibleItemCount + ", totalItemCount = " + totalItemCount);
        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            Toast.makeText(getActivity(), "到底了!!", Toast.LENGTH_SHORT).show();
        }*/
/*
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }*/


       /* if(scrollLoaded && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount){
            scrollLoaded = false;

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    currentPage ++;
                    try {
                        Thread.sleep(3000);

                        for (int i = 0; i < 15; i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("infoid", i + 1);
                            map.put("infoname", "test" + (i + 1));

                            dataList.addLast(map);
                        }
                    } catch (Exception e) {
//                        loading = false;
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    simpleAdapter.notifyDataSetChanged();
//                    loading = true;
                    scrollLoaded = true;
                }
            }.execute();
        }*/

    }

    private void showList() {

    }
}
