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
import android.widget.Button;
import android.widget.Toast;

import com.aizhan.rockitapp.ClientClasses.OfferInfo;
import com.aizhan.rockitapp.ClientClasses.OffersAdapter;
import com.aizhan.rockitapp.MusicianClasses.MusOffer;
import com.aizhan.rockitapp.MusicianClasses.MyOfferAdapter;
import com.aizhan.rockitapp.MusicianClasses.MyOffers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusOffersFragment extends Fragment {
    RecyclerView recyclerView;
    MyOfferAdapter adapter;
    List<MyOffers> offerList;
    Button inboxbtn, outboxbtn;
    String oftype = "inbox";

    public MusOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mus_offers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.myoffersrv);
        inboxbtn = (Button) view.findViewById(R.id.inbox_btn);
        outboxbtn = (Button) view.findViewById(R.id.outbox_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        offerList = new ArrayList<>();
        adapter = new MyOfferAdapter(getActivity(), offerList);
        recyclerView.setAdapter(adapter);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("musoffers").child(userId).addValueEventListener(valueEventListener);

        inboxbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oftype = "inbox";
                inboxbtn.setBackgroundResource(R.drawable.box_frame_bold);
                outboxbtn.setBackgroundResource(R.drawable.box_frame);
                FirebaseDatabase.getInstance().getReference("musoffers").child(userId).addValueEventListener(valueEventListener);            }
        });
        outboxbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oftype = "outbox";
                outboxbtn.setBackgroundResource(R.drawable.box_frame_bold);
                inboxbtn.setBackgroundResource(R.drawable.box_frame);
                FirebaseDatabase.getInstance().getReference("musoffers").child(userId).addValueEventListener(valueEventListener);            }
        });
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                offerList.clear();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final MusOffer musOffer = snapshot.getValue(MusOffer.class);
                    final String offerid = musOffer.getOfferid();
                    Log.i("musoffer: ", snapshot.toString());
                    Log.i("offerid: ", offerid);
                    Log.i("oftype: ", oftype);
                    Log.i("gettype: ", musOffer.getType());
                    if(musOffer.getType().equals(oftype)){

                        FirebaseDatabase.getInstance().getReference("offermain").child(offerid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    //for (DataSnapshot ofsnapshot : dataSnapshot.getChildren()){
                                    OfferInfo offerInfo = dataSnapshot.getValue(OfferInfo.class);
                                    Log.i("offerinfo: ", dataSnapshot.toString());
                                    Log.i("musofferagain: ", snapshot.toString());
                                    MyOffers myOffers = new MyOffers(offerInfo.getOfferid(), offerInfo.getWho(), offerInfo.getWhen(), offerInfo.getWhere(), offerInfo.getSalary(), offerInfo.getRequirements(), offerInfo.getAbout(), offerInfo.getOffertype(), musOffer.getStatus(), musOffer.getType());
                                    offerList.add(myOffers);
                                    Log.i("LENGTH of offerlist: ", String.valueOf(offerList.size()));
                                    //}
                                }else{
                                    Toast.makeText(getActivity(), "There is no such offer", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                adapter.refresh(offerList);
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
