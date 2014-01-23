package com.ghtn.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.ghtn.util.ConstantUtil;

/**
 * 向右滑动返回前一个页面
 * Created by Administrator on 14-1-23.
 */
public class SwipeBackActivity extends ActionBarActivity implements View.OnTouchListener {

    //记录手指按下时的横坐标。
    private float xDown;


    //记录手指移动时的横坐标。
    private float xMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                //活动的距离
                int distanceX = (int) (xMove - xDown);
                //获取顺时速度
                int xSpeed = getScrollVelocity();
                //当触摸点靠近屏幕左边, 滑动的距离大于设定的最小距离且滑动的瞬间速度大于设定的速度时，返回到上一个activity
                if (xDown < ConstantUtil.MARGIN_LEFT_MAX && distanceX > ConstantUtil.XDISTANCE_MIN && xSpeed > ConstantUtil.XSPEED_MIN) {
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 修改点击返回键的动画
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            this.finish();  //finish当前activity
            overridePendingTransition(R.anim.in_from_left,
                    R.anim.out_to_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
