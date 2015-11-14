package com.daking.app.client_common.view.listview.swipemenu;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * 侧滑菜单
 * 1.仅用于菜单的配置,不作ui组件
 * 2.包含各菜单项的配置
 */
public class SwipeMenu {

	private Context mContext;
	private List<SwipeMenuItem> mItems;
	private int mViewType;

	public SwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<SwipeMenuItem>();
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

}
