package com.pjadhav.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pjadhav.myapplication.database.ClipListDB;
import com.pjadhav.myapplication.database.DatabaseHelper;
import com.pjadhav.myapplication.events.FragmentEvent;
import com.pjadhav.myapplication.events.GlobalBus;
import com.pjadhav.myapplication.fragments.ClipListFragment;
import com.pjadhav.myapplication.fragments.DailyQuotesFragment;
import com.pjadhav.myapplication.fragments.HomeFragment;
import com.pjadhav.myapplication.utils.Constants;
import com.pjadhav.myapplication.utils.Utils;
import org.greenrobot.eventbus.Subscribe;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;


public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener, HomeFragment.OnHomeFragmentInteractionListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private HomeFragment homeFragment;
    private ViewAnimator viewAnimator;
    private int res;
    private LinearLayout linearLayout;
    private ClipListDB mDBHelper;
    private static final String TAG = MainActivity.class.getName();
    Toolbar toolbar;
    TextView toolbar_title;
    File clipDirectory;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = R.id.content_frame;
        homeFragment = HomeFragment.newInstance(R.id.content_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, homeFragment)
                .commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        mContext = this;

        //authorize app for permissions
        verifyStoragePermissions(this);
        setActionBar();
        createMenuList();
        createClipDirectory();
        viewAnimator = new ViewAnimator<>(this, list, homeFragment, drawerLayout, this);
        mDBHelper = new ClipListDB(this);
        //Check if db exists
        File databaseFile = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);

        if (false == databaseFile.exists()){

            mDBHelper.getReadableDatabase();
            CopyDB(DatabaseHelper.DBNAME);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getMessage(FragmentEvent.ActivityFragmentMessage fragmentActivityMessage) {

        Toast.makeText(getApplicationContext(),
                " " + fragmentActivityMessage.getMessage(),
                Toast.LENGTH_SHORT).show();
    }

    private void createClipDirectory(){
        clipDirectory = new File(Constants.folderPath);
        if (!clipDirectory.exists()) {
            clipDirectory.mkdir();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 112);
        }
    }

    private void CopyDB(String dbname) {
        InputStream myInput;
        try {
            myInput = this.getAssets().open(dbname);

            // Path to the just created empty db
            String outFileName = this.getFilesDir().getAbsolutePath();
            outFileName = outFileName
                    .substring(0, outFileName.lastIndexOf("/"))
                    + "/databases/"
                    + dbname;

            Log.d(TAG,"outFileName : " + outFileName);

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

            Log.d(TAG,"CopyDB is Done");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(Utils.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(Utils.BUILDING, R.drawable.icn_1);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(Utils.BOOK, R.drawable.icn_2);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(Utils.PAINT, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(Utils.CASE, R.drawable.icn_4);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(Utils.SHOP, R.drawable.icn_5);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(Utils.PARTY, R.drawable.icn_6);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(Utils.MOVIE, R.drawable.icn_7);
        list.add(menuItem7);
    }


    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        toolbar_title.setText("ShivShambhu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition, int fragPos) {
        this.res = R.id.content_frame;
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();

        switch (fragPos) {
            case 1:
                HomeFragment homeFragment = HomeFragment.newInstance(this.res);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment).commit();
                toolbar_title.setText("ShivShambhu");
                return homeFragment;
            case 2:
                DailyQuotesFragment dailyQuotesFragment = DailyQuotesFragment.newInstance(this.res);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, dailyQuotesFragment).commit();
                toolbar_title.setText("Daily Quotes");
                return dailyQuotesFragment;
            case 3:
                ClipListFragment clipListFragment = ClipListFragment.newInstance(this.res);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, clipListFragment).commit();
                toolbar_title.setText("Clip List");
                return clipListFragment;
            default:
                return null;
                }
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case Utils.CLOSE:
                return screenShotable;
            case Utils.BUILDING:
                return replaceFragment(screenShotable,position, 1);
            case Utils.BOOK:
                return replaceFragment(screenShotable,position, 2);
            case Utils.PAINT:
                return replaceFragment(screenShotable,position, 3);
            default:
                return replaceFragment(screenShotable, position, 0);
        }
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }


    public void onHomeFragmentInteraction(String item){
       // Toast.makeText(this,""+item,Toast.LENGTH_SHORT).show();
        createClipDirectory();
        new DownloadFileFromURL("GetThisFromDB.mp3",item).execute();
    }

    public void sendMessageToFragment(View view) {
        FragmentEvent.ActivityFragmentMessage activityFragmentMessageEvent =
                new FragmentEvent.ActivityFragmentMessage(String.valueOf("Hi Fragment"));

        GlobalBus.getBus().post(activityFragmentMessageEvent);
    }

    @Subscribe
    public void getMessageFromAdapter(FragmentEvent.ClipDownloadFromListMessage adapterActivityMessage) {

        Toast.makeText(getApplicationContext(),
                " " + adapterActivityMessage.getMusicFile().getFileName(),
                Toast.LENGTH_SHORT).show();
        createClipDirectory();
        new DownloadFileFromURL(adapterActivityMessage.getMusicFile().getFileName(),adapterActivityMessage.getMusicFile().getFileUrl()).execute();
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        String file_name, file_url;

        public DownloadFileFromURL (String fileName, String fileUrl){
            this.file_name = fileName;
            this.file_url = fileUrl;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showDialog(progress_bar_type);
        }
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(file_url);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                File file = new File(clipDirectory, file_name);

                if(file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                // Output stream
                OutputStream output = new FileOutputStream(file);


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            // pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            if(mDBHelper == null){
                mDBHelper = new ClipListDB(mContext);
            }
            mDBHelper.setDownloaded(file_name);
            FragmentEvent.ChangeButtonImageOnListMessage changeImageMessageEvent =
                    new FragmentEvent.ChangeButtonImageOnListMessage(
                            String.valueOf("Play"));
            GlobalBus.getBus().post(changeImageMessageEvent);

              // Toast.makeText(getApplicationContext(), "Download completed!", Toast.LENGTH_SHORT).show();

            // btnDownload.setText("PLAY");
            //txtFileName.setText("Downloaded mp3 file");

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            // String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
            //  my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
}

