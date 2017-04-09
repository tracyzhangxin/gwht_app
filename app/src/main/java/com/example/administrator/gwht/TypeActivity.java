package com.example.administrator.gwht;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.db.Selector;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.NewsModel;
import Msg.LayoutMessage;
import Msg.MyAdapter;
import Tools.NewsOpenHelper;
import com.handmark.pulltorefresh.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

@ContentView(R.layout.activity_type)
public class TypeActivity extends BaseActivity {
    private ListView lv;
    private String[] title;
    private String[] content;
    private List<LayoutMessage> contentlist;
    private BaseAdapter adapter;
    private NewsOpenHelper myHelper;
    private PullToRefreshListView mPullRefreshListView;

    private ImageView iv_more;
    List<Map<String, String>> moreList;

    @ViewInject(R.id.lv_type)
    private ListView listView;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.left_arrow);
        toolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initData();
        initviews();

    }

    private List<LayoutMessage> getMyData(){
        List<LayoutMessage> msgList = new ArrayList<LayoutMessage>();
        LayoutMessage web,msg,system,phone,data;
        title = getResources().getStringArray(R.array.lv_title);
        content = getResources().getStringArray(R.array.lv_content);

        msg = new LayoutMessage();
        msg.setTag(1);
        msg.setType(MyAdapter.LV_TYPE_PIC);
        msg.setImage(R.mipmap.news);
        msg.setTitle("安全资讯");
        msg.setContent("提供最新的安全资讯");
        msgList.add(msg);

        web = new LayoutMessage();
        web.setTag(1);
        web.setType(MyAdapter.LV_TYPE_PIC);
        web.setImage(R.mipmap.chrome);
        web.setTitle("web安全");
        web.setContent("提供最新的web安全漏洞信息");
        msgList.add(web);

        system = new LayoutMessage();
        system.setTag(1);
        system.setImage(R.mipmap.windows);
        system.setType(MyAdapter.LV_TYPE_PIC);
        system.setTitle("系统安全");
        system.setContent("关于系统方面的最新安全信息");
        msgList.add(system);

        phone = new LayoutMessage();
        phone.setTag(0);
        phone.setImage(R.mipmap.app);
        phone.setType(MyAdapter.LV_TYPE_PIC);
        phone.setTitle("移动安全");
        phone.setContent("提供android或者ios的安全信息");
        msgList.add(phone);

        data = new LayoutMessage();
        data.setTag(1);
        data.setImage(R.mipmap.data);
        data.setType(MyAdapter.LV_TYPE_PIC);
        data.setTitle("数据安全");
        data.setContent("有关数据方面的安全信息");
        msgList.add(data);

        return msgList;

    }


    private void initData() {

        contentlist = new ArrayList<LayoutMessage>(getMyData());
        adapter = getAdapter();

    }

    private BaseAdapter getAdapter(){
        return new MyAdapter(this,contentlist);
    }

    private void initviews() {
        // TODO Auto-generated method stub

        listView.setAdapter(adapter);

    }

}
