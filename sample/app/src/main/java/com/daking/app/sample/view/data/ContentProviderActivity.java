package com.daking.app.sample.view.data;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daking.app.client_common.mgr.base.ActivityMgr;
import com.daking.app.client_common.util.time.TimeUtil;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;
import com.daking.app.sample.view.debug.LogActivity;
import com.daking.app.sample.view.photo.ImageStoreActivity;


/**
 * 常用ContentProvider
 */
public class ContentProviderActivity extends BaseActivity implements View.OnClickListener {
    private Button btnImageStore;
    private Button btnAllSms;
    private Button btnAllPhone;

    // 系统短信Uri
    private final Uri SMS_URI = Uri.parse("content://sms/");
    // 短信数据的各列名
    private final String[] SMS_COLS = {
            "_id",          // 短信序号
            "address",      // 对方地址,如手机号
            "person",       // 对方在通讯录中的姓名,若没有则为null
            "date",         // 日期,long类型
            "protocol",     // 协议:0为SMS_RPOTO短信，1为MMS_PROTO彩信
            "read",         // 是否阅读:0未读,1已读
            "status",       // 状态:-1接收,0完成,64等待中,128失败
            "type",         // 类型:1接收,2发出
            "body"          // 短信内容
    };

    // 通讯录Uri
    private final Uri CONTACT_URI = Uri.parse("content://com.android.contacts/contacts");

    private SmsObserver smsObserver;
    public Handler smsHandler = new Handler(){
        // 短信监听回调操作
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "短信监听回调:"+msg.what);
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_contentprovider;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_contentProvider_name);
    }

    @Override
    public void initView(View view) {
        btnImageStore = (Button) view.findViewById(R.id.btn_cp_imageStore);
        btnAllSms = (Button) view.findViewById(R.id.btn_cp_allSms);
        btnAllPhone = (Button) view.findViewById(R.id.btn_cp_allPhone);

        btnImageStore.setOnClickListener(this);
        btnAllSms.setOnClickListener(this);
        btnAllPhone.setOnClickListener(this);
    }

    @Override
    public void initData(Context mContext) {
        // 注册短信监听
        smsObserver = new SmsObserver(smsHandler);
        getContentResolver().registerContentObserver(SMS_URI, true, smsObserver);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        // 注销短信监听
        if (null != smsObserver){
            getContentResolver().unregisterContentObserver(smsObserver);
            smsObserver = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_cp_imageStore:
                ActivityMgr.getInstance().startActivity(ContentProviderActivity.this,
                        new Intent(ContentProviderActivity.this, ImageStoreActivity.class),
                        ActivityMgr.ANIM_METHOD_PUSH);
                break;
            case R.id.btn_cp_allSms:
                Intent intentSms = new Intent(ContentProviderActivity.this, LogActivity.class);
                intentSms.putExtra(LogActivity.LOG, getAllSms());
                intentSms.putExtra(LogActivity.TITLE, getResources().getText(R.string.act_contentProvider_allSms).toString());
                ActivityMgr.getInstance().startActivity(intentSms, ActivityMgr.ANIM_METHOD_PUSH);
                break;
            case R.id.btn_cp_allPhone:
                Intent intentContact = new Intent(ContentProviderActivity.this, LogActivity.class);
                intentContact.putExtra(LogActivity.LOG, getAllContact());
                intentContact.putExtra(LogActivity.TITLE, getResources().getText(R.string.act_contentProvider_allPhone).toString());
                ActivityMgr.getInstance().startActivity(intentContact, ActivityMgr.ANIM_METHOD_PUSH);
                break;
        }
    }

    /**
     * 获取系统的所有短信
     * */
    private String getAllSms() {
        StringBuilder builder = new StringBuilder();

        Cursor cursor = getContentResolver().query(SMS_URI,
                SMS_COLS, null, null, "date desc");
        int idIndex = cursor.getColumnIndexOrThrow("_id");
        int addrIndex = cursor.getColumnIndexOrThrow("address");
        int personIndex = cursor.getColumnIndexOrThrow("person");
        int dateIndex = cursor.getColumnIndexOrThrow("date");
        int typeIndex = cursor.getColumnIndexOrThrow("type");
        int readIndex = cursor.getColumnIndexOrThrow("read");
        int bodyIndex = cursor.getColumnIndexOrThrow("body");

        while (cursor.moveToNext()) {
            int smsId = cursor.getInt(idIndex);
            String addr = cursor.getString(addrIndex);
            String person = cursor.getString(personIndex);
            long date = cursor.getLong(dateIndex);
            int type = cursor.getInt(typeIndex);
            int read = cursor.getInt(readIndex);
            String body = cursor.getString(bodyIndex);

            builder.append("序号:" + smsId + "\n");
            builder.append("号码:" + addr + "\n");
            if (person != null && person != "") {
                builder.append("姓名:" + person + "\n");
            }
            builder.append("时间:" + TimeUtil.getTime(date) + "\n");
            builder.append("类型:" + (type == 1 ? "接收" : "发送") + "\n");
            builder.append("状态:" + (read == 1 ? "已读" : "未读") + "\n");
            builder.append("消息:" + body + "\n");
            builder.append("=======================\n\n");
        }
        cursor.close();
        return builder.toString();
    }

    /**
     * 获取所有联系人
     * */
    private String getAllContact(){
        StringBuilder builder = new StringBuilder();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTACT_URI,
                null, null, null, null);
        while (cursor.moveToNext()){
            // 联系人
            String cId = cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.Contacts._ID));
            String cName = cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.Contacts.DISPLAY_NAME));
            builder.append("ID:"+cId+"\n");
            builder.append("姓名:"+cName+"\n");
            // 号码
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + cId, null, null);
            int pCount = 1;
            while(phones.moveToNext()){
                String phone = phones.getString(phones.getColumnIndexOrThrow("data1"));
                builder.append("号码"+pCount+":"+phone+"\n");
                pCount++;
            }
            phones.close();
            // email
            Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + cId, null, null);
            int eCount = 1;
            while(emails.moveToNext()){
                String email = emails.getString(emails.getColumnIndexOrThrow("data1"));
                builder.append("邮箱"+eCount+":"+email+"\n");
                eCount++;
            }
            emails.close();

            builder.append("========================\n\n");
        }
        cursor.close();
        return builder.toString();
    }



    /**
     * 短信监听器
     */
    class SmsObserver extends ContentObserver{

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            showToast("短信数据改变!");
            Log.i(TAG, "短信数据改变!");
        }
    }
}
