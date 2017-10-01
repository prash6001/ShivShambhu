package com.pjadhav.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pjadhav on 6/12/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "shivaji_clips.db";
    public static final String DB_LOCATION= "/data/data/com.pjadhav.shivshambhu/databases";
    private Context mContext;
    public SQLiteDatabase mDatabase;
    public static final String CLIP_DETAILS_TABLE = "Clip_details";
    public static final String DAILY_QUOTE_TABLE = "Daily_quotes";

    public DatabaseHelper(Context context){
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

  public void openDatabase (){
        String dbPath= mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()){
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase (){
        if(mDatabase !=null){
            mDatabase.close();
        }
    }



}
