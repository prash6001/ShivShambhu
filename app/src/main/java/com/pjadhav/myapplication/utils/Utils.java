package com.pjadhav.myapplication.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by pjadhav on 6/25/17.
 */

public class Utils {

    public static final String CLOSE = "Close";
    public static final String BUILDING = "Building";
    public static final String BOOK = "Book";
    public static final String PAINT = "Paint";
    public static final String CASE = "Case";
    public static final String SHOP = "Shop";
    public static final String PARTY = "Party";
    public static final String MOVIE = "Movie";


    public static Typeface setFontface(Context context){
        Typeface fontMarathi = Typeface.createFromAsset(context.getResources().getAssets(),
                "Shivaji_font.ttf");
        return fontMarathi;
    }
}
