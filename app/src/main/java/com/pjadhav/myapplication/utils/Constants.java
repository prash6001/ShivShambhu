package com.pjadhav.myapplication.utils;

import android.os.Environment;

/**
 * Created by pjadhav on 6/26/17.
 */

public final class Constants {

    public static final String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/shiv_shambhu";
    public static final int maxDownloadCount = 2;
    public static int downloadClickCount = 0;
}
