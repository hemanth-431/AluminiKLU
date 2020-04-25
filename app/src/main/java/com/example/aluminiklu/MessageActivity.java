package com.example.aluminiklu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class MessageActivity extends AppCompatActivity implements MessageAdapter.OnitemclickListener {
CircleImageView profile_image;
TextView username;
private ValueEventListener mdblistener;
FirebaseUser firebaseUser;
TextView info;
ImageButton btn_send;
EditText text_send;

DatabaseReference reference;
MessageAdapter messageAdapter;
MessageActivity messageActivity;
List<chat> mchat;
RecyclerView recyclerView;
DatabaseReference databaseReference;
Intent intent;
ValueEventListener seenListener;
APIServer apiService;
boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // finish();
                startActivity(new Intent(MessageActivity.this,ChatBox.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
{
    token token1=dataSnapshot1.getValue(token.class);
    String userid=intent.getStringExtra("userid");
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

        messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageurl);
        recyclerView.setAdapter(messageAdapter);
        messageAdapter.setOnitemclickListener(MessageActivity.this);

reference=FirebaseDatabase.getInstance().getReference("Chats");
mdblistener=reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mchat.clear();
        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
        {
            chat chat=dataSnapshot1.getValue(com.example.aluminiklu.Model.chat.class);
            if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)  || chat.getReceiver().equals(userid) && chat.getSender().equals(myid) )
            {
                chat.setIdkey(dataSnapshot1.getKey());
mchat.add(chat);
            }
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
        reference.removeEventListener(seenListener);
        status("offline");
    }

    @Override
    public void onDeleteClick(int position) {
       chat selsectitem=mchat.get(position);
       String selkey=selsectitem.getIdkey();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Chats");
        databaseReference.child(selkey).removeValue();
        Toast.makeText(MessageActivity.this,"deletetsuccess",Toast.LENGTH_LONG).show();
    }

}
