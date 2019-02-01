package com.luccas.buscaperto.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.model.offer.Offer;
import com.luccas.buscaperto.ui.ProductDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luccas.buscaperto.adapter.OfferAdapter.getCurrentOffer;

public class OfferViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tile_picture)    public ImageView picture;
    @BindView(R.id.tile_title)      public TextView name;
    @BindView(R.id.progress_bar)    public ProgressBar progressBar;

    public OfferViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProductDetailActivity.class);

            Offer offer = getCurrentOffer(getAdapterPosition());
            intent.putExtra(ProductDetailActivity.CURRENT_OFFER, offer);

            context.startActivity(intent);
        });
    }

    public void bindToPost(Context context, Offer offer) {
        String url;
        if (offer!= null
                && offer.getProduct()!= null
                && offer.getProduct().getThumbnail()!= null
                && offer.getProduct().getThumbnail().getUrl() != null)
            url = offer.getProduct().getThumbnail().getUrl();
        else
            url = offer.getThumbnail();

        name.setText(offer.getProduct().getShortName());

        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(picture);
    }
}
