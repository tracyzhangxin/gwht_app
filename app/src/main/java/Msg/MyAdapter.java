package Msg;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.gwht.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */

public class MyAdapter extends BaseAdapter {

    public static final int LV_NO_PIC= 0;// 3种不同的布局
    public static final int LV_Collect= 1; //收藏
    public static final int LV_BIG_PIC = 2;
    private LayoutInflater mInflater;

    private List<LayoutMessage> myList;

    public MyAdapter(Context context, List<LayoutMessage> myList) {
        this.myList = myList;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return myList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    //没有图片
    class ViewNoPic {
        private TextView tv_title;//标题
        private TextView tv_content;//内容
    }

    class ViewCollect{
        private ImageView iv_newspic;
        private TextView tv_title;//标题

    }


    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        LayoutMessage msg = myList.get(position);
        int type = getItemViewType(position);
        ViewNoPic holderNoPic  = null;
        ViewCollect holderCollect=null;


        if (convertView == null) {
            switch (type) {
                //没有图片
                case LV_NO_PIC:
                    holderNoPic = new ViewNoPic();
                    convertView = mInflater.inflate(R.layout.msglistview,
                            null);
                    holderNoPic.tv_title = (TextView) convertView
                            .findViewById(R.id.lv1_title);
                    if (msg.getIsRead()==0){
                        holderNoPic.tv_title.setTextColor(Color.parseColor("#8D6E63"));
                    }else{
                        holderNoPic.tv_title.setTextColor(Color.parseColor("#8D6E63"));
                    }
                    holderNoPic.tv_content = (TextView) convertView
                            .findViewById(R.id.lv1_content);
                    holderNoPic.tv_title.setText(msg.getTitle());
                    holderNoPic.tv_content.setText(msg.getContent());
                    convertView.setTag(holderNoPic);
                    break;
                case LV_Collect:
                    holderCollect=new ViewCollect();
                    convertView = mInflater.inflate(R.layout.collectlistview,
                            null);
                    holderCollect.tv_title = (TextView) convertView
                            .findViewById(R.id.lv1_title);
                   /* holderCollect.iv_newspic=(ImageView)convertView
                            .findViewById(R.id.lv1_pic);*/
                   /* if (msg.getIsRead()==0){
                        holderNoPic.tv_title.setTextColor(Color.parseColor("#8D6E63"));
                    }else{
                        holderNoPic.tv_title.setTextColor(Color.parseColor("#8D6E63"));
                    }*/
                   /* holderCollect.iv_newspic.setImageResource(R.drawable.android_smaller);*/
                    holderCollect.tv_title.setText(msg.getTitle());
                    break;
                default:
                    break;
            }

        } else {
            Log.d("baseAdapter", "Adapter_:" + (convertView == null));
            switch (type) {
                case LV_NO_PIC:
                    holderNoPic=(ViewNoPic)convertView.getTag();
                    holderNoPic.tv_title.setText(msg.getTitle());
                    holderNoPic.tv_content.setText(msg.getContent());
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }

    /**
     * 根据数据源的position返回�?��显示的的layout的type
     *
     * type的�?必须�?�?��
     *
     * */
    @Override
    public int getItemViewType(int position) {

        LayoutMessage msg = myList.get(position);
        int type = msg.getType();
        Log.e("TYPE:", "" + type);
        return type;
    }

    /**
     * 返回�?��的layout的数�?
     *
     * */
    @Override
    public int getViewTypeCount() {
        return 3;
    }


}