package com.zhouwei.anything.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhouwei.anything.R;
import com.zhouwei.anything.bean.AppLog;
import com.zhouwei.anything.bean.Person;
import com.zhouwei.anything.xxorm.DBEngine;

import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private android.widget.Button add;
    private android.widget.Button delete;
    private android.widget.Button update;
    private android.widget.Button get;
    private Button getPart;
    private Button saveLog;
    private Button showLog;
    private Button deleteLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.deleteLog = (Button) findViewById(R.id.deleteLog);
        this.showLog = (Button) findViewById(R.id.showLog);
        this.saveLog = (Button) findViewById(R.id.saveLog);
        this.getPart = (Button) findViewById(R.id.getPart);
        this.get = (Button) findViewById(R.id.get);
        this.update = (Button) findViewById(R.id.update);
        this.delete = (Button) findViewById(R.id.delete);
        this.add = (Button) findViewById(R.id.add);


        get.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        getPart.setOnClickListener(this);
        saveLog.setOnClickListener(this);
        showLog.setOnClickListener(this);
        deleteLog.setOnClickListener(this);

        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.add) {
            add();
        } else if (id == R.id.get) {
            get();
        } else if (id == R.id.delete) {
            delete();
        } else if (id == R.id.update) {
            update();
        } else if (id == R.id.getPart) {
            getPart();
        } else if (id == R.id.saveLog) {
            saveLog();
        } else if (id == R.id.showLog) {
            showLog();
        } else if (id == R.id.deleteLog) {
            deleteLog();
        }
    }

    private void deleteLog() {
        DBEngine.getInstance().delete(AppLog.class, null, null);
    }

    private void showLog() {
        Log.i("AAAA", "==showLog==");
        List<AppLog> ps = DBEngine.getInstance().get(AppLog.class, null, null, null, null, null);
        if (null != ps) {
            for (int i = 0; i < ps.size(); i++) {
                Log.i("AAAA", "person: " + ps.get(i));
            }
        }
    }

    private void saveLog() {
        AppLog log = new AppLog();
        log.setMsg("test saveLog");
        log.setTag("MainActivity");
        log.setThread(Thread.currentThread().getName());
        log.setTime(System.currentTimeMillis() + "");
        DBEngine.getInstance().save(log);
    }


    private void add() {
        Log.i("AAAA", "==add==");
        Person person = new Person();
        person.setAge(11);
        person.setName("xiaohon");
        DBEngine.getInstance().save(person);
        // DBEngine.getInstance().save(Person.class, person.save());
    }

    private void get() {
        Log.i("AAAA", "==get==");
        List<Person> ps = DBEngine.getInstance().get(Person.class, null, null, null, null, null);
        if (null != ps) {
            for (int i = 0; i < ps.size(); i++) {
                Log.i("AAAA", "person: " + ps.get(i));
            }
        }
    }

    private void getPart() {
        List<Person> ps = DBEngine.getInstance().get(Person.class, 15, 3);
        if (null != ps) {
            for (int i = 0; i < ps.size(); i++) {
                Log.i("AAAA", "person: " + ps.get(i));
            }
        }
    }

    private void delete() {
        Log.i("AAAA", "==delete==");
        DBEngine.getInstance().delete(Person.class, null, null);
    }

    private void update() {
        Log.i("AAAA", "==update==");
        ContentValues values = new ContentValues();
        values.put("name", "XXORM");
        String whereClause = "age = ?";
        String[] whereArgs = new String[]{11 + ""};
        DBEngine.getInstance().update(Person.class, values, whereClause, whereArgs);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
}
