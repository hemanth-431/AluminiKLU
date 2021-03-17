package com.example.aluminiklu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.aluminiklu.Fragments.ProfileFragment;
import com.example.aluminiklu.Model.user;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Side_drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
private DrawerLayout drawer;
    DatabaseReference reference,databaseReference;
    FirebaseUser fuser;
    FirebaseAuth firebaseAuth;
    TextView a,b;
    String imageurl;
    CircleImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_drawer);

firebaseAuth=FirebaseAuth.getInstance();

Toolbar toolbar=findViewById(R.id.toolbar);
setSupportActionBar(toolbar);

drawer=findViewById(R.id.drawer_layout);



        NavigationView navigationView=findViewById(R.id.navigation_view);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }


        View hView=navigationView.getHeaderView(0);
        a=(TextView)hView.findViewById(R.id.name1);
        profile=hView.findViewById(R.id.profilepick123);
        b=hView.findViewById(R.id.mail1);

        fuser= FirebaseAuth.getInstance().getCurrentUser();

      try{  String s=fuser.getUid().toString();}catch (Exception e){
Intent i=new Intent(Side_drawer.this, Login.class);
startActivity(i);

      }
       try {
           System.out.print(fuser.getUid()+" ************ ");
           reference = FirebaseDatabase.getInstance().getReference("Users1").child(fuser.getUid());
           reference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   user user = dataSnapshot.getValue(com.example.aluminiklu.Model.user.class);
try{         imageurl = dataSnapshot.child("imageUrl").getValue().toString();
                   a.setText(user.getUsername());

                   if (imageurl.equals("default")) {

                       profile.setImageResource(R.mipmap.ic_launcher);

                   } else {
                       Picasso.with(Side_drawer.this).load(imageurl).into(profile);

                   }
}catch (Exception e){}

               }

               @Override

               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

           databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   String smail = dataSnapshot.child("MailId").getValue().toString();
                   b.setText(smail);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }catch (Exception e){}


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
                setName("Alumini");
                break;
            case R.id.Addnew:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home()).commit();
                setName("Add New");
                break;
            case R.id.events:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new eventRecycler()).commit();
                setName("Events");
                break;
            case R.id.Chatbox:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatBox()).commit();
                setName("Chat Box");
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                setName("Profile");
                break;
            case R.id.vips:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new benefitygiven()).commit();
                setName("Alumini benefits");
                break;
            case R.id.jobs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new jobsactivity()).commit();
                setName("Jobs");
                break;

            case R.id.Logout:
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                firebaseAuth.signOut();
                finish();
                Intent i=new Intent(Side_drawer.this,loginexample.class);
                startActivity(i);

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void setName(String title){
    Toast.makeText(Side_drawer.this,title,Toast.LENGTH_LONG).show();
        getSupportActionBar().setTitle(title);

}

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = ResourcesCompat.getFont(Side_drawer.this, R.font.doppio_one);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

}