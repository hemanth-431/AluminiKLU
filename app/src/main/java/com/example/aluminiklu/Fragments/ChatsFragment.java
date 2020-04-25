package com.example.aluminiklu.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Adapter.UserAdapter;
import com.example.aluminiklu.Model.chat;
import com.example.aluminiklu.Model.user;
import com.example.aluminiklu.Notifications.token;
import com.example.aluminiklu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
private UserAdapter userAdapter;
private RecyclerView recyclerView;
private List<user> musers;
private FirebaseUser fuser;
DatabaseReference reference;
private List<String> userlist;  //<ChatList>
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_chats,container,false);
       recyclerView=view.findViewById(R.id.recycler_view);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       fuser= FirebaseAuth.getInstance().getCurrentUser();
       userlist=new ArrayList<>();
      /* reference= FirebaseDatabase.getInstance().getReference("chatlist").child(fuser.getUid());
       reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        userlist.clear();
                                                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                            Chatlist chatlist=snapshot.getValue(Chatlist.class);
                                                            userlist.add(chatlist);

                                                        }
                                                        chatList();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });   */
       reference= FirebaseDatabase.getInstance().getReference("Chats");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               userlist.clear();
               for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
               {
                   chat chat=dataSnapshot1.getValue(com.example.aluminiklu.Model.chat.class);
                   if(chat.getSender().equals(fuser.getUid()))
                   {
                       if(!userlist.contains(chat.getReceiver()))
                       {userlist.add(chat.getReceiver());}
                   }
                   if(chat.getReceiver().equals(fuser.getUid()))
                   {
                       if(!userlist.contains(chat.getSender()))
                       { userlist.add(chat.getSender());}
                   }
               }
               readchats();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
updatetoken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void updatetoken(String token)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
      token token1=new token(token);

        reference.child(fuser.getUid()).setValue(token1);
    }


    //delete
  private void readchats(){
            musers=new ArrayList<>();
            reference=FirebaseDatabase.getInstance().getReference("Users1");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    musers.clear();
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        user user=dataSnapshot1.getValue(com.example.aluminiklu.Model.user.class);
                        //display one user from charts
                        for (String id:userlist)
                        {
                            try {
                                if (user.getId().equals(id)) {
                                    if (musers.size() != 0) {
                                        for (user user1 : musers) {
                                            if (!user.getId().equals(user1.getId())) {
                                                musers.add(user);
                                                break;

                                            }
                                        }
                                    } else {
                                        musers.add(user);
                                    }

                                }
                            }catch (Exception e)
                            {

                            }
                        }
                    }
                    userAdapter=new UserAdapter(getContext(),musers,true);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }



/*public void chatList(){
    musers=new ArrayList<>();
    reference=FirebaseDatabase.getInstance().getReference("Users1");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            musers.clear();
            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                user user=snapshot.getValue(com.example.aluminiklu.Model.user.class);
                for(Chatlist chatlist:userlist){
                    if(user.getId().equals(chatlist.getId())){
                        musers.add(user);
                    }
                }
            }
            userAdapter=new UserAdapter(getContext(),musers,true);
            recyclerView.setAdapter(userAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
*/
}