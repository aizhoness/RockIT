package com.aizhan.rockitapp.MusicianClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aizhan.rockitapp.R;
import com.aizhan.rockitapp.SingleMusicianActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusiciansAdapter extends RecyclerView.Adapter<MusiciansAdapter.MusicianViewHolder> {

    Context mCtx;
    List<Musician> musicianList;

    public MusiciansAdapter(Context mCtx, List<Musician> musicianList) {
        this.mCtx = mCtx;
        this.musicianList = musicianList;
    }

    @NonNull
    @Override
    public MusicianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.musician_layout, parent, false);

        MusicianViewHolder musicianViewHolder = new MusicianViewHolder(view);
        return musicianViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MusicianViewHolder holder, int position) {
        final Musician musician = musicianList.get(position);
        holder.textViewName.setText(musician.getName_surname());
        holder.textViewRate.setText(String.valueOf(musician.getRating()));
        String[] skills_arr = musician.getSkills().split("%");
        String skills = "";
        for (int i = 0; i < skills_arr.length; i++) {
            if (skills==""){
                skills = skills_arr[i];
            }else{
                skills = skills + ", "+skills_arr[i];
            }
        }
        holder.textViewSkills.setText(skills);
        if (!musician.getImage_url().equals("-")){
            Picasso.with(mCtx)
                    .load(musician.getImage_url())
                    .fit()
                    .centerCrop()
                    .into(holder.imageViewPhoto);
        }


        //Set on click to every musician
        holder.musicianLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, SingleMusicianActivity.class);
                intent.putExtra("mus_id", musician.getMus_id());
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musicianList.size();
    }

    class MusicianViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageViewPhoto;
        TextView textViewName, textViewRate, textViewSkills;
        RelativeLayout musicianLayout;

        public MusicianViewHolder(View itemView) {
            super(itemView);

            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRate = itemView.findViewById(R.id.textViewRate);
            textViewSkills = itemView.findViewById(R.id.textViewSkills);
            musicianLayout = itemView.findViewById(R.id.musician_single);

        }
    }
}
