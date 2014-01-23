package com.ghtn.myapplication;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-1-9.
 */
public class AqyhActivity extends ActionBarActivity {
    private static final String TAG = "AqyhActivity";

    private ActionBar actionBar;
    private ViewPager viewPager;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqyh);

        setTitle("安全隐患");

        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ActionBar.Tab tab = actionBar.newTab().setText(R.string.aqyh_tab_yh)
                    .setTabListener(new MyTabsListener());
            actionBar.addTab(tab);

            tab = actionBar.newTab().setText(R.string.aqyh_tab_sw)
                    .setTabListener(new MyTabsListener());
            actionBar.addTab(tab);

            tab = actionBar.newTab().setText(R.string.aqyh_tab_rjxx)
                    .setTabListener(new MyTabsListener());
            actionBar.addTab(tab);
        }


        viewPager = (ViewPager) findViewById(R.id.vp);

        List<Fragment> fragmentList = new ArrayList<>();
        Fragment yhFragment = new FragmentTab("yh");
        Fragment swFragment = new FragmentTab("sw");
        Fragment rjxxFragment = new FragmentTab("rjxx");

        fragmentList.add(yhFragment);
        fragmentList.add(swFragment);
        fragmentList.add(rjxxFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        //---------------------------------------------------------------------------

        viewPager.setOnPageChangeListener(new MyPageScrollEvent());
        viewPager.setCurrentItem(0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected class MyTabsListener implements ActionBar.TabListener {
       /* private final Activity mActivity;
        private final String mTag;
        private Fragment mFragment;
        private final Class<T> mClass;*/


       /* @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        public MyTabsListener(Activity activity, String tag, Class<T> clz) {
            this.mTag = tag;
            this.mActivity = activity;
            this.mClass = clz;


            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();

                // 从activity中分离fragment
                ft.detach(mFragment);
                ft.commit();
            }

        }*/

        @Override
        public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        @Override
        public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
            // tab选中时, 加载对应的fragment
            /*if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(R.id.aqyh_tabs, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }*/

            if (viewPager != null)
                viewPager.setCurrentItem(tab.getPosition());
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        @Override
        public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
           /* if (mFragment != null) {
                ft.detach(mFragment);
            }*/
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentsList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }
    }

    private class MyPageScrollEvent implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            //当滑动ViewPager 换页时，将ActionBar上的Tab显示到对应页
            actionBar.selectTab(actionBar.getTabAt(i));
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
