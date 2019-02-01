package com.luccas.buscaperto.provider;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.adapter.OfferAdapter;
import com.luccas.buscaperto.model.ApiInterface;
import com.luccas.buscaperto.model.offer.OfferCatalog;
import com.luccas.buscaperto.model.offer.OfferInterface;
import com.luccas.buscaperto.utils.NullOnEmptyConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OffersProvider {
    public static void build(Context context, RecyclerView recyclerView) {
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

        //Call<OfferCatalog> offerCatalogCall = offerInterface.offerCatalog(ApiInterface.API_KEY, ApiInterface.SOURCE_ID, ApiInterface.SIZE);
        Call<OfferCatalog> offerCatalogCall = offerInterface
                .storeCatalog(ApiInterface.API_KEY,
                        Integer.toString(5766),
                        ApiInterface.SOURCE_ID,
                        ApiInterface.SIZE);

        offerCatalogCall.enqueue(new Callback<OfferCatalog>() {
            @Override
            public void onResponse(Call<OfferCatalog> call, Response<OfferCatalog> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Error: " + response.code());
                } else {
                    OfferCatalog offerCatalog = response.body();
                    OfferAdapter adapter = new OfferAdapter(offerCatalog, recyclerView.getContext());

                    int tilePadding = context.getResources().getDimensionPixelSize(R.dimen.tile_padding);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
                    recyclerView.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), 2));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<OfferCatalog> call, Throwable t) {
                System.out.println("Error:" + t.getMessage());
            }
        });
    }
}
