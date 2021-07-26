package my.alumni.klu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class personal_info extends AppCompatActivity {
TextView a,b,c,d,e,f,g,h;
ImageView imageView,arroe;
Context context;
DatabaseReference databaseReference,reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        a=findViewById(R.id.branch);
        arroe=findViewById(R.id.ARROW_BACK);
        b=findViewById(R.id.country);
        imageView=findViewById(R.id.profiledp);
        c=findViewById(R.id.course);
        d=findViewById(R.id.name);
        e=findViewById(R.id.graduated);
        f=findViewById(R.id.join);
        g=findViewById(R.id.mailid);
        h=findViewById(R.id.specialization);
        Bundle bundle = getIntent().getExtras();
        String path=null;
       try{ path=bundle.getString("STRING_I_NEED").toString();}
       catch (Exception e){

       }
       arroe.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               finish();
           }
       });
        Activity activity=personal_info.this;
        Window window =activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(path);
      reference=FirebaseDatabase.getInstance().getReference().child("Users1").child(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
try{                String data=dataSnapshot.child("Branch").getValue().toString();
                a.setText(data);
                String data1=dataSnapshot.child("Country").getValue().toString();
                b.setText(data1);
                String data2=dataSnapshot.child("Course").getValue().toString();
                c.setText(data2);
                String data3=dataSnapshot.child("FullName").getValue().toString();
                d.setText(data3);}catch(Exception e){}
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

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.child("imageUrl").getValue().toString();
                Picasso.with(context).load(data).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
