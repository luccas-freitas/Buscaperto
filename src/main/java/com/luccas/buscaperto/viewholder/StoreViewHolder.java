package com.luccas.buscaperto.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.model.store.Store;
import com.luccas.buscaperto.ui.StoreDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luccas.buscaperto.adapter.StoreAdapter.getCurrentStore;

public class StoreViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.card_image)  public ImageView picture;
    @BindView(R.id.card_title)  public TextView name;
    @BindView(R.id.card_text)   public TextView description;

    public StoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, StoreDetailActivity.class);

            Store store = getCurrentStore(getAdapterPosition());
            intent.putExtra(StoreDetailActivity.CURRENT_STORE, store);
            intent.putExtra("position", getAdapterPosition());

            context.startActivity(intent);
        });

        Button button = itemView.findViewById(R.id.action_button);
        button.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, StoreDetailActivity.class);

            Store store = getCurrentStore(getAdapterPosition());
            intent.putExtra(StoreDetailActivity.CURRENT_STORE, store);
            intent.putExtra("position", getAdapterPosition());

            context.startActivity(intent);
        });

        ImageButton favoriteImageButton = itemView.findViewById(R.id.location_button);
        favoriteImageButton.setOnClickListener(v -> Snackbar.make(v, "Abrindo Google Maps",
                Snackbar.LENGTH_LONG).show());

        ImageButton shareImageButton = itemView.findViewById(R.id.share_button);
        shareImageButton.setOnClickListener(v -> Snackbar.make(v, "Compartilhar Loja",
                Snackbar.LENGTH_LONG).show());
    }

    public void bindToPost(Store store, int position, Drawable[] imageView) {
        name.setText(store.getName());
        description.setText(store.getAddress());
        picture.setImageDrawable(imageView[position % imageView.length]);
    }
}