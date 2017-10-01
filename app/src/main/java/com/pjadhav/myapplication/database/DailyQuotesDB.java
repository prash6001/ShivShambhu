package com.pjadhav.myapplication.database;

import android.content.Context;
import android.database.Cursor;

import com.pjadhav.myapplication.model.Quotes;

/**
 * Created by pjadhav on 7/14/17.
 */

public class DailyQuotesDB extends DatabaseHelper {
    private Context mContext;
   // private SQLiteDatabase mDatabase;

    public DailyQuotesDB(Context context) {
        super(context);
        this.mContext = context;
    }

    public Quotes getTodaysQuote () {
        Quotes quotes = new Quotes();
       // List<MusicFile> listMusicFiles = new ArrayList<>();

        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Daily_quotes ORDER BY ROWID ASC LIMIT 1", null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            quotes = new Quotes(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
        }
        cursor.close();
        closeDatabase();
        return quotes;
    }
}
