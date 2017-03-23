package Tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import Model.UserInfoModel;

/**
 * Created by Administrator on 2017/2/15.
 */
public class PreferenceUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "userInfo";

    //在onBind回调时使用保存channelid和userid
    public static boolean saveChannelid(Context context,String ChannelId,String UserId){
        setParam(context,"channelid",ChannelId);
        setParam(context,"userid",UserId);
        return true;
    }
    //保存或者修改numberBadgeItem
    public static boolean saveNumberBadgeItem(Context context,int number){
        setParam(context,"numberBadgeItem",number);
        return true;
    }
    //获取numberBadgeItem
    public static int getNumberBadgeItem(Context context){
        int number=Integer.valueOf(getParam(context,"numberBadgeItem","").toString());
        return number;
    }
    //检查token判断是否登录
    public static boolean checkIfLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String username = sp.getString("isLogin", "");
        if(sp == null || username.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    //保存用户信息
    public static boolean saveUserInfo(Context context, UserInfoModel ui) {
        setParam(context, "userid", ui.userid);
        setParam(context, "username", ui.username);
        setParam(context, "isLogin", true);
        setParam(context, "runtime", ui.runtime);
        return true;
    }


    //获取用户信息
    public static Map<String, String> getUserInfo(Context context) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("userid", getParam(context, "userid", "").toString());
            map.put("username", getParam(context, "username", "").toString());
            map.put("channelid",getParam(context,"channelid","").toString());
            map.put("runtime", getParam(context, "runtime", "").toString());

        } catch (Exception ex) {

        }
        return map;
    }

    //注销，清除用户信息
    public static boolean deleteUserInfo(Context context) {
        setParam(context, "userid", "");
        setParam(context, "username", "");
        setParam(context, "isLogin", false);
        setParam(context, "runtime", "");
        return true;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }
}