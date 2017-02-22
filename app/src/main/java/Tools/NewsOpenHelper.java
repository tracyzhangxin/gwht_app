package Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String ISREAD="isRead";
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
                + DESCRIBTION + " varchar(500)," + URL + " varchar(100)," + ISREAD + " int," + RUNTIME
                + " varchar(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 重写onUpgrade方法
    }

    public boolean insertNews(NewsModel.News news){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("Title",news.title);
        values.put("Descbrition",news.desc);
        values.put("Url",news.url);
        values.put("runtime",news.runtime);
        values.put("isRead",news.isRead);
        long rid=db.insert(this.TABLE_NAME, this.NEWSID, values);
        db.close();
        if (rid>0)
            return true;
        else
            return false;


    }

}
