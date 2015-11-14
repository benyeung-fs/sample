package com.daking.app.sample.mgr.main;

/**
 * 主界面VO
 * Created by daking on 15/10/26.
 */
public class MainVO {
    public static int HANDLE_ACTIVITY = 1; // 跳转到Activity

    private String title;       // 标题
    private String content;     // 内容
    private int type;           // 操作类型

    public MainVO(String title, String content, int type){
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
