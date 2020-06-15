package com.example.aluminiklu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.aluminiklu.Fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class Side_drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_drawer);
Toolbar toolbar=findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
  drawer.addDrawerListener(toggle);
  toggle.syncState();
if(savedInstanceState == null) {
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImagesActivity()).commit();
    navigationView.setCheckedItem(R.id.home123);
}
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.home123:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ImagesActivity()).commit();
                break;
            case R.id.Addnew:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home()).commit();
                break;
            case R.id.events:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new eventRecycler()).commit();
                break;
            case R.id.Chatbox:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatBox()).commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                break;
            case R.id.Logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}