package com.example.ashwin.firebaseyoutubeplaylistpagination;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by vihaan on 17/4/16.
 */

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final String TAG = VideosAdapter.class.getSimpleName();
    private ArrayList<Video> mVideos;

    protected ImageLoader mImageLoader = ImageLoader.getInstance();

    public interface VideoListener {
        public void loadMoreVideos();

        public void onVideoImageClicked(View view, int position);
    }

    private static VideoListener sVideoListener;


    public VideosAdapter(VideoListener videoListener, ArrayList<Video> videos) {
        sVideoListener = videoListener;
        mVideos = videos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        //view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
        //viewHolder = new VideosAdapter.VideoViewHolder(view);

        switch (viewType) {
            case VIEW_TYPES.Normal:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
                viewHolder = new VideoViewHolder(view);
                break;

            case VIEW_TYPES.Footer:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_progress, parent, false);
                viewHolder = new ProgressViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VideoViewHolder)
        {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            String videoImageUrl = mVideos.get(position).getImageurl();

            videoViewHolder.videoImageView.setImageDrawable(null);
            videoViewHolder.position = position;
            mImageLoader.displayImage(videoImageUrl, videoViewHolder.videoImageView);
            videoViewHolder.videoTextView.setText(mVideos.get(position).getTitle());
        }
        else if (holder instanceof ProgressViewHolder)
        {
            ProgressViewHolder progressViewHolder;
            progressViewHolder = (ProgressViewHolder) holder;
        }


        if (mMoreVidoes) {
            if (position >= getItemCount() - 5) {
                sVideoListener.loadMoreVideos();
            }
        }
    }

    @Override
    public int getItemCount() {
        /*if (mMoreVidoes) {
            return mVideos.size() + 1;
        }*/
        return mVideos.size();
    }

    private boolean mMoreVidoes = true;

    public void onNoMoreImages() {
        mMoreVidoes = false;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mVideos.size()) ? VIEW_TYPES.Normal : VIEW_TYPES.Footer;
    }

    private class VIEW_TYPES {
        public static final int Normal = 1;
        public static final int Footer = 2;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView videoImageView;
        public LinearLayout linearLayout;
        public TextView videoTextView;
        public int position;

        public VideoViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            linearLayout.setOnClickListener(this);
            videoImageView = (ImageView) itemView.findViewById(R.id.videoImageView);
            videoTextView = (TextView) itemView.findViewById(R.id.videoTextView);
        }

        @Override
        public void onClick(View v) {
            if (sVideoListener != null) {
                sVideoListener.onVideoImageClicked(v, position);
            }
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        private CircleProgressBar circleProgressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            circleProgressBar = (CircleProgressBar) itemView.findViewById(R.id.circleProgressBar);
        }
    }
}
