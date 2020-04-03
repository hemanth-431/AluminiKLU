package com.example.aluminiklu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Model.CommentModel;
import com.example.aluminiklu.Model.user;
import com.example.aluminiklu.R;
import com.example.aluminiklu.personal_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class comment extends RecyclerView.Adapter<comment.ViewHolder> {

    private Context mcontext;
private List<CommentModel> mcomment;
private FirebaseUser firebaseUser;

    public comment(Context mcontext, List<CommentModel> mcomment) {
        this.mcontext = mcontext;
        this.mcomment = mcomment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.comment_item,parent,false);
        return new comment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
CommentModel comment=mcomment.get(position);
holder.comment.setText(comment.getComment());
getUserInfo(holder.image_profile,holder.username,comment.getPublisher());
holder.comment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),"You can see User info",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(mcontext, personal_info.class);
          intent.putExtra("STRING_I_NEED",comment.getPublisher());
        mcontext.startActivity(intent);
    }
});
        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),comment.getPublisher(),Toast.LENGTH_LONG).show();
                 Intent intent=new Intent(mcontext, personal_info.class);
                  intent.putExtra("STRING_I_NEED",comment.getPublisher());
                 mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mcomment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
public ImageView image_profile;
public TextView username,comment;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=itemView.findViewById(R.id.image_profile);
            username=itemView.findViewById(R.id.username);
            comment=itemView.findViewById(R.id.comment);
        }
    }
    private void getUserInfo(ImageView imageView,TextView username,String publisher)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users1").child(publisher);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user user=dataSnapshot.getValue(com.example.aluminiklu.Model.user.class);
                Picasso.with(mcontext).load(user.getImageUrl()).into(imageView);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
