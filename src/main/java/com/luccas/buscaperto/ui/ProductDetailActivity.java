package com.luccas.buscaperto.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Path;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.firebase.auth.BaseActivity;
import com.google.firebase.auth.auth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.luccas.buscaperto.model.offer.Offer;
import com.luccas.buscaperto.model.store.Coordinates;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends BaseActivity {
    private DatabaseReference mDatabase;

    public static final String CURRENT_OFFER = "offer";
    private boolean isActive = false;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.place_detail) TextView placeDetail;
    @BindView(R.id.place_location) TextView placeLocation;
    @BindView(R.id.image) ImageView placePicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ButterKnife.bind(this);

        Offer offer = (Offer) getIntent().getSerializableExtra(CURRENT_OFFER);

        initToolbar();
        initView(offer);
        initFab(offer);
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

    public void initToolbar() {
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initView(Offer offer) {
        Typeface font = Typer.set(this).getFont(Font.ROBOTO_CONDENSED_BOLD);

        productName.setAllCaps(true);
        productName.setTypeface(font);
        productName.setText(offer.getProduct().getShortName());

        placeDetail.setText(offer.getName());

        TextView productPrice = findViewById(R.id.product_price);
        DecimalFormat format = new DecimalFormat("0.00");

        if(offer.getProduct().getPriceMin() != null)
            productPrice.setText("R$ " + format.format(offer.getProduct().getPriceMin()).replace(".", ","));
        else
            productPrice.setText(R.string.unavailable);

        placeLocation.setText(offer.getStore().getName());

        String url;
        if (offer!= null
                && offer.getProduct()!= null
                && offer.getProduct().getThumbnail()!= null
                && offer.getProduct().getThumbnail().getUrl() != null)
            url = offer.getProduct().getThumbnail().getUrl();
        else
            url = offer.getThumbnail();

        Glide.with(this)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(placePicture);

        placePicture.setOnClickListener(view -> {
            Intent fullScreen = new Intent(this, FullScreenImageActivity.class);
            fullScreen.setData(Uri.parse(url));
            startActivity(fullScreen);
        });
    }

    public void initFab(Offer offer) {
        @ColorInt final int colorActive = ContextCompat.getColor(this, R.color.colorAccent);
        @ColorInt final int colorPassive = ContextCompat.getColor(this, R.color.white);

        final float from = 1.0f;
        final float to = 1.3f;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, View.SCALE_X, from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, View.SCALE_Y,  from, to);
        ObjectAnimator translationZ = ObjectAnimator.ofFloat(fab, View.TRANSLATION_Z, from, to);

        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(scaleX, scaleY, translationZ);
        set1.setDuration(100);
        set1.setInterpolator(new AccelerateInterpolator());

        set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setImageResource(isActive ? R.drawable.heart_passive : R.drawable.heart_active);
                fab.setBackgroundTintList(ColorStateList.valueOf(isActive ? colorPassive : colorActive));
                isActive = !isActive;
            }
        });

        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(fab, View.SCALE_X, to, from);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(fab, View.SCALE_Y, to, from);
        ObjectAnimator translationZBack = ObjectAnimator.ofFloat(fab, View.TRANSLATION_Z, to, from);

        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(0.5f, 1.3f);
        path.lineTo(0.75f, 0.8f);
        path.lineTo(1.0f, 1.0f);
        PathInterpolator pathInterpolator = new PathInterpolator(path);

        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(scaleXBack, scaleYBack, translationZBack);
        set2.setDuration(300);
        set2.setInterpolator(pathInterpolator);

        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(set1, set2);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setClickable(true);
                saveFavorite(offer);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                fab.setClickable(false);
            }
        });

        fab.setOnClickListener(v -> set.start());
    }

    public void saveFavorite(Offer offer) {
        final String storeId = Integer.toString(offer.getStore().getId());
        final String userId = getUid();

        mDatabase.child("stores").child(storeId).child("coordinates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Coordinates coordinates = dataSnapshot.getValue(Coordinates.class);

                offer.setDateCreated(ServerValue.TIMESTAMP);
                offer.getStore().setCoordinates(coordinates);

                Map<String, Object> postValues = offer.toMap();
                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put("/user-favorites/" + userId + "/" + offer.getId(), postValues);

                mDatabase.updateChildren(childUpdates);

                Snackbar.make(fab, "Salvo em seus Favoritos!", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
