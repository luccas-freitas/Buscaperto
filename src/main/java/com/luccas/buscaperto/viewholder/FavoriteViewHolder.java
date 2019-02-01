package com.luccas.buscaperto.viewholder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.auth.R;
import com.google.firebase.database.FirebaseDatabase;
import com.luccas.buscaperto.model.offer.Offer;
import com.luccas.buscaperto.ui.ProductDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luccas.buscaperto.adapter.OfferAdapter.getCurrentOffer;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_avatar) public ImageView imageView;
    @BindView(R.id.list_title)  public TextView name;
    @BindView(R.id.list_desc)   public TextView description;

    public FavoriteViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProductDetailActivity.class);

            Offer offer = getCurrentOffer(getAdapterPosition());
            intent.putExtra(ProductDetailActivity.CURRENT_OFFER, offer);

            context.startActivity(intent);
        });

        itemView.setOnLongClickListener(view -> {
            Offer offer = getCurrentOffer(getAdapterPosition());
            String offerId = Integer.toString(offer.getId());

            new AlertDialog.Builder(view.getContext())
                    .setTitle("Deseja remover este favorito?")
                    .setMessage("Não perca as esperanças! Ele pode estar mais perto do que você pensa.")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child("user-favorites")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(offerId)
                                .removeValue();

                        Snackbar.make(view, "Favorito removido.", Snackbar.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Não", (dialog, which) -> Log.d("FAVORITES ===>", "Aborting mission..."))
                    .show();

            return false;
        });
    }

    public void bindToPost(Context context, Offer offer) {
        Glide.with(context)
                .load(offer.getThumbnail())
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        name.setText(offer.getProduct().getShortName());
        description.setText(offer.getName());
    }

}
