package com.example.aluminiklu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class personal_info extends AppCompatActivity {
TextView a,b,c,d,e,f,g,h;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        a=findViewById(R.id.branch);
        b=findViewById(R.id.country);
        c=findViewById(R.id.course);
        d=findViewById(R.id.name);
        e=findViewById(R.id.graduated);
        f=findViewById(R.id.join);
        g=findViewById(R.id.mailid);
        h=findViewById(R.id.specialization);
        Bundle bundle = getIntent().getExtras();

        String path=bundle.getString("STRING_I_NEED").toString();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(path);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.child("Branch").getValue().toString();
                a.setText(data);
                String data1=dataSnapshot.child("Country").getValue().toString();
                b.setText(data1);
                String data2=dataSnapshot.child("Course").getValue().toString();
                c.setText(data2);
                String data3=dataSnapshot.child("FullName").getValue().toString();
                d.setText(data3);
                String data4=dataSnapshot.child("Graduation Year").getValue().toString();
                e.setText(data4);
                String data5=dataSnapshot.child("Join Date").getValue().toString();
                f.setText(data5);
                String data6=dataSnapshot.child("MailId").getValue().toString();
                g.setText(data6);
                String data7=dataSnapshot.child("Specialization").getValue().toString();
                h.setText(data7);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
