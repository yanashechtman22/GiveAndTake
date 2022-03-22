package com.example.giveandtake;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giveandtake.model.AuthenticationModel;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giveandtake.databinding.ActivityMainBinding;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        initializeNavHeader();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_postAdd, R.id.user_profile_page)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static void initializeNavHeader() {
        View header = navigationView.getHeaderView(0);
        UserInfo userInfo = AuthenticationModel.instance.getUserInfo();
        if (userInfo == null) return;
        TextView userName = header.findViewById(R.id.drawerUsernameTV);
        String userNameText = userInfo.getDisplayName();
        userName.setText(userNameText);
        TextView email = header.findViewById(R.id.drawerEmailTV);
        String emailText = userInfo.getEmail();
        email.setText(emailText);

        ImageView userImage = header.findViewById(R.id.drawerImv);
        Uri userPhotoUri = userInfo.getPhotoUrl();
        if (userPhotoUri != null) {
            Picasso.get().load(userPhotoUri).into(userImage);
        } else {
            Picasso.get().load(R.mipmap.ic_launcher_round).into(userImage);        }
    }
}

