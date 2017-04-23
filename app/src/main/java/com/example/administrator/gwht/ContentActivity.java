package com.example.administrator.gwht;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;

import Model.NewsModel;
import Tools.NewsJsonHelper;
import Tools.NewsOpenHelper;
import Tools.PreferenceUtil;

@ContentView(R.layout.activity_content)
public class ContentActivity extends BaseActivity implements  BottomNavigationBar.OnTabSelectedListener{
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private ArrayList<Fragment> fragments;
    BadgeItem numberBadgeItem = new BadgeItem()
            .setBorderWidth(4)
            .setBackgroundColor(Color.RED)
            .setHideOnSelect(false);
    public static final String SP_INFOS = "SPDATA_Files";
    public static final String USERID = "UserID";
    SharedPreferences sp;
    String uid ; // 取Preferences中的帐号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        uid = sp.getString(USERID, null); // 取Preferences中的帐号
       /* toolbar.setNavigationIcon(R.drawable.left_arrow);
         toolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(ContentActivity.this, NewsActivity.class);
                 ContentActivity.this.startActivity(intent);
                 ContentActivity.this.finish();
             }
         });*/

       // Toast.makeText(this,"content",Toast.LENGTH_LONG).show();

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_msg, "消息").setActiveColorResource(R.color.teal).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.ic_user, "用户").setActiveColorResource(R.color.grey))
                .addItem(new BottomNavigationItem(R.mipmap.ic_favorite_white_24dp, "收藏").setActiveColorResource(R.color.red))
                .setFirstSelectedPosition(0)
                .initialise();

        fragments = getFragments();
        setDefaultFragment();
        numberBadgeItem.hide();
        bottomNavigationBar.setTabSelectedListener(this);

    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, fragments.get(0));
        transaction.commit();

    }
    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(MsgFragment.newInstance("Msg"));
        fragments.add(UserFragment.newInstance("User"));
        fragments.add(LoveFragment.newInstance("Love"));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
       // updateNumberItem();
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.layFrame, fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }

    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String content = intent
                .getStringExtra("pushStr");
       /* if (!content.isEmpty()){
            Toast.makeText(this,content,Toast.LENGTH_LONG).show();
        }*/
        NewsModel newsModel =NewsJsonHelper.pushJsonDecode(content);
          if (newsModel.code==0){
            NewsOpenHelper myHelper = new NewsOpenHelper(this, NewsOpenHelper.DB_NAME, null, 1);// 打开数据表库表
            boolean flag=myHelper.insertNews(newsModel.data,this);
              //Toast.makeText(this,newsModel.data.isRead,Toast.LENGTH_LONG).show();
           /* if (flag)
                Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();*/
              this.recreate();
        }
       // updateNumberItem();

    }
    //接收到新的信息时numberBadgeItem+1
    public void updateNumberItem(){
       NewsOpenHelper myHelper = new NewsOpenHelper(this, NewsOpenHelper.DB_NAME, null, 1);// 打开数据表库表，
        int num=myHelper.getNoReadNum(uid);
        if (num==0)
            numberBadgeItem.hide();
        else{
            numberBadgeItem.setText(String.valueOf(num)).show();
        }

    }




}
