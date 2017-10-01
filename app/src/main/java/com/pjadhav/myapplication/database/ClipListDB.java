package com.pjadhav.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pjadhav.myapplication.model.MusicFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjadhav on 7/14/17.
 */

public class ClipListDB extends DatabaseHelper {

    public static final String DBNAME = "shivaji_clips.db";
    private Context mContext;

    public ClipListDB(Context context){
        super(context);
        this.mContext = context;
    }

    public List<MusicFile> getMusicFiles () {
        MusicFile musicFile= null;
        List<MusicFile> listMusicFiles = new ArrayList<>();
        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Clip_details", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            musicFile = new MusicFile(cursor.getInt(0), cursor.getString(1),cursor.getString(2), cursor.getInt(3));
            listMusicFiles.add(musicFile);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return listMusicFiles;
    }

    public int isDownloaded (String fileName){
        int isDwd = 0;
        MusicFile musicFile= null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("Select * from Clip_details where FILE_NAME = " + "'"+fileName+"'"  , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            musicFile = new MusicFile(cursor.getInt(0), cursor.getString(1),cursor.getString(2), cursor.getInt(3));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        if(musicFile.isDownloaded() == 1){
            return 1;
        }else {
            return 0;
        }
    }

    public void setDownloaded(String fileName) {
        openDatabase();
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IS_DOWNLOADED", "1");

        String selection = "FILE_NAME='" + fileName + "'";

        long insertUpdateCount = 0;
        database.update(CLIP_DETAILS_TABLE, values, selection, null);

        database.close();
    }
}
