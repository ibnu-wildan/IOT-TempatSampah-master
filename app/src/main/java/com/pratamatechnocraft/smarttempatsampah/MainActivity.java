package com.pratamatechnocraft.smarttempatsampah;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pratamatechnocraft.smarttempatsampah.Fragment.HistoriFragment;
import com.pratamatechnocraft.smarttempatsampah.Fragment.HomeFragment;
import com.pratamatechnocraft.smarttempatsampah.Service.SessionManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Fragment fragment = null;
    int fragmentLast = R.id.nav_home;
    NavigationView navigationView;
    SessionManager sessionManager;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("TAG", "onCreate: "+firebaseAuth.getUid());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        sessionManager = new SessionManager( this );
        sessionManager.checkLogin();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem( R.id.nav_home );
        displaySelectedScreen(R.id.nav_home);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        TextView txtEmailUserLogin = headerView.findViewById(R.id.txtEmailUserLogin);
        txtEmailUserLogin.setText(firebaseUser.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
            sessionManager.checkLogin();
        } else {
            MenuItem item = navigationView.getCheckedItem();
            if (fragmentLast!=R.id.nav_home) {
                super.onBackPressed();
                sessionManager.checkLogin();
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen( item.getItemId() );
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        int id = itemId;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if(id == R.id.nav_history){
            fragment = new HistoriFragment();
        } else if (id == R.id.nav_logout) {
            sessionManager.logout();
        }

        if (fragment != null) {
            if (fragmentLast!=id){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.screen_area, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }else if(id==R.id.nav_home){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.screen_area, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }

        fragmentLast = id;

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
