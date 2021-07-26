package my.alumni.klu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.alumni.klu.Adapter.comment;
import my.alumni.klu.Model.CommentModel;
import my.alumni.klu.Model.user;

public class Comments extends AppCompatActivity {
    private RecyclerView recyclerView;
    private comment commentAdapter;
    private List<CommentModel> commentModelList;
EditText addcompat;
ImageView image_profile;
ImageView post;

String postid,publisherid;
FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Window window = Comments.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(Comments.this.getResources().getColor(R.color.primarydark));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentModelList=new ArrayList<>();
        commentAdapter=new comment(this,commentModelList);

        recyclerView.setAdapter(commentAdapter);


        addcompat=findViewById(R.id.add_comment);
        image_profile=findViewById(R.id.image_profile);
        post=findViewById(R.id.post);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Intent intent=getIntent();
        postid=intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addcompat.getText().toString().equals(""))
                {
                   Toast.makeText(Comments.this,"You can't send empty comment", Toast.LENGTH_LONG).show();
                }
                else {
                    addComment();
                }
            }
        });

        getImage();
        readComments();
    }
    private void addComment(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("comment",addcompat.getText().toString());
        hashMap.put("publisher",firebaseUser.getUid());
        databaseReference.push().setValue(hashMap);
        addcompat.setText("");
    }

    private void getImage(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users1").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
     user user=dataSnapshot.getValue(my.alumni.klu.Model.user.class);
                Picasso.with(getApplicationContext()).load(user.getImageUrl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readComments(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentModelList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    CommentModel commentModel=snapshot.getValue(CommentModel.class);
                    commentModelList.add(commentModel);
                }
//                Toast.makeText(Comments.this,"Ooops",Toast.LENGTH_LONG).show();
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
