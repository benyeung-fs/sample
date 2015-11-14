package com.daking.app.client_common.view.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 通用适配器
 * Created by daking on 15/10/15.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mData;
    protected int mLayoutRes;

    public CommonAdapter(Context context, List<T> data, int layoutRes) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = data;
        this.mLayoutRes = layoutRes;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, mLayoutRes, i);
        bind(viewHolder, getItem(i));
        return viewHolder.getConvertView();
    }

    /**
     * 利用数据填充视图(子类必须重写)
     */
    public abstract void bind(ViewHolder holder, T data);



    //==================数据操作(起)===================
    /**
     * 添加新数据到末尾
     *
     * @param data
     */
    public void add(T data) {
        if (mData == null) {
            mData = new LinkedList<T>();
        }
        mData.add(data);
        this.notifyDataSetChanged();
    }

    /**
     * 添加新数据到指定索引
     *
     * @param data
     * @param index
     */
    public void add(T data, int index) {
        if (mData == null) {
            mData = new LinkedList<T>();
        }
        mData.add(index, data);
        this.notifyDataSetChanged();
    }

    /**
     * 删除指定数据
     *
     * @param data
     */
    public void remove(T data) {
        if (mData != null) {
            mData.remove(data);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除指定索引的数据
     *
     * @param index
     */
    public void remove(int index) {
        if (mData != null) {
            mData.remove(index);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 清空
     */
    public void clear() {
        if (mData != null) {
            mData.clear();
            this.notifyDataSetChanged();
        }
    }

    /**
     * 全部替换
     *
     * @param data 新数据列表
     */
    public void replaceAllData(List<T> data) {
        mData = data;
        this.notifyDataSetChanged();
    }
    //==================数据操作(止)===================



    public static class ViewHolder {
        private int mPosition;              // 位于listview中的索引
        private View mConvertView;          // 布局界面
        private SparseArray<View> mViews;   // 存储各子组件

        private ViewHolder(Context context, ViewGroup parent, int layoutRes) {
            mViews = new SparseArray<View>();
            this.mConvertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
            this.mConvertView.setTag(this);
        }

        /**
         * 获取ViewHolder(会复用)
         * @param context
         * @param convertView 与此ViewHolder绑定的布局视图
         * @param parent
         * @param layoutRes 布局资源id
         * @param position 行索引
         * @return
         */
        public static ViewHolder get(Context context, View convertView, ViewGroup parent,
                                     int layoutRes, int position) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(context, parent, layoutRes);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mPosition = position;
            return holder;
        }

        /**
         * 根据命名id获取子视图
         */
        public <T extends View> T getView(int id) {
            T t = (T) mViews.get(id);
            if (t == null) {
                t = (T) mConvertView.findViewById(id);
                mViews.put(id, t);
            }
            return t;
        }

        /**
         * 获取布局文件的根视图
         */
        public View getConvertView() {
            return mConvertView;
        }

        /**
         * 获取此视图对应的行索引
         */
        public int getPosition() {
            return mPosition;
        }

        /**
         * 设置对应id的ImageView的图片路径
         */
        public void displayImage(final int id, String url) {
            ImageView iv = (ImageView) getView(id);
            if (iv != null && iv.getParent() != null) {
                String old_url = (String) iv.getTag();
                // 若两次请求内容相同则忽略
                if (old_url != null && old_url.equals(url)) {
                    return;
                }
                // 记录此次请求内容
                iv.setTag(url);
                ImageLoader.getInstance().displayImage(url, iv, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        ImageView load_iv = (ImageView) view;
                        if (load_iv != null && load_iv.getParent() != null) {
                            String record = (String) load_iv.getTag();
                            // 防止因图片异步加载导致的错位
                            if (record.equals(imageUri)) {
                                load_iv.setImageBitmap(loadedImage);
                            }
                        }
                    }
                });
            }
        }

    }
}
