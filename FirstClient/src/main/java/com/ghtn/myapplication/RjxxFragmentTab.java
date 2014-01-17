package com.ghtn.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Administrator on 14-1-9.
 */
public class RjxxFragmentTab extends Fragment implements OnRefreshListener {
    private ListView listView;

    private static final String TAG = "RjxxFragmentTab";

    private LinkedList<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;

    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rjxxtab_aqyh, container, false);

        if (view != null) {
            listView = (ListView) view.findViewById(R.id.baseInfoList);
        }
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
}
