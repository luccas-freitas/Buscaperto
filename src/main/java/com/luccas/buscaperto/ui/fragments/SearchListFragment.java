package com.luccas.buscaperto.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.provider.SearchProvider;

public class SearchListFragment extends Fragment {
    private String query;

    public SearchListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        SearchProvider.build(getContext(), recyclerView, query);

        return recyclerView;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
