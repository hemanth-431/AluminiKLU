package com.example.aluminiklu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.aluminiklu.Model.chat;
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

public class ChatBox extends Fragment {
CircleImageView profile;

TextView textView;

TextView username;
FirebaseUser firebaseUser;
DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
Intent i=new Intent(getActivity(),Login.class);
            startActivity(i);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_chat_box,container,false);


        Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");



        profile=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.user);
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
                   Picasso.with(getActivity().getApplicationContext()).load(user.getImageUrl()).into(profile);
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

        final TabLayout tabLayout=view.findViewById(R.id.tab_layout);
       final ViewPager viewPager=view.findViewById(R.id.viewpager);

        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         try{       ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getActivity().getSupportFragmentManager());
                int unread=0;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    chat chat=dataSnapshot1.getValue(com.example.aluminiklu.Model.chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }
                if(unread == 0)
                {
                    viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
                }
                else {
                    viewPagerAdapter.addFragment(new ChatsFragment(),"("+unread+") Chats");
                }

                viewPagerAdapter.addFragment(new UsersFragment(),"Users");
                viewPagerAdapter.addFragment(new ProfileFragment(),"Profile");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);}catch (Exception e){

         }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        status("offline");
    }
}
