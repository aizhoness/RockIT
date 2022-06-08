package com.aizhan.rockitapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aizhan.rockitapp.ClientClasses.OfferInfo;
import com.aizhan.rockitapp.ClientClasses.OffersAdapter;
import com.aizhan.rockitapp.MusicianClasses.MusOffer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends Fragment {
    RecyclerView recyclerView;
    OffersAdapter adapter;
    List<OfferInfo> offerList;
    public String myValue;
    public String mState = "";



    public OffersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Confirm this fragment has menu items.
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        myValue = bundle.getString("message");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the fragment menu items.
        inflater.inflate(R.menu.add_offer_menu, menu);
        if (mState.equals("mus"))
        {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.menu_add_offer)
        {
            startActivity(new Intent(getActivity(), CreateOfferActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.offersrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        offerList = new ArrayList<>();
        Log.i("MyActivity", "myValue: "+myValue);

        if (myValue.equals("all")){
            mState = "mus";
            DatabaseReference dbMain = FirebaseDatabase.getInstance().getReference("offermain");
            dbMain.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()){
                            OfferInfo offerInfo = offerSnapshot.getValue(OfferInfo.class);
                            offerList.add(offerInfo);
                        }
                        adapter = new OffersAdapter(getActivity(), offerList);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            getActivity().invalidateOptionsMenu();

        }else if (myValue.equals("myclient")){
            mState = "client";
            DatabaseReference dbMain = FirebaseDatabase.getInstance().getReference("clientoffers");
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dbMain.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()){
                            OfferInfo offerInfo = offerSnapshot.getValue(OfferInfo.class);
                            offerList.add(offerInfo);
                        }
                        adapter = new OffersAdapter(getActivity(), offerList);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            Toast.makeText(getActivity(), "myValue is empty", Toast.LENGTH_SHORT).show();
        }



    }
}
