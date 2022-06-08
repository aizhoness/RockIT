package com.aizhan.rockitapp.ClientClasses;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.aizhan.rockitapp.R;
import com.aizhan.rockitapp.SingleOfferActivity;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder>{
    Context mCtx;
    List<OfferInfo> offerList;

    public OffersAdapter(Context mCtx, List<OfferInfo> offerList) {
        this.mCtx = mCtx;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.offer_layout, parent, false);
        OfferViewHolder offerViewHolder = new OfferViewHolder(view);
        return offerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        final OfferInfo offerInfo = offerList.get(position);
        holder.offerName.setText(offerInfo.getWho());
        holder.offerSalary.setText(offerInfo.getSalary());

        //SET ON CLICK LISTENER
        holder.offerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, SingleOfferActivity.class);
                intent.putExtra("offer_id", offerInfo.getOfferid());
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }


    class OfferViewHolder extends RecyclerView.ViewHolder{
        TextView offerName, offerSalary;
        RelativeLayout offerLayout;

        public OfferViewHolder(View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.offerName);
            offerSalary = itemView.findViewById(R.id.offerSalary);
            offerLayout = itemView.findViewById(R.id.offer_single);
        }
    }
}
