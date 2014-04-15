package com.ghtn.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ghtn.util.DataUtil;

import java.util.List;
import java.util.Map;

public class RjxxSummaryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rjxx_summary);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rjxx_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            System.out.println("按下刷新按钮!!!!!!");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ListView listView;
        private SimpleAdapter simpleAdapter;
        private List<Map<String, Object>> dataList;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_rjxx_summary, container, false);

            if (rootView != null) {
                listView = (ListView) rootView.findViewById(R.id.rjxxSummaryList);

                // 从服务器取得数据
                dataList = DataUtil.rjxxSummaryList;

                simpleAdapter = new SimpleAdapter(getActivity(),
                        dataList,
                        R.layout.rjxx_summary_listview_item,
                        new String[]{"dept", "name", "rjTotal", "ybrj", "zbrj", "zhbfj", "zcrj",
                                "dbrj", "pcyh", "pcsw"},
                        new int[]{R.id.dept, R.id.name, R.id.rjTotal, R.id.ybrj, R.id.zbrj, R.id.zhbfj,
                                R.id.zcrj, R.id.dbrj, R.id.pcyh, R.id.pcsw}
                );

                listView.setAdapter(simpleAdapter);
            }

            return rootView;
        }
    }

}
