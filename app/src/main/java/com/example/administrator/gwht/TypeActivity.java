package com.example.administrator.gwht;

import android.content.SharedPreferences;
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
import Msg.TypeAdapter;
import Tools.NewsOpenHelper;
import Tools.TypeOpenHelper;

import com.handmark.pulltorefresh.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


@ContentView(R.layout.activity_type)
public class TypeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lv;
    private String[] title;
    private String[] content;
    private List<LayoutMessage> contentlist;
    private BaseAdapter adapter;
    private NewsOpenHelper myHelper;
    private PullToRefreshListView mPullRefreshListView;
    private int newselect=0;
    private int webselect=0;
    private int systemselect=0;
    private int androidselect=0;
    private int dataselect=0;
    public static final String SP_INFOS = "SPDATA_Files";
    private TypeOpenHelper typeHelper;

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
        typeHelper = new TypeOpenHelper(this, TypeOpenHelper.DB_NAME, null, 1);// 打开数据表库表，
        initData();
        initviews();

    }
    private void setSpInfos() {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        webselect = sp.getString("webselect", null)!=null?0:1;
        newselect = sp.getString("newselect", null)!=null?0:1;
        systemselect = sp.getString("systemselect", null)!=null?0:1;
        dataselect= sp.getString("dataselect", null)!=null?0:1;
        androidselect= sp.getString("androidselect", null)!=null?0:1;
        SharedPreferences.Editor editor = sp.edit(); // 获得Editor
        if (webselect>0){
            editor.putInt("webselect",webselect);
        }
        if (newselect>0){
            editor.putInt("webselect",newselect);
        }
        if (systemselect>0){
            editor.putInt("webselect",systemselect);
        }
        if (dataselect>0){
            editor.putInt("webselect",dataselect);
        }
        if (androidselect>0){
            editor.putInt("webselect",androidselect);
        }

    }
    private List<LayoutMessage> getMyData(){
        List<LayoutMessage> msgList = new ArrayList<LayoutMessage>();
        LayoutMessage web,msg,system,phone,data;
        title = getResources().getStringArray(R.array.lv_title);
        content = getResources().getStringArray(R.array.lv_content);
        setSpInfos();

       /* msg = new LayoutMessage();
        msg.setTag(newselect);
        msg.setType(MyAdapter.LV_TYPE_PIC);
        msg.setImage(R.mipmap.news);
        msg.setTitle("安全资讯");
        msg.setContent("提供最新的安全资讯");
        msgList.add(msg);

        web = new LayoutMessage();
        web.setTag(webselect);
        web.setType(MyAdapter.LV_TYPE_PIC);
        web.setImage(R.mipmap.chrome);
        web.setTitle("web安全");
        web.setContent("提供最新的web安全漏洞信息");
        msgList.add(web);

        system = new LayoutMessage();
        system.setTag(systemselect);
        system.setImage(R.mipmap.windows);
        system.setType(MyAdapter.LV_TYPE_PIC);
        system.setTitle("系统安全");
        system.setContent("关于系统方面的最新安全信息");
        msgList.add(system);

        phone = new LayoutMessage();
        phone.setTag(androidselect);
        phone.setImage(R.mipmap.app);
        phone.setType(MyAdapter.LV_TYPE_PIC);
        phone.setTitle("移动安全");
        phone.setContent("提供android或者ios的安全信息");
        msgList.add(phone);

        data = new LayoutMessage();
        data.setTag(dataselect);
        data.setImage(R.mipmap.data);
        data.setType(MyAdapter.LV_TYPE_PIC);
        data.setTitle("数据安全");
        data.setContent("有关数据方面的安全信息");
        msgList.add(data);
*/
        SQLiteDatabase db = typeHelper.getWritableDatabase(); // 获得数据库对象t
        Cursor c = db.query(TypeOpenHelper.TABLE_NAME, new String[]{
                        TypeOpenHelper.TYPEID, TypeOpenHelper.TYPENAME, TypeOpenHelper.ISSELECT,
                        TypeOpenHelper.CONTENT,TypeOpenHelper.LOGO
                },
                null, null, null, null, null, null);
       /* Cursor cursor = db.query(TypeOpenHelper.TABLE_NAME, new String[]{TypeOpenHelper.TYPEID}, null, null, null, null, null);*/
        int idindex=c.getColumnIndex(TypeOpenHelper.TYPEID);
        int nameindex=c.getColumnIndex(TypeOpenHelper.TYPENAME);
        int selectindex=c.getColumnIndex(TypeOpenHelper.ISSELECT);
        int conindex=c.getColumnIndex(TypeOpenHelper.CONTENT);
        int logoindex=c.getColumnIndex(TypeOpenHelper.LOGO);
        while(c.moveToNext()){
            String typeid=c.getString(idindex);
            String typename=c.getString(nameindex);
            int isselect=c.getInt(selectindex);
            int logo=c.getInt(logoindex);
            String content=c.getString(conindex);
            msg = new LayoutMessage();
            msg.setTag(isselect);
            msg.setTypeid(typeid);
            msg.setTitle(typename);
            msg.setType(MyAdapter.LV_TYPE_PIC);
            msg.setImage(logo);
            msg.setContent(content);
            msgList.add(msg);

        }
        return msgList;

    }


    private void initData() {
        contentlist = new ArrayList<LayoutMessage>(getMyData());
        adapter = getAdapter();

    }

    private BaseAdapter getAdapter(){
        return new TypeAdapter(this,contentlist,mListener);
    }

    private void initviews() {
        // TODO Auto-generated method stub
        listView.setAdapter(adapter);

    }

    //响应item点击事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        Toast.makeText(this, "listview的item被点击了！，点击的位置是-->" + position,
                Toast.LENGTH_SHORT).show();
    }
    /**
     * 实现类，响应按钮点击事件
     */
    private TypeAdapter.MyClickListener mListener = new TypeAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            int tag=contentlist.get(position).getTag();
            String type=contentlist.get(position).getTypeid();
            //SQLiteDatabase db = typeHelper.getWritableDatabase(); // 获得数据库对象
            switch (tag){
                case 0:
                    contentlist.get(position).setTag(1);
                    typeHelper.updateSelect(type,1);
                    break;
                case 1:
                    contentlist.get(position).setTag(0);
                    typeHelper.updateSelect(type,0);
                    break;
            }

            adapter=getAdapter();
            listView.setAdapter(adapter);
    }
   };


}
