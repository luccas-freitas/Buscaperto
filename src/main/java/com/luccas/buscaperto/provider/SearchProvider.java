package com.luccas.buscaperto.provider;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.adapter.OfferAdapter;
import com.luccas.buscaperto.model.ApiInterface;
import com.luccas.buscaperto.model.offer.Offer;
import com.luccas.buscaperto.model.offer.OfferCatalog;
import com.luccas.buscaperto.model.offer.OfferInterface;
import com.luccas.buscaperto.utils.NullOnEmptyConverterFactory;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchProvider {
    public static void build(Context context, RecyclerView recyclerView, String keyword) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        OfferInterface offerInterface = retrofit.create(OfferInterface.class);

        Call<OfferCatalog> offerCatalogCall = offerInterface
                .searchCatalog(ApiInterface.API_KEY, ApiInterface.SOURCE_ID, keyword);

        offerCatalogCall.enqueue(new Callback<OfferCatalog>() {
            @Override
            public void onResponse(Call<OfferCatalog> call, Response<OfferCatalog> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Error: " + response.code());
                } else {
                    OfferCatalog offerCatalog = response.body();
                    OfferAdapter adapter = new OfferAdapter(offerCatalog, recyclerView.getContext());

                    recyclerView.setAdapter(adapter);
                    int tilePadding = context.getResources().getDimensionPixelSize(R.dimen.tile_padding);
                    recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
                    recyclerView.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), 2));
                }
            }

            @Override
            public void onFailure(Call<OfferCatalog> call, Throwable t) {
                System.out.println("Error:" + t.getMessage());
            }
        });

    }
}
