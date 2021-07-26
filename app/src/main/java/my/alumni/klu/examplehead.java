package my.alumni.klu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import my.alumni.klu.Model.user;

public class examplehead extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseUser fuser;
    TextView a,b;
    String imageurl;
    CircleImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examplehead);

        profile=findViewById(R.id.profilepick123);
        a=findViewById(R.id.name1);
        b=findViewById(R.id.mail1);
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        String s=fuser.getUid().toString();
        reference= FirebaseDatabase.getInstance().getReference("Users1").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user user=dataSnapshot.getValue(my.alumni.klu.Model.user.class);
                 imageurl=dataSnapshot.child("imageUrl").getValue().toString();
                a.setText(user.getUsername());

                    if (imageurl.equals("default")) {

                        profile.setImageResource(R.mipmap.ic_launcher);

                    } else {
                        Picasso.with(examplehead.this).load(imageurl).into(profile);

                    }

            }

            @Override

            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(examplehead.this,imageurl,Toast.LENGTH_LONG).show();

            }
        });
    }
}
