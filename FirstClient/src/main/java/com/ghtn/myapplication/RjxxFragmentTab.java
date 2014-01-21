package com.ghtn.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Administrator on 14-1-9.
 */
public class RjxxFragmentTab extends Fragment implements OnRefreshListener, AdapterView.OnItemClickListener {
    private ListView listView;

    private static final String TAG = "RjxxFragmentTab";

    private LinkedList<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;

    private ProgressBar progressBar;

    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rjxxtab_aqyh, container, false);

        if (view != null) {
            listView = (ListView) view.findViewById(R.id.baseInfoList);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }

        progressBar.setVisibility(View.VISIBLE);
        dataList = new LinkedList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("infoid", 1);
        map.put("infoname", "正常");
        dataList.add(map);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("infoid", 2);
        map2.put("infoname", "代班");
        dataList.add(map2);

        simpleAdapter = new SimpleAdapter(getActivity(),
                dataList,
                R.layout.listview_item,
                new String[]{"infoid", "infoname"},
                new int[]{R.id.tv_item_id, R.id.tv_item_name});

        listView.setAdapter(simpleAdapter);

        progressBar.setVisibility(View.GONE);

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

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idText = (String) ((TextView) view.findViewById(R.id.tv_item_id)).getText();

        Intent intent = new Intent();
        intent.putExtra("baseInfo", "rjxx");
        intent.putExtra("typeId", Integer.parseInt(idText));
        intent.setClass(getActivity(), AqyhDetailActivity.class);

        startActivity(intent);
    }
}
