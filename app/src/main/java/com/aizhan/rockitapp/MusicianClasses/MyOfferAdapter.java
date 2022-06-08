package com.aizhan.rockitapp.MusicianClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aizhan.rockitapp.R;
import com.aizhan.rockitapp.SingleOfferActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyOfferAdapter  extends RecyclerView.Adapter<MyOfferAdapter.MyOfferViewHolder>{
    Context mCtx;
    List<MyOffers> offList;
    DatabaseReference musDB, offerDB;

    public MyOfferAdapter(Context mCtx, List<MyOffers> offList) {
        this.mCtx = mCtx;
        this.offList = offList;
    }

    @NonNull
    @Override
    public MyOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.musoffer_layout, parent, false);
        MyOfferViewHolder myOfferViewHolder = new MyOfferViewHolder(view);
        return myOfferViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOfferViewHolder holder, int position) {
        final MyOffers myOffers = offList.get(position);
        holder.offerName.setText(myOffers.getWho());
        holder.offerSalary.setText(myOffers.getSalary());

        if (myOffers.getType().equals("inbox")){
            if (myOffers.getStatus().equals("applied")){
                holder.acceptBtn.setVisibility(View.VISIBLE);
                holder.rejectBtn.setVisibility(View.VISIBLE);
                holder.offerName.setTextColor(Color.parseColor("#0F1F38"));
            }else if (myOffers.getStatus().equals("accepted")){
                holder.acceptBtn.setVisibility(View.INVISIBLE);
                holder.rejectBtn.setVisibility(View.VISIBLE);
                holder.offerName.setTextColor(Color.parseColor("#8EBA43"));
            }else if (myOffers.getStatus().equals("rejected")){
                holder.acceptBtn.setVisibility(View.VISIBLE);
                holder.acceptBtn.setVisibility(View.INVISIBLE);
                holder.offerName.setTextColor(Color.parseColor("#F55449"));
            }
        }else{//Outbox
            if (myOffers.getStatus().equals("applied")){
                holder.acceptBtn.setVisibility(View.INVISIBLE);
                holder.rejectBtn.setVisibility(View.VISIBLE);
                holder.offerName.setTextColor(Color.parseColor("#0F1F38"));
            }else if (myOffers.getStatus().equals("accepted")){
                holder.acceptBtn.setVisibility(View.INVISIBLE);
                holder.rejectBtn.setVisibility(View.VISIBLE);
                holder.offerName.setTextColor(Color.parseColor("#8EBA43"));
            }else if (myOffers.getStatus().equals("rejected")){
                holder.acceptBtn.setVisibility(View.INVISIBLE);
                holder.rejectBtn.setVisibility(View.INVISIBLE);
                holder.offerName.setTextColor(Color.parseColor("#F55449"));
            }
        }


        //SET ON CLICK LISTENER
        holder.offerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, SingleOfferActivity.class);
                intent.putExtra("offer_id", myOffers.getOfferid());
                mCtx.startActivity(intent);
            }
        });
        //ACCEPT THE OFFER
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(myOffers.getOfferid()).child("status").setValue("accepted");
                offerDB.child(myOffers.getOfferid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("accepted");
            }
        });
        //REJECT THE OFFER
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musDB.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(myOffers.getOfferid()).child("status").setValue("rejected");
                offerDB.child(myOffers.getOfferid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue("rejected");

            }
        });
    }

    @Override
    public int getItemCount() {
        return offList.size();
    }

    class MyOfferViewHolder extends RecyclerView.ViewHolder{
        TextView offerName, offerSalary;
        RelativeLayout offerLayout;
        Button acceptBtn, rejectBtn;


        public MyOfferViewHolder(View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.my_offerName);
            offerSalary = itemView.findViewById(R.id.my_offerSalary);
            offerLayout = itemView.findViewById(R.id.my_offer_single);
            acceptBtn = itemView.findViewById(R.id.accept_btn);
            rejectBtn = itemView.findViewById(R.id.reject_btn);
            musDB = FirebaseDatabase.getInstance().getReference("musoffers");
            offerDB = FirebaseDatabase.getInstance().getReference("offermusicians");
        }
    }
    public void refresh(List<MyOffers> offerlist){
        this.offList = offerlist;
        notifyDataSetChanged();
    }
}
