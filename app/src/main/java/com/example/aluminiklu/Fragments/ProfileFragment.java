package com.example.aluminiklu.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.aluminiklu.Model.user;
import com.example.aluminiklu.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {


    CircleImageView imageprofile;
    TextView username;
DatabaseReference reference,databaseReference;
FirebaseUser fuser;
private TextView a,b,c,d,e,f,g,h;
StorageReference storageReference;
private static final int IMAGE_REQUEST=1;
private Uri imageUri;
private StorageTask uploadTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        imageprofile=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.username);
        a=view.findViewById(R.id.branch);
        b=view.findViewById(R.id.country);
        c=view.findViewById(R.id.course);
        d=view.findViewById(R.id.name);
        e=view.findViewById(R.id.graduated);
        f=view.findViewById(R.id.join);
        g=view.findViewById(R.id.mailid);
        h=view.findViewById(R.id.specialization);

        storageReference= FirebaseStorage.getInstance().getReference("uploads1");

fuser= FirebaseAuth.getInstance().getCurrentUser();
databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
String a1=dataSnapshot.child("Branch").getValue().toString();
a.setText(a1);
        String b1=dataSnapshot.child("Country").getValue().toString();
        b.setText(b1);
        String c1=dataSnapshot.child("Course").getValue().toString();
        c.setText(c1);
        String d1=dataSnapshot.child("FullName").getValue().toString();
        d.setText(d1);
        String e1=dataSnapshot.child("Graduation Year").getValue().toString();
        e.setText(e1);
        String f1=dataSnapshot.child("Join Date").getValue().toString();
        f.setText(f1);
        String g1=dataSnapshot.child("MailId").getValue().toString();
        g.setText(g1);
        String h1=dataSnapshot.child("Specialization").getValue().toString();
        h.setText(h1);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
reference= FirebaseDatabase.getInstance().getReference("Users1").child(fuser.getUid());
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         user user=dataSnapshot.getValue(com.example.aluminiklu.Model.user.class);
        username.setText(user.getUsername());
        try {
            if (user.getImageUrl().equals("default")) {

                imageprofile.setImageResource(R.mipmap.ic_launcher);

            } else {
                Picasso.with(getContext()).load(user.getImageUrl()).into(imageprofile);

            }
        }
        catch (Exception e){

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
imageprofile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openImage();
    }
});
        return view;
    }
    public void openImage(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,IMAGE_REQUEST);
    }

private String getFileException(Uri uri){
    ContentResolver contentResolver=getContext().getContentResolver();
    MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
}
private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();
        if(imageUri!=null)
        {
            final StorageReference finalreference=storageReference.child(System.currentTimeMillis()+"."+getFileException(imageUri));
            uploadTask=finalreference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                  if(!task.isSuccessful()){
                      throw task.getException();
                  }
                  return finalreference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
if(task.isSuccessful()){
    Uri downloadUri=task.getResult();
    String mUri=downloadUri.toString();
    reference=FirebaseDatabase.getInstance().getReference("Users1").child(fuser.getUid());
    HashMap<String,Object> map=new HashMap<>();
    map.put("imageUrl",mUri);
    reference.updateChildren(map);
    pd.dismiss();
}else {
    Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
    pd.dismiss();
}
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });

        }else {
            Toast.makeText(getContext(),"No Item Selected",Toast.LENGTH_LONG).show();
        }
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null){
            imageUri=data.getData();
            if(uploadTask!=null && uploadTask.isInProgress() )
            {
Toast.makeText(getContext(),"Uploading in progerss",Toast.LENGTH_LONG).show();
            }
            else {
                uploadImage();
            }
        }
    }
}

