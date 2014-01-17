package com.ghtn.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment_a containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            GridView gridview = (GridView) rootView.findViewById(R.id.GridView);
            ArrayList<HashMap<String, Object>> menuList = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("ItemImage", R.drawable.mail);
                switch (i) {
                    case 1:
                        map.put("ItemText", "安全生产");
                        break;
                    case 2:
                        map.put("ItemText", "生产日报");
                        break;
                    case 3:
                        map.put("ItemText", "重点工程");
                        break;
                    case 4:
                        map.put("ItemText", "人员定位");
                        break;
                    case 5:
                        map.put("ItemText", "视频监控");
                        break;
                    case 6:
                        map.put("ItemText", "安全隐患");
                        break;
                    case 7:
                        map.put("ItemText", "系统设置");
                        break;
                    case 8:
                        map.put("ItemText", "帮助说明");
                        break;
                    case 9:
                        map.put("ItemText", "关于");
                        break;
                }

                menuList.add(map);
            }

            SimpleAdapter saItem = new SimpleAdapter(getActivity(),
                    menuList, //数据源
                    R.layout.item, //xml实现
                    new String[]{"ItemImage", "ItemText"}, //对应map的Key
                    new int[]{R.id.ItemImage, R.id.ItemText});  //对应R的Id

            //添加Item到网格中
            gridview.setAdapter(saItem);
            //添加点击事件
            gridview.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int index = i + 1;//id是从0开始的，所以需要+1
                            if (index == 6) {
                                // 跳转到安全隐患页面
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), AqyhActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "你按下了选项：" + index, 0).show();
                            }
                        }
                    }
            );
            return rootView;
        }
    }

}
