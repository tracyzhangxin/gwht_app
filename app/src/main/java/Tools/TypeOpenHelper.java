package Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.gwht.R;

/**
 * Created by Administrator on 2017/4/19.
 */
public class TypeOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Msg2"; // 数据库文件名称
    public static final String TABLE_NAME = "Type"; // 表名
    public static final String ID = "id"; // 字段：ID
    public static final String TYPEID = "typeid"; // 字段：typeid
    public static final String TYPENAME= "typename"; // 字段：typeid
    public static final String ISSELECT = "isselect"; //字段： 是否选择
    public static final String LOGO = "logo"; //字段： 是否选择
    public static final String CONTENT = "content"; //字段： 是否选择


    public TypeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override    // 重写onCreate方法,首次执行时执行该方法，生成对应表
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL("create table if not exists "
                + TABLE_NAME
                + " (" // 调用execSQL方法创建表
                + ID + " INTEGER  primary key," +
                TYPEID + " varchar(100),"
                + TYPENAME + "  varchar(100),"
                + ISSELECT + " int default 0)");*/
        db.execSQL("create table if not exists Type ( " +
                "id INTEGER primary key,typeid varchar(100),typename varchar(100)," +
                "content varchar(200),logo int," +
                "isselect int default 0 );");
        //this.insertType();

        ContentValues values=new ContentValues();
        values.put(TYPEID,"news");
        values.put(TYPENAME,"安全资讯");
        values.put(ISSELECT,0);
        values.put(LOGO, R.mipmap.news);
        values.put(CONTENT,"提供最新的安全资讯");
        db.insert(this.TABLE_NAME, null, values);
        values.clear();

        values.put(TYPEID, "web");
        values.put(TYPENAME, "安全资讯");
        values.put(ISSELECT,0);
        values.put(LOGO, R.mipmap.chrome);
        values.put(CONTENT,"提供最新的web安全漏洞信息");
        db.insert(this.TABLE_NAME, null, values);
        values.clear();

        values.put(TYPEID, "system");
        values.put(TYPENAME, "系统安全");
        values.put(ISSELECT,0);
        values.put(LOGO, R.mipmap.windows);
        values.put(CONTENT,"关于系统方面的最新安全信息");
        db.insert(this.TABLE_NAME, null, values);
        values.clear();

        values.put(TYPEID, "phone");
        values.put(TYPENAME, "移动安全");
        values.put(ISSELECT,0);
        values.put(LOGO, R.mipmap.app);
        values.put(CONTENT,"提供android或者ios的安全信息");
        db.insert(this.TABLE_NAME, null, values);
        values.clear();

        values.put(TYPEID,"data");
        values.put(TYPENAME, "数据安全");
        values.put(ISSELECT,0);
        values.put(LOGO, R.mipmap.data);
        values.put(CONTENT,"有关数据方面的安全信息");
        db.insert(this.TABLE_NAME, null, values);
        values.clear();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 重写onUpgrade方法
    }

    private void insertType(){

        SQLiteDatabase db=this.getWritableDatabase();
        String []types={"安全资讯","web安全","系统安全","移动安全","数据安全"};
        for (int i=0;i<types.length;i++){
            ContentValues values=new ContentValues();
            values.put(TYPEID,i);
            values.put(TYPENAME,types[i]);
            values.put(ISSELECT,0);
            db.insert(this.TABLE_NAME, null, values);
            values.clear();
        }

    }
    public boolean updateSelect(String typeid,int isSelect){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ISSELECT,isSelect);
        int flag=0;
        try{
            flag=db.update(TABLE_NAME, cv, TYPEID + "=?", new String[]{typeid});
        }catch (Exception e){

        }
        if(flag>0)
            return true;
        else
            return false;
    }

}