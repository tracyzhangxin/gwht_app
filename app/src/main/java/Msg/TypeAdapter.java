package Msg;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.gwht.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */

public class TypeAdapter extends BaseAdapter {

    public static final int LV_NO_PIC= 0;// 3种不同的布局
    public static final int LV_Collect= 1; //收藏
    public static final int LV_TYPE_PIC = 2;
    private LayoutInflater mInflater;

    private MyClickListener mListener;


    private List<LayoutMessage> myList;

    public TypeAdapter(Context context, List<LayoutMessage> myList,MyClickListener listener) {
        this.myList = myList;

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListener = listener;
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
        private TextView tv_tag;
        private TextView tv_title;//标题
        private TextView tv_content;//内容
        private ImageView tv_collect;//是否收藏
    }

    class ViewCollect{
        private ImageView iv_newspic;
        private TextView tv_title;//标题

    }
    class ViewType{
        private ImageView tv_logo;
        private TextView tv_title;//标题
        private TextView tv_content;//内容
        private Button tv_btn;//是否订阅
    }


    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        LayoutMessage msg = myList.get(position);
        int type = getItemViewType(position);
        ViewNoPic holderNoPic  = null;
        ViewCollect holderCollect=null;
        ViewType holderType=null;


        if (convertView == null) {

                    holderType=new ViewType();
                    convertView = mInflater.inflate(R.layout.typelist,
                            null);
                    holderType.tv_content = (TextView) convertView
                            .findViewById(R.id.lv1_content);
                    holderType.tv_title = (TextView) convertView
                            .findViewById(R.id.lv1_title);
                    holderType.tv_logo=(ImageView)convertView
                            .findViewById(R.id.lvl_logo);
                    holderType.tv_logo.setImageResource(msg.getImage());
                    holderType.tv_title.setText(msg.getTitle());
                    holderType.tv_content.setText(msg.getContent());
                    holderType.tv_btn=(Button)convertView
                            .findViewById(R.id.lv1_btn);
                    if(msg.getTag()==1) {
                        holderType.tv_btn.setText("-取消");
                        holderType.tv_btn.setBackgroundColor(Color.parseColor("#AAAAAA"));
                    }
                    if(msg.getTag()==0) {
                        holderType.tv_btn.setText("+订阅");
                        holderType.tv_btn.setBackgroundResource(R.drawable.shape);
                    }

                    holderType.tv_btn.setOnClickListener(mListener);
                    holderType.tv_btn.setTag(position);

            }

         else {
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

    /**
     * 用于回调的抽象类
     * @author Ivan Xu
     * 2014-11-26
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }


}