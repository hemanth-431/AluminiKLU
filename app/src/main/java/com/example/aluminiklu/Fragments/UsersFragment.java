package com.example.aluminiklu.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Adapter.UserAdapter;
import com.example.aluminiklu.Model.user;
import com.example.aluminiklu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
private RecyclerView recyclerView;
private UserAdapter userAdapter;
EditText search_users;
private List<user> musers;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_users,container,false);
        recyclerView=view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        musers=new ArrayList<>();
        readusers();


        search_users=view.findViewById(R.id.search_users);
       search_users.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchusers(s.toString().toLowerCase());
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        return view;
    }

    public void searchusers(String s)
    {
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("Users1").orderByChild("search")//.orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    user user=dataSnapshot1.getValue(com.example.aluminiklu.Model.user.class);
                    assert fuser != null;
                    assert user != null;
                    if(!user.getId().equals(fuser.getUid())){
                        musers.add(user);
                    }
                }
                userAdapter=new UserAdapter(getContext(),musers,false);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readusers()
    {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (search_users.getText().toString().equals("")) {
                    musers.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        user user = dataSnapshot1.getValue(com.example.aluminiklu.Model.user.class);

                        assert user != null;

                        try {


                            if (!user.getId().equals(firebaseUser.getUid())) {
                                musers.add(user);
                            }
                        }catch (Exception e){

                        }
                    }
                    userAdapter = new UserAdapter(getContext(), musers,false);
                    recyclerView.setAdapter(userAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}