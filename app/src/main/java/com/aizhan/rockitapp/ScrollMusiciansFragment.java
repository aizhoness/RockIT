package com.aizhan.rockitapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aizhan.rockitapp.MusicianClasses.Musician;
import com.aizhan.rockitapp.MusicianClasses.MusicianMain;
import com.aizhan.rockitapp.MusicianClasses.MusiciansAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollMusiciansFragment extends Fragment {
    RecyclerView recyclerView;
    MusiciansAdapter adapter;
    List<Musician> musicianList;


    public ScrollMusiciansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scroll_musicians, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.musiciansrv);


        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        musicianList = new ArrayList<>();
        DatabaseReference dbMain = FirebaseDatabase.getInstance().getReference("musmain");
        dbMain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot musSnapshot : dataSnapshot.getChildren()){
                        MusicianMain musicianMain = musSnapshot.getValue(MusicianMain.class);
                        Musician musician = new Musician(musSnapshot.getKey(), musicianMain.getImageUri(), musicianMain.getNameSurname(), musicianMain.getSkills(), musicianMain.getRating());
                        musicianList.add(musician);
                    }
                    adapter = new MusiciansAdapter(getActivity(), musicianList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
