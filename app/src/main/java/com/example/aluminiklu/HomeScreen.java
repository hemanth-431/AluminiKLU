package com.example.aluminiklu;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.aluminiklu.Fragments.ProfileFragment;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
SNavigationDrawer sNavigationDrawer;
Class fragmentClass;
    int color1=0;
public static Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        sNavigationDrawer=findViewById(R.id.navigatorDrawer);
List<MenuItem> menuItems=new ArrayList<>();

        menuItems.add(new com.shrikanthravi.customnavigationdrawer2.data.MenuItem("Menu",R.drawable.klulogo));
        menuItems.add(new com.shrikanthravi.customnavigationdrawer2.data.MenuItem("Chat Box", R.mipmap.ic_launcher));
        menuItems.add(new com.shrikanthravi.customnavigationdrawer2.data.MenuItem("Profile", R.mipmap.ic_launcher));
        menuItems.add(new com.shrikanthravi.customnavigationdrawer2.data.MenuItem("Add new...", R.mipmap.ic_launcher));
        menuItems.add(new com.shrikanthravi.customnavigationdrawer2.data.MenuItem("Events", R.mipmap.ic_launcher));
        menuItems.add(new com.shrikanthravi.customnavigationdrawer2.data.MenuItem("Logout", R.mipmap.ic_launcher));


sNavigationDrawer.setMenuItemList(menuItems);

fragmentClass= ImagesActivity.class;

        try {
            fragment=(Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if(fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).replace(R.id.frameLayout,fragment).commit();

        }

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position "+position);

                switch (position){
                    case 0:{

                        fragmentClass = ImagesActivity.class;
                        break;
                    }
                    case 1:{

                        fragmentClass = ChatBox.class;
                        break;
                    }
                    case 2:{

                        fragmentClass = ProfileFragment.class;
                        break;
                    }
                    case 3:{

                        fragmentClass = home.class;
                        break;
                    }
                    case 4:{

                        fragmentClass = eventRecycler.class;
                        break;
                    }
                    case 5:{

                        Toast.makeText(HomeScreen.this,"LogOut",Toast.LENGTH_LONG).show();
                        break;
                    }

                }
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening(){

                    }

                    @Override
                    public void onDrawerClosing(){
                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State "+newState);
                    }
                });
            }
        });

    }
}
