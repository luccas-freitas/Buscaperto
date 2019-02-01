package com.luccas.buscaperto.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.adapter.OfferAdapter;
import com.luccas.buscaperto.model.ApiInterface;
import com.luccas.buscaperto.model.offer.OfferCatalog;
import com.luccas.buscaperto.model.offer.OfferInterface;
import com.luccas.buscaperto.model.store.Store;
import com.luccas.buscaperto.provider.OffersByStoreProvider;
import com.luccas.buscaperto.utils.NullOnEmptyConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.luccas.buscaperto.ui.StoreDetailActivity.CURRENT_STORE;

public class ProductListByStoreFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        Store store = (Store) getActivity().getIntent().getSerializableExtra(CURRENT_STORE);

        OffersByStoreProvider.build(getContext(), recyclerView, store);

        return recyclerView;
    }
}
