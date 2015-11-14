package com.daking.app.sample.view.viewpager.tabmenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 底部导航栏 适配器
 * Created by daking on 15/11/2.
 */
public class TabMenuAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    private TabMenuFragment1 myFragment1 = null;
    private TabMenuFragment2 myFragment2 = null;
    private TabMenuFragment3 myFragment3 = null;
    private TabMenuFragment4 myFragment4 = null;

    public TabMenuAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new TabMenuFragment1();
        myFragment2 = new TabMenuFragment2();
        myFragment3 = new TabMenuFragment3();
        myFragment4 = new TabMenuFragment4();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case TabMenuActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case TabMenuActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case TabMenuActivity.PAGE_THREE:
                fragment = myFragment3;
                break;
            case TabMenuActivity.PAGE_FOUR:
                fragment = myFragment4;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
