package com.example.lenovo.a20170909;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.andy.library.ChannelActivity;
import com.andy.library.ChannelBean;
import com.example.kson.tablayout.widget.HorizontalScollTabhost;
import com.example.kson.tablayout.widget.bean.CategoryBean;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<CategoryBean> list;
    private List<Fragment> fragmentlist;
    private CategoryBean bean;
    private HorizontalScollTabhost tabhost;
    private String josonStr;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private List<ChannelBean> cblist;
    private ImageView iv;
    private List<Fragment> otherfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabhost = (HorizontalScollTabhost) findViewById(R.id.tabhost);
        iv = (ImageView) findViewById(R.id.iv);
        sp = getSharedPreferences("msg", MODE_PRIVATE);
        edit = sp.edit();
        list = new ArrayList<CategoryBean>();
        fragmentlist = new ArrayList<Fragment>();
        bean = new CategoryBean();
        list.add(new CategoryBean(null,"头条1"));
        list.add(new CategoryBean(null,"头条2"));
        list.add(new CategoryBean(null,"头条3"));
        list.add(new CategoryBean(null,"头条4"));
        list.add(new CategoryBean(null,"头条5"));
        list.add(new CategoryBean(null,"头条6"));
        list.add(new CategoryBean(null,"头条7"));
        list.add(new CategoryBean(null,"头条8"));

        fragmentlist.add(new Fragment1());
        fragmentlist.add(new Fragment1());
        fragmentlist.add(new Fragment1());
        fragmentlist.add(new Fragment1());
        fragmentlist.add(new Fragment1());
        fragmentlist.add(new Fragment1());
        fragmentlist.add(new Fragment1());
        fragmentlist.add(new Fragment1());
        tabhost.diaplay(list,fragmentlist);


         iv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 josonStr = sp.getString("user_setting", null);
                 cblist = new ArrayList<ChannelBean>();
                 if(josonStr==null)
                 {   //如果josonStr为空的话，向频道中添加频道
                     cblist.add(new ChannelBean("头条1",true));
                     cblist.add(new ChannelBean("头条2",true));
                     cblist.add(new ChannelBean("头条3",true));
                     cblist.add(new ChannelBean("头条4",true));
                     cblist.add(new ChannelBean("头条5",true));
                     cblist.add(new ChannelBean("头条6",true));
                     cblist.add(new ChannelBean("头条7",true));
                     cblist.add(new ChannelBean("头条8",true));
                 }
                 else
                 {   //解析得到数据
                     try {
                         JSONArray array=new JSONArray(josonStr);
                         for (int i = 0; i <array.length(); i++) {
                             JSONObject jb= (JSONObject) array.get(i);
                             String name = jb.getString("name");
                             boolean isSelect = jb.getBoolean("isSelect");
                             ChannelBean cb=new ChannelBean(name,isSelect);
                             //添加到ChannelBean集合中
                             cblist.add(cb);
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 //添加到我的频道中
                 ChannelActivity.startChannelActivity(MainActivity.this,cblist);
             }
         });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==101)
        {
            Toast.makeText(this, "11111111111", Toast.LENGTH_SHORT).show();
            josonStr = data.getStringExtra(ChannelActivity.RESULT_JSON_KEY);
             edit.putString("user_setting",josonStr).commit();
            //将list清空，如不清空则数据会追加
             list.clear();
            try {
                JSONArray array=new JSONArray(josonStr);
                //创建一个空的Fragment集合
                otherfragment = new ArrayList<Fragment>();
                //重新实例CategoryBean对象
                bean=new CategoryBean();
                for (int i = 0; i <array.length(); i++) {
                    JSONObject jb= (JSONObject) array.get(i);
                    String name = jb.getString("name");
                    boolean isSelect = jb.getBoolean("isSelect");
                    ChannelBean cb=new ChannelBean(name,isSelect);
                    cblist.add(cb);

                    if(isSelect==true)
                    {   //重新添加CategoryBean对象移动到空fragment集合中
                        list.add(new CategoryBean(null,name));
                        otherfragment.add(fragmentlist.get(i));
                    }
                    tabhost.clear();

                    tabhost.diaplay(list,fragmentlist);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
