package com.luccas.buscaperto.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.model.offer.Offer;
import com.luccas.buscaperto.model.offer.OfferCatalog;
import com.luccas.buscaperto.viewholder.OfferViewHolder;

public class OfferAdapter extends RecyclerView.Adapter<OfferViewHolder> {
    private static OfferCatalog offerCatalog;
    private Context context;

    public OfferAdapter(OfferCatalog offerCatalog, Context context) {
        OfferAdapter.offerCatalog = offerCatalog;
        this.context = context;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new OfferViewHolder(inflater.inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offerCatalog.offers.get(position);
        holder.bindToPost(context, offer);
    }

    @Override
    public int getItemCount() {
        return offerCatalog.offers.size();
    }

    public static Offer getCurrentOffer(int position) {
        return offerCatalog.offers.get(position);
    }

}
