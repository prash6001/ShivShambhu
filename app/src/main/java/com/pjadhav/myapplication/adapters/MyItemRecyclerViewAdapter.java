package com.pjadhav.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pjadhav.myapplication.R;
import com.pjadhav.myapplication.database.ClipListDB;
import com.pjadhav.myapplication.events.FragmentEvent;
import com.pjadhav.myapplication.events.GlobalBus;
import com.pjadhav.myapplication.model.MusicFile;
import com.pjadhav.myapplication.utils.Constants;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<MusicFile> mValues;
    Context mContext;
    ImageButton imgDownloadButton;
    private ClipListDB mDBHelper;
   // int downloadClickCount = 0;

    public MyItemRecyclerViewAdapter(List<MusicFile> items, Context context, ImageButton imgButton) {
        mValues = items;
        mContext = context;
        imgDownloadButton = imgButton;
        mDBHelper = new ClipListDB(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_clip_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final String fileName = mValues.get(position).getFileName();
        final String fileUrl = mValues.get(position).getFileUrl();
        holder.file_name.setText(fileName);
        holder.file_path.setText(fileUrl);

        mDBHelper = new ClipListDB(mContext);

        if(mDBHelper.isDownloaded(fileName) == 1){
            holder.img_btn_download.setImageResource(R.drawable.play);
        }else {
            holder.img_btn_download.setImageResource(R.drawable.download);
        }

       // holder.img_btn_download.setImageResource(R.drawable.download);

        holder.img_btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDBHelper.isDownloaded(fileName) == 1){
                    holder.img_btn_download.setImageResource(R.drawable.play);
                }else {
                    if(Constants.downloadClickCount< Constants.maxDownloadCount) {
                        Constants.downloadClickCount++;
                        Log.d("Download","Start +++++++++++++++ : "+Constants.downloadClickCount);
                        FragmentEvent.ClipDownloadFromListMessage adapterActivityMessageEvent =
                                new FragmentEvent.ClipDownloadFromListMessage(
                                        holder.mItem);
                        GlobalBus.getBus().post(adapterActivityMessageEvent);
                    }else{
                        Toast.makeText(mContext, "Wait!!! files are downloading..",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView file_name;
        public final TextView file_path;
        public final ImageButton img_btn_download;
        public MusicFile mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            file_name = (TextView) view.findViewById(R.id.file_name);
            file_path = (TextView) view.findViewById(R.id.file_path);
            img_btn_download = (ImageButton) view.findViewById(R.id.img_btn_download);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + file_name.getText() + "'";
        }
    }
}
