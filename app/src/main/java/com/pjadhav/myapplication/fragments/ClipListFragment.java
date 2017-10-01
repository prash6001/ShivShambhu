package com.pjadhav.myapplication.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pjadhav.myapplication.R;
import com.pjadhav.myapplication.adapters.MyItemRecyclerViewAdapter;
import com.pjadhav.myapplication.database.ClipListDB;
import com.pjadhav.myapplication.events.FragmentEvent;
import com.pjadhav.myapplication.events.GlobalBus;
import com.pjadhav.myapplication.model.MusicFile;
import com.pjadhav.myapplication.utils.Constants;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * A fragment representing a list of Items.
 */
public class ClipListFragment extends Fragment implements ScreenShotable{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    ImageButton img_btn_download;
    ClipListDB mDbHelper;
    List<MusicFile> mMusicFiles;
    RecyclerView recyclerView;
    MyItemRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClipListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ClipListFragment newInstance(int resId) {
        ClipListFragment clipListFragment = new ClipListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        clipListFragment.setArguments(bundle);
        return clipListFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Subscribe
    public void getMessageFromAdapter(FragmentEvent.ClipDownloadFromListMessage activityFragmentMessage) {
        //Write code to perform action after event is received.
    }

    @Subscribe
    public void getMessageFromActivity(FragmentEvent.ChangeButtonImageOnListMessage adapterActivityMessage) {
        if(mDbHelper == null){
            mDbHelper = new ClipListDB(getActivity());
        }
        Constants.downloadClickCount--;
        Log.d("Download","After +++++++++++++++ : "+ Constants.downloadClickCount);
       // mMusicFiles = mDbHelper.getMusicFiles();
        if(recyclerView != null && adapter != null){
            recyclerView.invalidate();
            adapter.notifyDataSetChanged();
        }

        Toast.makeText(getActivity(), "Download completed : "+adapterActivityMessage.getMessageToChangeButtonImage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clip_item_list, container, false);
        //Register the blobal event bus
        GlobalBus.getBus().register(this);

      //  img_btn_download = (ImageButton) view.findViewById(R.id.img_btn_download);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mDbHelper = new ClipListDB(getActivity());
            mMusicFiles = mDbHelper.getMusicFiles();
            adapter = new MyItemRecyclerViewAdapter(mMusicFiles, getActivity(), img_btn_download);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void takeScreenShot() {
    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

}
