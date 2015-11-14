package com.daking.app.sample.view.viewpager.tabmenu;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;

/**
 * 底部导航栏
 */
public class TabMenuActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{
    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    private TabMenuAdapter adapter;
    private ViewPager vpager;

    private TextView tv_title;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_better;
    private RadioButton rb_setting;

    @Override
    public int bindLayout() {
        return R.layout.activity_tabmenu;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_tabmenu_name);
    }

    @Override
    public void initView(View view) {
        this.showTitle(false);

        adapter = new TabMenuAdapter(getSupportFragmentManager());

        tv_title = (TextView) view.findViewById(R.id.tv_tabmenu_title);
        rg_tab_bar = (RadioGroup) view.findViewById(R.id.rg_tabmenu_bottombar);
        rb_channel = (RadioButton) view.findViewById(R.id.rb_tabmenu_channel);
        rb_message = (RadioButton) view.findViewById(R.id.rb_tabmenu_message);
        rb_better = (RadioButton) view.findViewById(R.id.rb_tabmenu_better);
        rb_setting = (RadioButton) view.findViewById(R.id.rb_tabmenu_setting);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vp_tabmenu_main);
        vpager.setAdapter(adapter);

        vpager.addOnPageChangeListener(this);

        // 默认选择第一页
        vpager.setCurrentItem(0);
        rb_channel.setChecked(true);
        tv_title.setText(rb_channel.getText());
    }

    @Override
    public void initData(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_tabmenu_channel:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_tabmenu_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_tabmenu_better:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_tabmenu_setting:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_channel.setChecked(true);
                    tv_title.setText(rb_channel.getText());
                    break;
                case PAGE_TWO:
                    rb_message.setChecked(true);
                    tv_title.setText(rb_message.getText());
                    break;
                case PAGE_THREE:
                    rb_better.setChecked(true);
                    tv_title.setText(rb_better.getText());
                    break;
                case PAGE_FOUR:
                    rb_setting.setChecked(true);
                    tv_title.setText(rb_setting.getText());
                    break;
            }
        }
    }
}
