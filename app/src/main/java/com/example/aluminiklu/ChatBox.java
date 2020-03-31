package com.example.aluminiklu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.aluminiklu.Fragments.ChatsFragment;
import com.example.aluminiklu.Fragments.ProfileFragment;
import com.example.aluminiklu.Fragments.UsersFragment;
import com.example.aluminiklu.Model.user;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBox extends AppCompatActivity {
CircleImageView profile;

TextView textView;

TextView username;
FirebaseUser firebaseUser;
DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
Intent i=new Intent(ChatBox.this,Login.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        Toolbar toolbar=findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setTitle("");



        profile=findViewById(R.id.profile_image);
        username=findViewById(R.id.user);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        databaseReference= FirebaseDatabase.getInstance().getReference("Users1").child(firebaseUser.getUid());
       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   // String na1=dataSnapshot.child(firebaseUser.getUid()).child("FullName").getValue(String.class);
              user user=dataSnapshot.getValue(com.example.aluminiklu.Model.user.class);
              username.setText(user.getUsername());
           try {
               if (user.getImageUrl().equals("default")) {

                   profile.setImageResource(R.mipmap.ic_launcher);
               } else {
                   Picasso.with(getApplicationContext()).load(user.getImageUrl()).into(profile);
               }
           }
           catch (Exception e){

           }
              //  if(user.getFullName()!=null)
              //  {    Toast.makeText(ChatBox.this,user.getFullName(),Toast.LENGTH_LONG).show();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     username.setText("Hemanth");

        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPagerAdapter.addFragment(new UsersFragment(),"Users");
        viewPagerAdapter.addFragment(new ProfileFragment(),"Profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatBox.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
              //  finish();
                return true;
        }
        return false;
    }
    class  ViewPagerAdapter extends FragmentPagerAdapter{
private ArrayList<Fragment> fragments;
private ArrayList<String> titles;
ViewPagerAdapter(FragmentManager fm)
{
    super(fm);
    this.fragments=new ArrayList<>();
    this.titles=new ArrayList<>();
}
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
    fragments.add(fragment);
    titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    private void  status(String status)
    {
        databaseReference=FirebaseDatabase.getInstance().getReference("Users1").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
