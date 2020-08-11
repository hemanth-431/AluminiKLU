package com.example.aluminiklu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Adapter.MessageAdapter;
import com.example.aluminiklu.Fragments.APIServer;
import com.example.aluminiklu.Model.chat;
import com.example.aluminiklu.Model.user;
import com.example.aluminiklu.Notifications.Client;
import com.example.aluminiklu.Notifications.MyResponse;
import com.example.aluminiklu.Notifications.Sender;
import com.example.aluminiklu.Notifications.data;
import com.example.aluminiklu.Notifications.token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity implements MessageAdapter.OnitemclickListener, FragmentManager.OnBackStackChangedListener {
CircleImageView profile_image;
TextView username;
private static final int REQUEST_CALL=1;
private ValueEventListener mdblistener;
FirebaseUser firebaseUser;
ImageView info;
ImageButton btn_send;
EditText text_send;
ImageView phone;
DatabaseReference reference;
MessageAdapter messageAdapter;
MessageActivity messageActivity;
List<chat> mchat;
RecyclerView recyclerView;
DatabaseReference databaseReference,getDatabaseReference;
Intent intent;
ValueEventListener seenListener;
APIServer apiService;
boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        phone=findViewById(R.id.image_call);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Activity activity=MessageActivity.this;
        Window window =activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

phone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final String User123=intent.getStringExtra("userid");
        makePhoneCall(User123);
    }
});

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String User13="No";
                try{  User13=intent.getStringExtra("condition");}catch(Exception e){

                }
              try{  if(User13.equals("Yes")){}}catch (Exception e){
                  User13="No";
              }
                if(User13.equals("Yes"))
                {

                    Intent i=new Intent(MessageActivity.this,Side_drawer.class);
                    startActivity(i);}
                else{
                    // System.out.println(User13+"--------------------------------");
                    //   Intent i=new Intent(MessageActivity.this,Side_drawer.class);
                    //   startActivity(i);
                    finish();
                }




              //  onBackStackChanged();
        //        Fragment fragment = new ChatBox();
        //        FragmentManager fragmentManager = getSupportFragmentManager();
        //        fragmentManager.beginTransaction().replace(messageActivity., fragment).commit();
      //          startActivity(new Intent(MessageActivity.this,ChatBox.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIServer.class);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


profile_image=findViewById(R.id.profile_image);
username=findViewById(R.id.user);
btn_send=findViewById(R.id.btn_send);
info=findViewById(R.id.information);
text_send=findViewById(R.id.text_send);
intent=getIntent();


info.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(MessageActivity.this, personal_info.class);
        final String userid=intent.getStringExtra("userid");
        i.putExtra("STRING_I_NEED", userid);
        startActivity(i);
        //Toast.makeText(MessageActivity.this,userid,Toast.LENGTH_LONG).show();
    }
});

btn_send.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        notify=true;
        String msg=text_send.getText().toString();
        if(!msg.equals("")){
            String userid=intent.getStringExtra("userid");
            sendmessage(firebaseUser.getUid(),userid,msg);

        }else {
            Toast.makeText(MessageActivity.this,"You can't send empty message",Toast.LENGTH_LONG).show();
        }
        text_send.setText("");
    }
});

firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userid=intent.getStringExtra("userid");
databaseReference= FirebaseDatabase.getInstance().getReference("Users1").child(userid);
//Toast.makeText(MessageActivity.this,userid,Toast.LENGTH_LONG).show();
databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        user user=dataSnapshot.getValue(com.example.aluminiklu.Model.user.class);
        if(user.getUsername()!=null)
        {username.setText(user.getUsername().toString());}
        if (user.getImageUrl()!=null)
        if(user.getImageUrl().equals("default"))
        {
            profile_image.setImageResource(R.mipmap.ic_launcher);

        }else {
            Picasso.with(getApplicationContext()).load(user.getImageUrl()).into(profile_image);

        }
        //Toast.makeText(MessageActivity.this,userid,Toast.LENGTH_LONG).show();
        readmessages(firebaseUser.getUid(),userid,user.getImageUrl());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

});
seenMessage(userid);
    }

    private void makePhoneCall(String User123){
//Toast.makeText(MessageActivity.this,User123,Toast.LENGTH_LONG).show();
getDatabaseReference=FirebaseDatabase.getInstance().getReference("Users").child(User123);
        getDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String  data=dataSnapshot.child("Call").getValue().toString();
                Toast.makeText(MessageActivity.this,"calling "+data,Toast.LENGTH_LONG).show();
                if(data.trim().length() > 0){

                    if(ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MessageActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
                    }else {
                        String dial="tel:"+data;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }

                }else {
                    Toast.makeText(MessageActivity.this,"Number Changed...",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }


    private void seenMessage(final String userid){
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    chat chat=dataSnapshot1.getValue(com.example.aluminiklu.Model.chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        dataSnapshot1.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendmessage(String sender,String receiver,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        final String userid = intent.getStringExtra("userid");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("isseen",false);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        String s=reference.push().getKey();
        hashMap.put("idkey",s);
        reference.child("Chats").child(s).setValue(hashMap);

      /*  final DatabaseReference chatref=FirebaseDatabase.getInstance().getReference("chatlist")
        .child(firebaseUser.getUid())
        .child(userid);
chatref.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         if(!dataSnapshot.exists()){
             chatref.child("id").setValue(userid);/////////////////
         }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

       */
      final String msg=message;
      reference = FirebaseDatabase.getInstance().getReference("Users1").child(firebaseUser.getUid());
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
user user=dataSnapshot.getValue(user.class);
if(notify) {
    sendNotification(receiver, user.getUsername(), msg);
}
notify=false;
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


    }

    private void sendNotification(String receiver,String username,String message)
    {
        System.out.println(receiver+"***----");
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
{
    token token1=dataSnapshot1.getValue(token.class);
    String userid=intent.getStringExtra("userid");
    System.out.println(userid+"-------");
    data data1=new data(firebaseUser.getUid(),R.mipmap.ic_launcher,username+": "+message,"New Message",userid);
    Sender sender=new Sender(data1,token1.getToken());
    apiService.sendNotification(sender)
            .enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    if(response.code() == 200)
                    {
                        if(response.body().success != 1)
                        {
                            Toast.makeText(MessageActivity.this,"Failed!",Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MyResponse> call, Throwable t) {

                }
            });
}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readmessages(final String myid, final String userid, final String imageurl){
        mchat=new ArrayList<>();


     //   messageAdapter.setOnitemclickListener(MessageActivity.this);

reference=FirebaseDatabase.getInstance().getReference("Chats");
mdblistener=reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mchat.clear();
        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
        {
            chat chat=dataSnapshot1.getValue(chat.class);
            if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)  || chat.getReceiver().equals(userid) && chat.getSender().equals(myid) )
            {
                chat.setIdkey(dataSnapshot1.getKey());
mchat.add(chat);
            }
            messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageurl);
            recyclerView.setAdapter(messageAdapter);
messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mdblistener);
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
        editor.putString("currentuser",userid);
        editor.apply();
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
        String userid=intent.getStringExtra("userid");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        String userid=intent.getStringExtra("userid");
        currentUser("none");
    }

    @Override
    public void onDeleteClick(int position) {
       chat selsectitem=mchat.get(position);
       String selkey=selsectitem.getIdkey();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Chats");
        databaseReference.child(selkey).removeValue();
        Toast.makeText(MessageActivity.this,"deletetsuccess",Toast.LENGTH_LONG).show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode == REQUEST_CALL){
           if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
               final String User123=intent.getStringExtra("userid");
               makePhoneCall(User123);
           }else {
               Toast.makeText(this,"Permission DENIED",Toast.LENGTH_SHORT).show();
           }
       }
    }


    @Override
    public void onBackStackChanged() {
FragmentManager fm=getSupportFragmentManager();
fm.addOnBackStackChangedListener(this);
    }
}
