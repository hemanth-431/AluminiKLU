package com.example.aluminiklu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class benefitygiven extends Fragment {
TextView t;
DatabaseReference databaseReference;
FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
StringBuilder s=new StringBuilder();
        View view = inflater.inflate(R.layout.activity_benefitygiven, container, false);
t=view.findViewById(R.id.data123);
floatingActionButton=view.findViewById(R.id.add_event);
floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(getActivity(),addaluminiben.class);
        startActivity(i);
    }
});
databaseReference= FirebaseDatabase.getInstance().getReference().child("Benfits");
databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        try{

            t.setText(dataSnapshot.getValue().toString());

        }catch (Exception e){

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

        return view;
    }
    }
