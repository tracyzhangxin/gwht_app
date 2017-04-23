package com.example.administrator.gwht;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class MsgFragment extends Fragment {

    private ListView lv;
    private String[] title;
    private String[] content;
    private List<LayoutMessage> contentlist;
    private BaseAdapter adapter;
    private NewsOpenHelper myHelper;
    private PullToRefreshListView mPullRefreshListView;
    private int limitNum=5;

    public static final String SP_INFOS = "SPDATA_Files";
    public static final String USERID = "UserID";

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
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int p,
                                    long arg3) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                String url = contentlist.get(p).getUrl().toString();
                int isCollect = contentlist.get(p).getIsCollect();
                int id = contentlist.get(p).getId();
                bundle.putInt("isCollect", isCollect);
                bundle.putInt("id", id);
                Intent intent = new Intent(getContext(), NewsDetail.class);
                intent.putExtra("url", url);
                intent.putExtras(bundle);
                startActivity(intent);
               // Toast.makeText(getActivity(), id + "", Toast.LENGTH_LONG).show();
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

        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFOS, getActivity().MODE_PRIVATE);
        String uid = sp.getString(USERID, ""); // 取Preferences中的帐号

       Cursor c = db.query(NewsOpenHelper.TABLE_NAME, new String[]{
                       NewsOpenHelper.NEWSID, NewsOpenHelper.TITLE,NewsOpenHelper.TYPENAME,
                       NewsOpenHelper.DESCRIBTION, NewsOpenHelper.ISCOLLECT,
                       NewsOpenHelper.URL, NewsOpenHelper.ISREAD, NewsOpenHelper.USERNAME},
               NewsOpenHelper.USERNAME + "=?", new String[]{uid},
               null, null, NewsOpenHelper.NEWSID + " desc", "0," + limitNum);
        int newid=c.getColumnIndex(NewsOpenHelper.NEWSID);
        int typeindex=c.getColumnIndex(NewsOpenHelper.TYPENAME);
        int idindex=c.getColumnIndex(NewsOpenHelper.TITLE);
        int pwdindex=c.getColumnIndex(NewsOpenHelper.DESCRIBTION);
        int urlindex=c.getColumnIndex(NewsOpenHelper.URL);
        int isreadindex=c.getColumnIndex(NewsOpenHelper.ISREAD);
        int iscollectindex=c.getColumnIndex(NewsOpenHelper.ISCOLLECT);
        int uidindex=c.getColumnIndex(NewsOpenHelper.USERNAME);
        while(c.moveToNext()){
            int id=c.getInt(newid);
            String title = c.getString(idindex);
            String desc = c.getString(pwdindex);
            String url=c.getString(urlindex);
            String username=c.getString(uidindex);
            int isRead=c.getInt(isreadindex);
            int isCollect=c.getInt(iscollectindex);
            String typename=c.getString(typeindex);
            msg = new LayoutMessage();
            msg.setId(id);
            msg.setIsRead(isRead);
            msg.setTypename(typename);
            msg.setIsCollect(isCollect);
            msg.setTag(1);
            msg.setType(MyAdapter.LV_NO_PIC);
            msg.setTitle(title);
            msg.setContent(desc);
            msg.setUrl(url);
            //Toast.makeText(getActivity(),desc, Toast.LENGTH_LONG).show();
            msgList.add(msg);

        }

        db.close();


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

      /*  lv = (ListView) getActivity().findViewById(R.id.health_news_lv);
        lv.setAdapter(adapter);*/
        mPullRefreshListView = (PullToRefreshListView)getActivity().findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullRefreshListView.setAdapter(adapter);
    }
    private class GetDataTask extends AsyncTask<Void, Void, String> {

        //后台处理部分
        @Override
        protected String doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            String str="Added after refresh...I add";
            return str;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(String result) {
            //在头部增加新添
            if (mPullRefreshListView.isHeaderShown()){
                contentlist.clear();
                contentlist.addAll(getMyData());
            }else if(mPullRefreshListView.isFooterShown()){
                contentlist.clear();
                limitNum+=5;
                contentlist.addAll(getMyData());
            }
            //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
            adapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }
}
