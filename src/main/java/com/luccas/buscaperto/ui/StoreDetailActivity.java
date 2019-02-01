package com.luccas.buscaperto.ui;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.adapter.MainAdapter;
import com.luccas.buscaperto.model.store.Store;
import com.luccas.buscaperto.ui.fragments.ProductListByStoreFragment;

public class StoreDetailActivity extends AppCompatActivity {
    public static final String CURRENT_STORE = "store";
    private String[] mPlaceDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Store store = (Store) getIntent().getSerializableExtra(CURRENT_STORE);

        setupCollapsingToolbar(store);
        initView();

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

    }

    public void setupCollapsingToolbar(Store store) {
        Typeface font = Typer.set(this).getFont(Font.ROBOTO_BLACK);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setCollapsedTitleTypeface(font);
        collapsingToolbar.setExpandedTitleTypeface(font);
        collapsingToolbar.setTitle(store.getName());
    }

    public void initView() {
        Resources resources = this.getResources();
        int position = getIntent().getIntExtra("position", 0);

        TextView placeLocation = findViewById(R.id.place_location);
        mPlaceDesc = resources.getStringArray(R.array.place_desc);
        placeLocation.setText(mPlaceDesc[position % mPlaceDesc.length]);

        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        Drawable imageView =  a.getDrawable(position);
        ImageView placePicture = findViewById(R.id.image);
        placePicture.setImageDrawable(imageView);

        a.recycle();
    }

    private void setupViewPager(ViewPager viewPager) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProductListByStoreFragment(), getString(R.string.related_products));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Runtime.getRuntime().gc();
        finish();
    }
}