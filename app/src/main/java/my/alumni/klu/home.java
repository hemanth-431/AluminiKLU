package my.alumni.klu;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class home extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
private ProgressBar progressBar;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private TextInputEditText mEditTextFileName;
    private LottieAnimationView mImageView;
    String sname;

    private FirebaseAuth mAuth;
    private Uri mImageUri;
//private ImageView imageView;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,nameDta;
private TextView adddis;
    private StorageTask mUploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_home,container,false);


        progressBar = view.findViewById(R.id.progress_circle);
        progressBar.setVisibility(View.INVISIBLE);

//logout=findViewById(R.id.logout);
        mButtonChooseImage = view.findViewById(R.id.button3);
        mButtonUpload = view.findViewById(R.id.button_upload);
       // mTextViewShowUploads = view.findViewById(R.id.text_view_uploads);

//imageView=view.findViewById(R.id.imageing);
        mImageView = view.findViewById(R.id.image_view);

        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        nameDta = FirebaseDatabase.getInstance().getReference().child("Users1").child(mAuth.getUid());
        nameDta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               sname=dataSnapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        sendToStart();



    }
});

 */
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            //   imageView.setVisibility(View.GONE);
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
progressBar.setVisibility(View.VISIBLE);
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    uploadFile();
                }
            }
        });

     /*   mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

    a  '
             //   Toast.makeText(home.this,c,Toast.LENGTH_LONG).show();

                openImagesActivity();
            }
        });

      */
     return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){

            sendToStart();
        }
    }
    private void sendToStart() {
        Intent start=new Intent(getActivity(),Login.class);
        startActivity(start);

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getActivity()).load(mImageUri).fit().centerCrop().into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
       if (mImageUri != null ) {

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException(); }
                            return fileReference.getDownloadUrl();
                        } })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) { Uri downloadUri = task.getResult();
                            String S="";
                                String s1=mAuth.getCurrentUser().getUid();
//                                Toast.makeText(getActivity(),s1,Toast.LENGTH_LONG).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String currentDateandTime = sdf.format(new Date());
                                String s = mDatabaseRef.push().getKey();
                                upload upload = new upload("", downloadUri.toString(),S,s1,currentDateandTime,s,sname);

                                mDatabaseRef.child(s).setValue(upload);
                                progressBar.setVisibility(View.GONE);

                               Intent i = new Intent(getActivity(), add_discription.class);

                              i.putExtra("STRING_I_NEED", s);
                               startActivity(i);
                                //hashMap.put("status","offline");
//                                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                            }
                            else { Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void openImagesActivity() {


        Intent intent = new Intent(getActivity(), ImagesActivity.class);



        startActivity(intent);
    }



    }
