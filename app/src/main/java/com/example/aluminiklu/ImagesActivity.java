package com.example.aluminiklu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImagesActivity extends Fragment implements image_adapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageView logout;
    private image_adapter mAdapter;
    private ImageView upload;
private ImageView uploading;
Context context;

    private ProgressBar mProgressCircle;
private FirebaseAuth firebaseAuth;
private ImageView imageView;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
String str,stri="hjnc",path;
int count=0;
    private List<upload> mUploads;
    ArrayList<String> soop=new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_images,container,false);




        firebaseAuth=FirebaseAuth.getInstance();

        Bundle bundle =getActivity().getIntent().getExtras();
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
logout=view.findViewById(R.id.Logout_1);
uploading=view.findViewById(R.id.upload123);
upload=view.findViewById(R.id.upload);
imageView=view.findViewById(R.id.image_view_up);


try {
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            sendToStart();


        }
    });
}catch (Exception e){

}


try{upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(getActivity(),home.class);
        startActivity(i);
    }
});}
catch (Exception e){

}

        mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));



        mProgressCircle = view.findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mAdapter = new image_adapter(getActivity(), mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");




   try {
       uploading.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getActivity(), ChatBox.class);
               startActivity(i);
               //  String s=firebaseAuth.getCurrentUser().getUid();
               //   Toast.makeText(ImagesActivity.this, s, Toast.LENGTH_LONG).show();
           }
       });
   }catch (Exception e){

   }


       mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    upload upload = postSnapshot.getValue(upload.class);
                    upload.setKey(postSnapshot.getKey());
                    soop.add(postSnapshot.getKey().toString());

                    Collections.reverse(soop);

                      //  Toast.makeText(ImagesActivity.this,s.get(0).toString(),Toast.LENGTH_LONG).show();



                    mUploads.add(upload);
                }
                Collections.reverse(mUploads);

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


return  view;
    }


    private void sendToStart() {
        Intent start=new Intent(getActivity(),Login.class);
        startActivity(start);
        //finish();
    }
    /////////

    @Override
    public void onItemClick(int position) {


        Toast.makeText(getActivity(), path + soop.get(position), Toast.LENGTH_SHORT).show();


        Intent i = new Intent(getActivity(), information.class);

        i.putExtra("STRING_I_NEED", soop.get(position));
        startActivity(i);


    }



    @Override
    public void onWhatEverClick(int position) {
      //  sharecontent(position);
        upload selectedItem = mUploads.get(position);
           final String selectedKey = selectedItem.getKey();

mDatabaseRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String data12=dataSnapshot.child(selectedKey).child("imageUrl").getValue().toString();
        Toast.makeText(getActivity(), data12, Toast.LENGTH_SHORT).show();

        Intent sendIntend=new Intent();
        sendIntend.setAction(Intent.ACTION_SEND);
        sendIntend.putExtra(Intent.EXTRA_TEXT,data12);
        sendIntend.setType("text/plain");
        startActivity(sendIntend);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});


     //   sharecontent(str);
       // Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    private void sharecontent(String position) {
      //  upload selectedItem = mUploads.get(position);
     //   final String selectedKey = selectedItem.getKey();

     //   StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
      //  Picasso.with(context).load(String.valueOf(imageRef)).into(imageView);

        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        try{
            File file=new File(getActivity().getExternalCacheDir(),"logicchip.png");
            FileOutputStream fout=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.flush();
            fout.close();
            file.setReadable(true,false);
            final Intent i=new Intent(Intent.ACTION_SEND);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Intent.EXTRA_TEXT,"nice");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            i.setType("image/png");
            startActivity(Intent.createChooser(i,"Share image via"));

        }catch (Exception e)
        {
e.printStackTrace();
        }
    }
    private Bitmap getBitmap(View view)
    {
Bitmap returnBitmap=Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas =new Canvas(returnBitmap);
        Drawable drawable=view.getBackground();
        if(drawable!=null)
        {
            drawable.draw(canvas);
        }else {
            canvas.drawColor(Color.WHITE);        }
        view.draw(canvas);
        return returnBitmap;
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
                Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);



    }

    public void onClick (){
        Intent intent = getActivity().getIntent();

        startActivity(intent);
    }








}