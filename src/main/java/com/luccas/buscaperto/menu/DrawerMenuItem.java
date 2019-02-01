package com.luccas.buscaperto.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.LoginActivity;
import com.google.firebase.auth.auth.R;
import com.luccas.buscaperto.services.LocationMonitoringService;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.drawer_item)
public class DrawerMenuItem {

    public static final int EDIT_PROFILE = 1;
    public static final int DRAWER_MENU_ITEM_REQUESTS = 2;
    public static final int MAX_DISTANCE = 3;
    public static final int SUPPORT = 4;
    public static final int ABOUT = 5;
    public static final int DRAWER_MENU_ITEM_SETTINGS = 6;
    public static final int TERMS = 7;
    public static final int LOGOUT = 8;

    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;

    public DrawerMenuItem(Context context, int menuPosition) {
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition){
            case EDIT_PROFILE:
                itemNameTxt.setText(R.string.edit);
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_edit_black_24dp));
                itemIcon.setColorFilter(mContext.getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
                break;
            case MAX_DISTANCE:
                itemNameTxt.setText(R.string.max_distance);
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_person_pin_circle_black_24dp));
                itemIcon.setColorFilter(mContext.getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
                break;
            case SUPPORT:
                itemNameTxt.setText(R.string.support);
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_forum_black_24dp));
                itemIcon.setColorFilter(mContext.getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
                break;
            case ABOUT:
                itemNameTxt.setText(R.string.abount);
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_help_outline_black_24dp));
                itemIcon.setColorFilter(mContext.getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                itemNameTxt.setText(R.string.options);
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_settings_20px));
                itemIcon.setColorFilter(mContext.getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
                break;
            case TERMS:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_book_24px));
                itemIcon.setColorFilter(mContext.getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
                itemNameTxt.setText(R.string.terms);
                break;
            case LOGOUT:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_exit_to_app_24px));
                itemIcon.setColorFilter(mContext.getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
                itemNameTxt.setText(R.string.logout);
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick(){
        switch (mMenuPosition){
            case EDIT_PROFILE:
                Toast.makeText(mContext, "Editar Conta", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onEditMenuSelected();
                break;
            case MAX_DISTANCE:
                Toast.makeText(mContext, "Distância Máxima", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onMaxDistanceMenuSelected();
                break;
            case SUPPORT:
                Toast.makeText(mContext, "Fale Conosco", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onSupportMenuSelected();
                break;
            case ABOUT:
                Toast.makeText(mContext, "Sobre", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onAboutMenuSelected();
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                Toast.makeText(mContext, "Opções", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onSettingsMenuSelected();
                break;
            case TERMS:
                Toast.makeText(mContext, "Termos de Uso", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onTermsMenuSelected();
                break;
            case LOGOUT:
                Toast.makeText(mContext, "Sessão encerrada", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.stopService(new Intent(mContext, LocationMonitoringService.class));
                AuthUI.getInstance().signOut(mContext);
                mContext.startActivity(intent);

                if(mCallBack != null)mCallBack.onLogoutMenuSelected();
                break;
        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack{
        void onEditMenuSelected();
        void onMaxDistanceMenuSelected();
        void onSupportMenuSelected();
        void onAboutMenuSelected();
        void onSettingsMenuSelected();
        void onTermsMenuSelected();
        void onLogoutMenuSelected();
    }
}