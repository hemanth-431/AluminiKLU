package com.example.aluminiklu;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class information extends AppCompatActivity {
    DatabaseReference databaseReference;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


textView=findViewById(R.id.paragraph);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("STRING_I_NEED")!= null)
        {
            databaseReference= FirebaseDatabase.getInstance().getReference().child("uploads").child(bundle.getString("STRING_I_NEED"));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              try {
                  String data = dataSnapshot.child("mData").getValue().toString();
                  textView.setText(data);
              }
              catch (Exception e)
              {
                  
              }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
          // String na=databaseReference.child("mData").toString();
           // textView.setText(na);
            Toast.makeText(information.this,bundle.getString("STRING_I_NEED"),Toast.LENGTH_LONG).show();
        }
    }
}
