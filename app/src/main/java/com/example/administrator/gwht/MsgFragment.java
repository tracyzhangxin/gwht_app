package com.example.administrator.gwht;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.db.Selector;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Msg.LayoutMessage;
import Msg.MyAdapter;
import Tools.NewsOpenHelper;


public class MsgFragment extends Fragment {

    private ListView lv;
    private String[] title;
    private String[] content;
    private List<LayoutMessage> contentlist;
    private BaseAdapter adapter;
    private NewsOpenHelper myHelper;

    private ImageView iv_more;
    List<Map<String, String>> moreList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        x.view().inject(this, view);//注入view和事件
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myHelper = new NewsOpenHelper(getActivity(), NewsOpenHelper.DB_NAME, null, 1);// 打开数据表库表，
        initData();
        initviews();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int p,
                                    long arg3) {
                // TODO Auto-generated method stub
                String url = contentlist.get(p).getUrl().toString();
                Intent intent = new Intent(getContext(), NewsDetail.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }

        });

    }

    public static MsgFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        MsgFragment fragment = new MsgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<LayoutMessage> getMyData(){
        List<LayoutMessage> msgList = new ArrayList<LayoutMessage>();
        LayoutMessage msg;
        title = getResources().getStringArray(R.array.lv_title);
        content = getResources().getStringArray(R.array.lv_content);

        SQLiteDatabase db = myHelper.getWritableDatabase(); // 获得数据库对象
        ContentValues values = new ContentValues();
        values.put(NewsOpenHelper.TITLE, title[1]);
        values.put(NewsOpenHelper.DESCRIBTION, content[1]);
        values.put(NewsOpenHelper.URL, "http://www.mottoin.com/96267.html");
        values.put(NewsOpenHelper.RUNTIME, "20131012");
      /*  long rid = db.insert(NewsOpenHelper.TABLE_NAME, NewsOpenHelper.NEWSID, values); // 插入数据*/


       Cursor c = db.query(NewsOpenHelper.TABLE_NAME, new String[]{
                       NewsOpenHelper.NEWSID, NewsOpenHelper.TITLE, NewsOpenHelper.DESCRIBTION,
                       NewsOpenHelper.URL, NewsOpenHelper.RUNTIME}, null, null,
               null, null, NewsOpenHelper.NEWSID+" desc", null);
        int idindex=c.getColumnIndex(NewsOpenHelper.TITLE);
        int pwdindex=c.getColumnIndex(NewsOpenHelper.DESCRIBTION);
        int urlindex=c.getColumnIndex(NewsOpenHelper.URL);
        while(c.moveToNext()){
            String title = c.getString(idindex);
            String desc = c.getString(pwdindex);
            String url=c.getString(urlindex);
            msg = new LayoutMessage();
            msg.setTag(1);
            msg.setType(MyAdapter.LV_NO_PIC);
            msg.setTitle(title);
            msg.setContent(desc);
            msg.setUrl(url);
            //Toast.makeText(getActivity(),url, Toast.LENGTH_LONG).show();
            msgList.add(msg);

        }
        db.close();

       /* msg = new LayoutMessage();
        msg.setTag(1);
        msg.setType(MyAdapter.LV_NO_PIC);
        msg.setTitle(title[0]);
        msg.setContent(content[0]);
        msg.setUrl("http://www.mottoin.com/96277.html");
        msgList.add(msg);*/

        return msgList;

    }

    private void initData() {

        contentlist = new ArrayList<LayoutMessage>(getMyData());
        adapter = getAdapter();
    }

    private BaseAdapter getAdapter(){
        return new MyAdapter(getActivity(),contentlist);
    }

    private void initviews() {
        // TODO Auto-generated method stub

        lv = (ListView) getActivity().findViewById(R.id.health_news_lv);
        lv.setAdapter(adapter);



    }
}
