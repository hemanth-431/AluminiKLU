package com.example.aluminiklu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements image_adapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private image_adapter mAdapter;
    private ImageView upload;
private TextView uploading;
    private ProgressBar mProgressCircle;
private FirebaseAuth firebaseAuth;
SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
String str,stri="hjnc",path;
int count=0;
    private List<upload> mUploads;
    ArrayList<String> s=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);




        firebaseAuth=FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
try {

    if (bundle.getString("STRING_I_NEED") != null) {
        str = bundle.getString("STRING_I_NEED");
        if(count==0)
        { path=str;
        count=1;}
        else
        {path=path;}
        // Toast.makeText(ImagesActivity.this,bundle.getString("STRING_I_NEED"),Toast.LENGTH_LONG).show();
    }
}catch (Exception e)
{

}
uploading=findViewById(R.id.upload123);
upload=findViewById(R.id.upload);
upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(ImagesActivity.this,home.class);
        startActivity(i);
    }
});

        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mAdapter = new image_adapter(ImagesActivity.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

    uploading.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(ImagesActivity.this,ChatBox.class);
            startActivity(i);
          //  String s=firebaseAuth.getCurrentUser().getUid();
         //   Toast.makeText(ImagesActivity.this, s, Toast.LENGTH_LONG).show();
        }
    });


       mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    upload upload = postSnapshot.getValue(upload.class);
                    upload.setKey(postSnapshot.getKey());
                    s.add(postSnapshot.getKey().toString());

                      //  Toast.makeText(ImagesActivity.this,s.get(0).toString(),Toast.LENGTH_LONG).show();



                    mUploads.add(upload);
                }
                Collections.reverse(mUploads);

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });



    }
    /////////

    @Override
    public void onItemClick(int position) {


     //   Toast.makeText(this, path + s.get(position), Toast.LENGTH_SHORT).show();


        Intent i = new Intent(ImagesActivity.this, information.class);

        i.putExtra("STRING_I_NEED", s.get(position));
        startActivity(i);


    }



    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ImagesActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);



    }






}