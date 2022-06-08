package com.aizhan.rockitapp.VideoClasses;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.aizhan.rockitapp.R;

import java.util.List;

public class VideoAdapter  extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    Context mCtx;
    List<MusVideo> videoList;

    public VideoAdapter(Context mCtx, List<MusVideo> videoList) {
        this.mCtx = mCtx;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.video_layout, parent, false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder holder, int position) {
        final MusVideo musVideo = videoList.get(position);
        holder.videoname.setText(musVideo.getVideo_desc());
        holder.videovv.setVideoURI(Uri.parse(musVideo.getVideo_uri()));
        holder.videovv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        MediaController mc = new MediaController(mCtx);
                        holder.videovv.setMediaController(mc);
                        mc.setAnchorView(holder.videovv);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView videoname;
        VideoView videovv;
        RelativeLayout videolayout;
        MediaController mc;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoname = itemView.findViewById(R.id.video_tv);
            videovv = itemView.findViewById(R.id.video_vv);
            videolayout = itemView.findViewById(R.id.video_single);


        }
    }
}
