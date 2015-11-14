package com.daking.app.client_common.view.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 联动ScrollView
 * Created by daking on 15/10/27.
 */
public class RelationScrollView extends ScrollView{
    private ScrollViewListener mScrollViewListener;
    private ScrollView mRelationScrollView; // 绑定的ScrollView,我滚动的时候联合TA跟着滚动

    public RelationScrollView(Context context) {
        super(context);
    }

    public RelationScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelationScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置滚动位置改变回调
     * */
    public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
    }

    /**
     * 设置受我滚动影响的联合ScrollView
     * */
    public void setRelationScrollView(ScrollView scrollView){
        this.mRelationScrollView = scrollView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 告知回调
        if(mScrollViewListener != null){
            mScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
        // 联合绑定的ScrollView
        if(mRelationScrollView != null){
            mRelationScrollView.scrollTo(l, t);
        }
    }


    /**
     * 滚动回掉接口
     * */
    public interface ScrollViewListener {
        void onScrollChanged(RelationScrollView scrollView, int x, int y,
                             int oldx, int oldy);
    }
}
