package com.daking.app.client_common.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * 联动ListView
 * Created by daking on 15/10/27.
 */
public class RelationListView extends ListView {
    private RelationListView mListView; // 绑定的联动listview

    public RelationListView(Context context) {
        super(context);
    }

    public RelationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 绑定联合listview
     */
    public void setRelationListView(RelationListView listView) {
        mListView = listView;
    }

    /**
     * 重写测量方法是为了使listview能wrap_content成最宽的item的宽度.
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = this.getMeasuredHeight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View item = getChildAt(i);
                measureChild(item, widthMeasureSpec, heightMeasureSpec);
                width = Math.max(width, item.getMeasuredWidth());
            }
        }

        setMeasuredDimension(width, height);
    }

    /**
     * 提供给外部使用,主动触发模拟的触摸事件
     */
    public void onTouch(MotionEvent ev) {
        super.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != mListView) {
            mListView.onTouch(ev);
        }
        return super.onTouchEvent(ev);
    }
}
