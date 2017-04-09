package com.example.administrator.gwht;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Msg.LayoutMessage;
import Msg.MyAdapter;
import Tools.NewsOpenHelper;


public class LoveFragment extends Fragment {
    private ListView lv;
    private String[] title;
    private Context context;
    private String[] content;
    private SwipeMenuCreator creator;
    private SwipeMenuListView listView;
    private List<LayoutMessage> contentlist;
    private BaseAdapter adapter;
    private NewsOpenHelper myHelper;
    private PullToRefreshListView mPullRefreshListView;
    private int limitNum=5;

    private ImageView iv_more;
    List<Map<String, String>> moreList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_love, container, false);
        x.view().inject(this, view);//注入view和事件
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myHelper = new NewsOpenHelper(getActivity(), NewsOpenHelper.DB_NAME, null, 1);// 打开数据表库表，
        initData();
        initviews();

    }

    public static LoveFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        LoveFragment fragment = new LoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<LayoutMessage> getMyData(){
        List<LayoutMessage> msgList = new ArrayList<LayoutMessage>();
        LayoutMessage msg;
        SQLiteDatabase db = myHelper.getWritableDatabase(); // 获得数据库对象
        title = getResources().getStringArray(R.array.lv_title);
        content = getResources().getStringArray(R.array.lv_content);

        Cursor c = db.query(NewsOpenHelper.TABLE_NAME, new String[]{
                        NewsOpenHelper.NEWSID, NewsOpenHelper.TITLE, NewsOpenHelper.DESCRIBTION,
                        NewsOpenHelper.URL, NewsOpenHelper.ISREAD}, null, null,
                null, null, NewsOpenHelper.NEWSID+" desc", "0,"+limitNum);
        int idindex=c.getColumnIndex(NewsOpenHelper.TITLE);
        int pwdindex=c.getColumnIndex(NewsOpenHelper.DESCRIBTION);
        int urlindex=c.getColumnIndex(NewsOpenHelper.URL);
        int isreadindex=c.getColumnIndex(NewsOpenHelper.ISREAD);
        while(c.moveToNext()){
            String title = c.getString(idindex);
            String desc = c.getString(pwdindex);
            String url=c.getString(urlindex);
            msg = new LayoutMessage();
            msg.setTag(1);
            msg.setType(MyAdapter.LV_Collect);
            msg.setTitle(title);
            msg.setContent(desc);
            msg.setUrl(url);
            //Toast.makeText(getActivity(),url, Toast.LENGTH_LONG).show();
            msgList.add(msg);

        }
        db.close();
       /* msg = new LayoutMessage();
        msg.setTag(1);
        msg.setType(MyAdapter.LV_Collect);
        msg.setTitle("浅谈XXE攻击");
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
        /*lv = (ListView) getActivity().findViewById(R.id.health_news_lv);
        lv.setAdapter(adapter);*/

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem openItem = new SwipeMenuItem(context);
                openItem.setBackground(new ColorDrawable(Color.RED));
                openItem.setWidth(dp2px(90));
                openItem.setTitleSize(20);
                openItem.setTitle("删除");
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);


            }
        };
        listView = (SwipeMenuListView)(getActivity()).findViewById(R.id.listView);
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu,int index) {
                //index的值就是在SwipeMenu依次添加SwipeMenuItem顺序值，类似数组的下标。
                //从0开始，依次是：0、1、2、3...
                switch (index) {
                    case 0:
                        Toast.makeText(x.app(), "删除:" + position, Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(x.app(), "删除:"+position,Toast.LENGTH_SHORT).show();
                        break;
                }

                // false : 当用户触发其他地方的屏幕时候，自动收起菜单。
                // true : 不改变已经打开菜单的样式，保持原样不收起。
                return false;
            }
        });

        // 监测用户在ListView的SwipeMenu侧滑事件。
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int pos) {
                Log.d("位置:" + pos, "开始侧滑...");
            }

            @Override
            public void onSwipeEnd(int pos) {
                Log.d("位置:" + pos, "侧滑结束.");
            }
        });

        listView.setAdapter(adapter);
    }
    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
