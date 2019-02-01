package com.luccas.buscaperto.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.auth.BuildConfig;
import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.adapter.MainAdapter;
import com.luccas.buscaperto.menu.DrawerHeader;
import com.luccas.buscaperto.menu.DrawerMenuItem;
import com.luccas.buscaperto.services.LocationManager;
import com.luccas.buscaperto.ui.fragments.FavoriteListFragment;
import com.luccas.buscaperto.ui.fragments.ProductListFragment;
import com.luccas.buscaperto.ui.fragments.StoreListFragment;
import com.mindorks.placeholderview.PlaceHolderView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final int TAB_FEATURED = 1;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private LocationManager locationManager;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.drawer) DrawerLayout mDrawerLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.drawerView) PlaceHolderView mDrawerView;

    private DrawerHeader drawerHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        drawerHeader = new DrawerHeader(this);
        setupDrawer();
        setupFab(fab);

        viewPager.setCurrentItem(TAB_FEATURED);
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager = new LocationManager(this);
        locationManager.startStep1();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                locationManager.startStep3();
            else
                locationManager.showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
        }
    }

    private void setupFab(FloatingActionButton fab) {
        fab.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));
    }

    private void setupViewPager(ViewPager viewPager) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavoriteListFragment(), getString(R.string.tab_favorite));
        adapter.addFragment(new ProductListFragment(), getString(R.string.tab_featured));
        adapter.addFragment(new StoreListFragment(), getString(R.string.tab_affliates));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private void setupDrawer(){
        mDrawerView
                .addView(new DrawerHeader(this))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.EDIT_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_REQUESTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.SUPPORT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.MAX_DISTANCE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.ABOUT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.TERMS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.LOGOUT));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

}
