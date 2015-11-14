package com.daking.app.sample.view.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;
import com.daking.app.sample.mgr.sql.DBOpenHelper;

/**
 * SQLite
 */
public class SQLiteActivity extends BaseActivity implements View.OnClickListener {
    private static final String TABLE_NAME = "account_table";

    private Button btnInsert;
    private Button btnDelete;
    private Button btnQuery;
    private Button btnUpdate;

    private SQLiteDatabase db;
    private int index = 1;

    @Override
    public int bindLayout() {
        return R.layout.activity_sqlite;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_sqlite_name);
    }

    @Override
    public void initView(View view) {
        btnInsert = (Button) view.findViewById(R.id.btn_sqlite_btnInsert);
        btnDelete = (Button) view.findViewById(R.id.btn_sqlite_btnDelete);
        btnQuery = (Button) view.findViewById(R.id.btn_sqlite_btnQuery);
        btnUpdate = (Button) view.findViewById(R.id.btn_sqlite_btnUpdate);

        btnInsert.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void initData(Context mContext) {
        db = new DBOpenHelper(mContext).getWritableDatabase();

        onQuery(); // 为了获取最后一个id
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
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_sqlite_btnInsert:
                index++;
                String name = "呵呵"+index;
                ContentValues valuesInsert = new ContentValues();
                valuesInsert.put("name", name);
                db.insert(TABLE_NAME, null, valuesInsert);
                showToast("插入记录:\""+name+"\"成功!");
                break;
            case R.id.btn_sqlite_btnDelete:
                db.delete(TABLE_NAME, "id = ?",
                        new String[]{String.valueOf(index)});
                break;
            case R.id.btn_sqlite_btnQuery:
                showToast(onQuery());
                break;
            case R.id.btn_sqlite_btnUpdate:
                ContentValues valuesUpdate = new ContentValues();
                valuesUpdate.put("name", "嘻嘻~");
                db.update(TABLE_NAME, valuesUpdate, "id = ?",
                        new String[]{String.valueOf(index)});
                break;
        }
    }

    private String onQuery(){
        StringBuilder sb = new StringBuilder();

        index = 1;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "id", null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                sb.append("id:"+id+",name:"+name+"<br>");
                index = id;
            }while (cursor.moveToNext());
        }
        cursor.close();
        return sb.toString();
    }
}
