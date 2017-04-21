package Msg;

import android.content.Context;

import com.baidu.android.pushservice.PushManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */
public class TagManager {
    List <String> alltags;

    public TagManager(){

    }

    public  boolean setTag(Context context,String tag){
        this.alltags.add(tag);
        PushManager.setTags(context,this.alltags);
        return true;
    }

    public boolean delTag(Context context,String deltag){
        List <String> tag=new ArrayList<String>();
        tag.add(deltag);
        PushManager.delTags(context, tag);
        return true;
    }

    public void stopPush(Context context){
        PushManager.stopWork(context);
    }

}
