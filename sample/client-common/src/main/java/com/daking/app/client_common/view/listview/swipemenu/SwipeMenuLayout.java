package com.daking.app.client_common.view.listview.swipemenu;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.daking.app.client_common.util.view.DisplayUtil;

/**
 * 侧滑菜单Layout
 * 1.包含 侧滑菜单界面 和 原始item界面
 * 2.处理侧滑操作
 */
public class SwipeMenuLayout extends FrameLayout {

    private static final int CONTENT_VIEW_ID = 1;
    private static final int MENU_VIEW_ID = 2;

    private static final int STATE_CLOSE = 0;
    private static final int STATE_OPEN = 1;

    private View mContentView;
    private SwipeMenuView mMenuView;
    private int mDownX;
    private int state = STATE_CLOSE;
    private GestureDetectorCompat mGestureDetector;
    private OnGestureListener mGestureListener;
    private boolean isFling;                    // 是否为快速滑动
    private int MIN_FLING = 15;                 // 快速滑动生效的最小距离
    private int MAX_VELOCITYX = 500;            // 快速滑动的最大距离
    private ScrollerCompat mOpenScroller;
    private ScrollerCompat mCloseScroller;
    private int mBaseX;
    private int position;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    public SwipeMenuLayout(View contentView, SwipeMenuView menuView) {
        this(contentView, menuView, null, null);
    }

    public SwipeMenuLayout(View contentView, SwipeMenuView menuView,
                           Interpolator closeInterpolator, Interpolator openInterpolator) {
        super(contentView.getContext());

        mCloseInterpolator = closeInterpolator;
        mOpenInterpolator = openInterpolator;
        mContentView = contentView;
        mMenuView = menuView;
        mMenuView.setLayout(this);

        init();
    }

    private SwipeMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private SwipeMenuLayout(Context context) {
        super(context);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        mMenuView.setPosition(position);
    }

    private void init() {
        MIN_FLING = DisplayUtil.dip2px(MIN_FLING);
        MAX_VELOCITYX = -DisplayUtil.dip2px(MAX_VELOCITYX);

                setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        // 手势判断是否为快速滑动
        mGestureListener = new SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                isFling = false;
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 手势从左向右划过一定距离,"快速滑动"生效
                if ((e1.getX() - e2.getX()) > MIN_FLING && velocityX < MAX_VELOCITYX) {
                    isFling = true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);

        if (mCloseInterpolator != null) {
            mCloseScroller = ScrollerCompat.create(getContext(), mCloseInterpolator);
        } else {
            mCloseScroller = ScrollerCompat.create(getContext());
        }
        if (mOpenInterpolator != null) {
            mOpenScroller = ScrollerCompat.create(getContext(), mOpenInterpolator);
        } else {
            mOpenScroller = ScrollerCompat.create(getContext());
        }

        // 依次添加 原始item界面 侧滑菜单界面
        LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(contentParams);
        if (mContentView.getId() < 1) {
            mContentView.setId(CONTENT_VIEW_ID);
        }

        mMenuView.setId(MENU_VIEW_ID);
        mMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        addView(mContentView);
        addView(mMenuView);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 外部处理完Touch事件,再调用到这里
     * */
    public boolean onSwipe(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mDownX = (int) event.getX();
            isFling = false;
            break;
        case MotionEvent.ACTION_MOVE:
            int dis = (int) (mDownX - event.getX());
            if (state == STATE_OPEN) {
                dis += mMenuView.getWidth();
            }
            swipe(dis);
            break;
        case MotionEvent.ACTION_UP:
            // "快速滑动"或从左往右划过半个侧滑菜单的距离,则打开菜单
            if (isFling || (mDownX - event.getX()) > (mMenuView.getWidth() / 2)) {
                // open
                smoothOpenMenu();
            } else {
                // close
                smoothCloseMenu();
                return false;
            }
            break;
        }
        return true;
    }

    public boolean isOpen() {
        return state == STATE_OPEN;
    }

    public boolean isActive() {
        if (mContentView != null) {
            return mContentView.getLeft() != 0;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 移动contentView和menuView的布局位置
     * @param dis contentView超出左边界的距离
     */
    private void swipe(int dis) {
        if (dis > mMenuView.getWidth()) {
            dis = mMenuView.getWidth();
        }
        if (dis < 0) {
            dis = 0;
        }
        mContentView.layout(-dis, mContentView.getTop(), mContentView.getWidth() - dis, getMeasuredHeight());
        mMenuView.layout(mContentView.getWidth() - dis, mMenuView.getTop(),
                mContentView.getWidth() + mMenuView.getWidth() - dis, mMenuView.getBottom());
    }

    @Override
    public void computeScroll() {
        if (state == STATE_OPEN) {
            if (mOpenScroller.computeScrollOffset()) {
                swipe(mOpenScroller.getCurrX());
                postInvalidate();
            }
        } else {
            if (mCloseScroller.computeScrollOffset()) {
                swipe(mBaseX - mCloseScroller.getCurrX());
                postInvalidate();
            }
        }
    }

    /**平滑关闭菜单*/
    public void smoothCloseMenu() {
        state = STATE_CLOSE;
        mBaseX = -mContentView.getLeft();
        mCloseScroller.startScroll(0, 0, mBaseX, 0, 350);
        postInvalidate();
    }

    /**平滑打开菜单*/
    public void smoothOpenMenu() {
        state = STATE_OPEN;
        mOpenScroller.startScroll(-mContentView.getLeft(), 0, mMenuView.getWidth(), 0, 350);
        postInvalidate();
    }

    /**关闭菜单*/
    public void closeMenu() {
        if (mCloseScroller.computeScrollOffset()) {
            mCloseScroller.abortAnimation();
        }
        if (state == STATE_OPEN) {
            state = STATE_CLOSE;
            swipe(0);
        }
    }

    /**打开菜单*/
    public void openMenu() {
        if (state == STATE_CLOSE) {
            state = STATE_OPEN;
            swipe(mMenuView.getWidth());
        }
    }

    public View getContentView() {
        return mContentView;
    }

    public SwipeMenuView getMenuView() {
        return mMenuView;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMenuView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContentView.layout(0, 0, getMeasuredWidth(), mContentView.getMeasuredHeight());
        mMenuView.layout(getMeasuredWidth(), 0, getMeasuredWidth() + mMenuView.getMeasuredWidth(),
                mContentView.getMeasuredHeight());
    }

    public void setMenuHeight(int measuredHeight) {
        Log.i("byz", "pos = " + position + ", height = " + measuredHeight);
        LayoutParams params = (LayoutParams) mMenuView.getLayoutParams();
        if (params.height != measuredHeight) {
            params.height = measuredHeight;
            mMenuView.setLayoutParams(mMenuView.getLayoutParams());
        }
    }
}
