package Tools;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLDataException;
import java.sql.SQLException;

import Model.NewsModel;

/**
 * Created by Administrator on 2017/2/19.
 */
public class NewsOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Msg"; // 数据库文件名称
    public static final String TABLE_NAME = "News"; // 表名
    public static final String NEWSID = "NewsId"; // 字段：ID
    public static final String TITLE = "Title"; // 字段：标题
    public static final String DESCRIBTION = "Descbrition"; //字段： 简介
    public static final String URL = "Url"; //字段：连接
    public static final String RUNTIME = "runtime"; //字段：时间
    public static final String USERNAME="username";
    public static final String ISREAD="isRead";
    public static final String ISCOLLECT="isCollect";//是否收藏

    public static final String SP_INFOS = "SPDATA_Files";
    public static final String USERID = "UserID";
    public NewsOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }
    @Override	// 重写onCreate方法,首次执行时执行该方法，生成对应表
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "
                + TABLE_NAME
                + " (" // 调用execSQL方法创建表
                + NEWSID + " INTEGER  primary key," + TITLE + " varchar(100),"
                +USERNAME+" varchar(100),"
                + DESCRIBTION + " varchar(500)," + URL + " varchar(100)," + ISREAD + " int default 0,"
                +ISCOLLECT+" int default 0,"
                + RUNTIME + " varchar(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 重写onUpgrade方法
    }

    public boolean insertNews(NewsModel.News news,Context context){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("Title",news.title);
        values.put("Descbrition",news.desc);
        values.put("Url",news.url);
        values.put("runtime",news.runtime);
        values.put("isRead",news.isRead);
        values.put("isCollect",news.isCollect);

        SharedPreferences sp = context.getSharedPreferences(SP_INFOS, context.MODE_PRIVATE);
        String uid = sp.getString(USERID, null); // 取Preferences中的帐号
        values.put("username",uid);
        long rid=db.insert(this.TABLE_NAME, this.NEWSID, values);
        db.close();
        if (rid>0)
            return true;
        else
            return false;


    }
    public  int getNoReadNum(String username){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=null;
        c=db.rawQuery("select count(*) from "+NewsOpenHelper.TABLE_NAME+" where isRead=0" +
                    " and username="+username,null);


        int num=0;
        if (c.moveToFirst())
           num=c.getInt(0);
        db.close();
        return num;

    }

    public Boolean updateCollect(String url,int isCollect){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ISCOLLECT,isCollect);
        int flag=0;
        try{
            flag=db.update(TABLE_NAME, cv, URL + "=?", new String[]{url});
        }catch (Exception e){

        }
        if(flag>0)
            return true;
        else
            return false;

    }


}
