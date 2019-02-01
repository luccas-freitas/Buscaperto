package com.luccas.buscaperto.menu;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.auth.R;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader {
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    @View(R.id.profileImageView)
    private ImageView profileImage;

    @View(R.id.nameTxt)
    private TextView nameTxt;

    @View(R.id.emailTxt)
    private TextView emailTxt;

    @Resolve
    private void onResolved() {
        if(currentUser.getPhotoUrl() != null) {
            if(currentUser.getPhotoUrl().toString().contains("graph.facebook"))
                Glide.with(context).load(currentUser.getPhotoUrl() + "?width=1000&height=1000").apply(RequestOptions.circleCropTransform()).into(profileImage);
            else
                Glide.with(context).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profileImage);
        }
        nameTxt.setText(currentUser.getDisplayName());
        emailTxt.setText(currentUser.getEmail());
    }

    public DrawerHeader(Context context) {
        this.context = context;
    }
}