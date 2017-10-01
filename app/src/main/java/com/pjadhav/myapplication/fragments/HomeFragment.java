package com.pjadhav.myapplication.fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pjadhav.myapplication.R;
import com.pjadhav.myapplication.utils.Constants;

import java.util.concurrent.TimeUnit;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class HomeFragment extends Fragment implements ScreenShotable {

    private View containerView;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;

    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private android.os.Handler myHandler = new android.os.Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    public static int oneTimeOnly = 0;
    // button to show progress dialog
    private Button btnDownload,playButton,pauseButton,btnPreviousClips;
    private TextView txtFileName, startTimeField, endTimeField;
    private SeekBar seekbar;
    // Progress Dialog
    private ProgressDialog pDialog;
    ImageView my_image;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    public int curentProgress;
    // File url to download
    public static String file_url = "https://drive.google.com/uc?export=download&id=0B0H4ZxGOmtZHYzZMZ1NPanI1SWc";

    private OnHomeFragmentInteractionListener mListner;



    public static HomeFragment newInstance(int resId) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container_frame);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            mListner = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClipListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListner = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        btnDownload = (Button) rootView.findViewById(R.id.btnDownload);
        playButton = (Button) rootView.findViewById(R.id.playButton);
        pauseButton = (Button) rootView.findViewById(R.id.pauseButton);
        my_image = (ImageView) rootView.findViewById(R.id.my_image);
        txtFileName = (TextView) rootView.findViewById(R.id.txtFileName);
        seekbar = (SeekBar) rootView.findViewById(R.id.seekBarMusic);
        startTimeField = (TextView) rootView.findViewById(R.id.startTimeField);
        endTimeField = (TextView) rootView.findViewById(R.id.endTimeField);
        btnPreviousClips = (Button) rootView.findViewById(R.id.btnPreviousClips);

        mediaPlayer=new MediaPlayer();
        try{
            //you can change the path, here path is external directory(e.g. sdcard) /Music/GetThisFromDB.mp3
            mediaPlayer.setDataSource(Constants.folderPath+"/GetThisFromDB.mp3");

            mediaPlayer.prepare();
        }catch(Exception e){e.printStackTrace();}

        seekbar.setClickable(false);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                curentProgress=seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(curentProgress < seekBar.getProgress()){
                    forward();
                }else{
                    rewind();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onHomeFragmentInteraction(file_url);
              //  new DownloadFileFromURL().execute();
            }

        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

        return rootView;
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                if(containerView != null) {
                    Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                            containerView.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    containerView.draw(canvas);
                    HomeFragment.this.bitmap = bitmap;
                }
            }
        };
        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }


//    /**
//     * Background Async Task to download file
//     * */
//    class DownloadFileFromURL extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread
//         * Show Progress Bar Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//           // showDialog(progress_bar_type);
//        }
//
//        /**
//         * Downloading file in background thread
//         * */
//        @Override
//        protected String doInBackground(String... f_url) {
//            int count;
//            try {
//                URL url = new URL(f_url[0]);
//                URLConnection conection = url.openConnection();
//                conection.connect();
//                // this will be useful so that you can show a tipical 0-100% progress bar
//                int lenghtOfFile = conection.getContentLength();
//
//                // download the file
//                InputStream input = new BufferedInputStream(url.openStream(), 8192);
//
//                // Output stream
//                OutputStream output = new FileOutputStream("/sdcard/song.mp3");
//
//                byte data[] = new byte[1024];
//
//                long total = 0;
//
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    // publishing the progress....
//                    // After this onProgressUpdate will be called
//                    publishProgress(""+(int)((total*100)/lenghtOfFile));
//
//                    // writing data to file
//                    output.write(data, 0, count);
//                }
//
//                // flushing output
//                output.flush();
//
//                // closing streams
//                output.close();
//                input.close();
//
//            } catch (Exception e) {
//                Log.e("Error: ", e.getMessage());
//            }
//
//            return null;
//        }
//
//        /**
//         * Updating progress bar
//         * */
//        protected void onProgressUpdate(String... progress) {
//            // setting progress percentage
//           // pDialog.setProgress(Integer.parseInt(progress[0]));
//        }
//
//        /**
//         * After completing background task
//         * Dismiss the progress dialog
//         * **/
//        @Override
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after the file was downloaded
//            //dismissDialog(progress_bar_type);
//         //   Toast.makeText(getActivity(), "Download completed!", Toast.LENGTH_SHORT).show();
//
//            // btnDownload.setText("PLAY");
//            //txtFileName.setText("Downloaded mp3 file");
//
//            // Displaying downloaded image into image view
//            // Reading image path from sdcard
//            // String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
//            // setting downloaded into image view
//            //  my_image.setImageDrawable(Drawable.createFromPath(imagePath));
//        }
//
//    }



    @TargetApi(Build.VERSION_CODES.GINGERBREAD) public void play(){
        Toast.makeText(getActivity(), "Playing sound",
                Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0){
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        endTimeField.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) finalTime)))
        );
        startTimeField.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) startTime)))
        );
        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD) public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void pause(){
        Toast.makeText(getActivity(), "Pausing sound",
                Toast.LENGTH_SHORT).show();

        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    public void forward(){
        int temp = (int)startTime;
        if((temp+forwardTime)<=finalTime){
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
        else{
            Toast.makeText(getActivity(),
                    "Cannot jump forward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void rewind(){
        int temp = (int)startTime;
        if((temp-backwardTime)>0){
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
        else{
            Toast.makeText(getActivity(),
                    "Cannot jump backward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }

   public interface OnHomeFragmentInteractionListener {
       void onHomeFragmentInteraction(String fileUrl);
   }

}

