package com.luccas.buscaperto.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.model.store.Store;
import com.luccas.buscaperto.model.store.StoresCatalog;
import com.luccas.buscaperto.viewholder.StoreViewHolder;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {
    private Drawable[] imageView;

    private static StoresCatalog storesCatalog;
    private Context context;

    public StoreAdapter(StoresCatalog storesCatalog, Context context) {
        StoreAdapter.storesCatalog = storesCatalog;
        this.context = context;

        Resources resources = context.getResources();

        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        imageView = new Drawable[a.length()];
        for (int i = 0; i < imageView.length; i++) {
            imageView[i] = a.getDrawable(i);
        }
        a.recycle();
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new StoreViewHolder(inflater.inflate(R.layout.item_store, parent, false));
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        final Store store = storesCatalog.stores.get(position);

        holder.bindToPost(store, position, imageView);
    }

    @Override
    public int getItemCount() {
        return storesCatalog.stores.size();
    }

    public static Store getCurrentStore(int position) {
        return storesCatalog.stores.get(position);
    }


}