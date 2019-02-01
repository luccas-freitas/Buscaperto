package com.luccas.buscaperto.provider;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.auth.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luccas.buscaperto.adapter.StoreAdapter;
import com.luccas.buscaperto.model.ApiInterface;
import com.luccas.buscaperto.model.store.Coordinates;
import com.luccas.buscaperto.model.store.Store;
import com.luccas.buscaperto.model.store.StoreInterface;
import com.luccas.buscaperto.model.store.StoresCatalog;
import com.luccas.buscaperto.utils.NullOnEmptyConverterFactory;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoresProvider {
    private static DatabaseReference mDatabase;

    public static void build(Context context, RecyclerView recyclerView) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        StoreInterface storeInterface = retrofit.create(StoreInterface.class);
        Call<StoresCatalog> storesCatalogCall = storeInterface.storeCatalog(
                ApiInterface.API_KEY, ApiInterface.SOURCE_ID, ApiInterface.hasOffer);

        storesCatalogCall.enqueue(new Callback<StoresCatalog>() {
            public static final String TAG = "luccas";

            @Override
            public void onResponse(Call<StoresCatalog> call, Response<StoresCatalog> response) {
                if(!response.isSuccessful()) {
                    Log.i(TAG, "Erro: " + response.code());
                } else {
                    StoresCatalog storesCatalog = response.body();

                    populateFirebase(context, storesCatalog);

                    StoreAdapter adapter = new StoreAdapter(storesCatalog, recyclerView.getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<StoresCatalog> call, Throwable t) {
                Log.e(TAG, "Erro:" + t.getMessage());
            }
        });

    }

    public static void populateFirebase(Context context, StoresCatalog storesCatalog) {
        String storeId;
        Resources resources = context.getResources();
        String[] coordinates = resources.getStringArray(R.array.coordinates);
        String[] adresses = resources.getStringArray(R.array.place_desc);
        int i = 0, j = 0;
        for(Store store : storesCatalog.stores){
            storeId = Integer.toString(store.getId());
            store.setCoordinates(new Coordinates(coordinates[i], coordinates[i+1], coordinates[i+2]));
            store.setAddress(adresses[j]);

            Map<String, Object> postValues = store.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/stores/" + storeId, postValues);

            mDatabase.updateChildren(childUpdates);
            i+=3;
            j++;
        }
    }
}
