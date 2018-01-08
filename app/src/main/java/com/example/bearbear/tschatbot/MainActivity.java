package com.example.bearbear.tschatbot;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.jar.JarException;

public class MainActivity extends AppCompatActivity implements HttpGetDataListener, View.OnClickListener{

    private HttpData httpData;
    private List<ListData> lists;
    private ListView lv;
    private EditText sendtext;
    private Button send_btn;
    private String content_str;
    private TextAdapter adapter;
    private String[] welcome_array;
    private double curTime, oldTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        httpData = (HttpData) new HttpData("http://www.tuling123.com/openapi/api?key=b11cb3ccb823dbfa9dfdbadc24b715ad&info=你好", this).execute();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.iv);
        sendtext = (EditText)findViewById(R.id.sendtext);
        send_btn = (Button)findViewById(R.id.send_btn);
        lists = new ArrayList<ListData>();
        send_btn.setOnClickListener(this);
        adapter = new TextAdapter(lists, this);
        lv.setAdapter(adapter);
        ListData listData = new ListData(getRandomTips(), ListData.RECEIVER,getTime());
        lists.add(listData);
    }

    private String getRandomTips() {
//        String welcome = null;
        welcome_array = this.getResources().getStringArray(R.array.welcome_tips);
        Random rand = new Random();
        int index = rand.nextInt(welcome_array.length);
        return welcome_array[index];
    }


    @Override
    public void getDataUrl(String data) {
        System.out.println(data);
        parseText(data);
    }

    public void parseText(String str) {
//        JSONObject jb = new JSONObject();
        try {
            JSONObject jb = new JSONObject(str);
//            System.out.println(jb.getString("code"));
//            System.out.println(jb.getString("text"));
            ListData listData;
            listData = new ListData(jb.getString("text"), ListData.RECEIVER,getTime());
            lists.add(listData);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        getTime();
        content_str = sendtext.getText().toString();
        sendtext.setText("");
        String sendkh = content_str.replace(" ", "").replace("\n", "");
        ListData listData = new ListData(content_str,ListData.SEND,getTime());
        lists.add(listData);
        if (lists.size() > 30) {
            for (int i = 0; i < 20; i++) {
                lists.remove(0);
            }
        }
        adapter.notifyDataSetChanged();
        httpData = (HttpData) new HttpData(
                "http://www.tuling123.com/openapi/api?key=b11cb3ccb823dbfa9dfdbadc24b715ad&info="+sendkh, this).execute();
    }

    private String getTime(){
        curTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        if (curTime - oldTime >= 500) {
            oldTime = curTime;
            return str;
        }
        return "";

    }

}
