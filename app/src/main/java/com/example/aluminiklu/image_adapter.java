package com.example.aluminiklu;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class image_adapter extends RecyclerView.Adapter<image_adapter.ImageViewHolder> {
    private TextView textView;
    private Context mContext;
    private List<upload> mUploads;
    public CircleImageView circleImageView;
    private OnItemClickListener mListener;
    private FirebaseAuth firebaseAuth;
    String boom=null;

    public    String check="unfill";

    public int count=0;

    private DatabaseReference databaseReference,getnam;
    public image_adapter(Context context, List<upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        upload uploadCurrent = mUploads.get(position);
      try{  holder.textViewName.setText(uploadCurrent.getName());}catch (Exception e){}
        firebaseAuth=FirebaseAuth.getInstance();

        // String s=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("uploads").child(uploadCurrent.getKey());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                /////////////////////////////////////////////////////////////////////////////////////////
                try {
                    synchronized(this){
                        wait(300);
                    }
                }
                catch(InterruptedException ex){
                }

                try {
                    boom = dataSnapshot.child("key").getValue().toString();   /////////////////////////////error
                } catch (Exception e) {


                }
                try {
                    synchronized(this){
                        wait(300);
                    }
                }
                catch(InterruptedException ex){
                }
////////////////////////////////////////////////////////////////////////////////////////////////////







/*


                unfill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(check.equals("unfill"))
                        {count++;
                            animateHeart(fill);
                            fill.setVisibility(View.VISIBLE);
                            unfill.setVisibility(View.GONE);
                        }
                        String s=Integer.toString(count);
                        like.setText(s);
                        String pos=Integer.toString(position);
                        Snackbar.make(v,pos,Snackbar.LENGTH_LONG).setAction("Action",null).show();
                         check="fill";

                    }
                });


              fill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            if (check.equals("fill"))
                            {
                                dislikeheart(unfill);
                                fill.setVisibility(View.GONE);
                                unfill.setVisibility(View.VISIBLE);
                            }
                            if(count>0)
                                count--;
                            String s1=Integer.toString(count);
                            like.setText(s1);
                            Snackbar.make(v,boom,Snackbar.LENGTH_LONG).setAction("Action",null).show();
                            check="unfill";

                    }
                });

            */




                getnam = FirebaseDatabase.getInstance().getReference().child("Users1").child(boom);
                getnam.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String data1 = dataSnapshot.child("username").getValue().toString();
                        textView.setText(data1);
                        String data2 = dataSnapshot.child("imageUrl").getValue().toString();
                        Picasso.with(mContext).load(data2).into(circleImageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, personal_info.class);
                        intent.putExtra("STRING_I_NEED",boom);
                        mContext.startActivity(intent);
                    }
                });

                circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, personal_info.class);
                        intent.putExtra("STRING_I_NEED",boom);
                        mContext.startActivity(intent);
                    }
                });






            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });






    /*  textView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(boom.equals("MA7kK4DWtubZ0q1mG87Ulzf9sUJ2"))
              { Toast.makeText(v.getContext(),boom,Toast.LENGTH_LONG).show();}
              else {
                 textView.setText(boom+"\nMA7kK4DWtubZ0q1mG87Ulzf9sUJ2");
              }
          }
      });
*/


        //  Picasso.with(mContext).load(user.getImageUrl()).into(circleImageView);
        // textView

        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
islikes(uploadCurrent.getKey(),holder.like);
nrLikes(holder.likes,uploadCurrent.getKey());
getComments(uploadCurrent.getKey(),holder.comments);
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
holder.like.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(holder.like.getTag().equals("Like")){
            FirebaseDatabase.getInstance().getReference().child("Likes").child(uploadCurrent.getKey()).child(firebaseUser.getUid()).setValue(true);
        }
        else
        {
            FirebaseDatabase.getInstance().getReference().child("Likes").child(uploadCurrent.getKey()).child(firebaseUser.getUid()).removeValue();

        }
    }
});


holder.comment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(mContext,Comments.class);
        intent.putExtra("postid",uploadCurrent.getKey());

        intent.putExtra("publisherid",boom);
        mContext.startActivity(intent);
    }
});
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,Comments.class);
                intent.putExtra("postid",uploadCurrent.getKey());

                intent.putExtra("publisherid",boom);
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName,likes,comments;
        public ImageView imageView,like,comment;


        public ImageViewHolder(View itemView) {
            super(itemView);

            like=itemView.findViewById(R.id.unfill);
comment=itemView.findViewById(R.id.comment);
comments=itemView.findViewById(R.id.comments);
            likes=itemView.findViewById(R.id.likes);
            textViewName = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.image_view_up);
            circleImageView=itemView.findViewById(R.id.upload_profile);
            textView=itemView.findViewById(R.id.nameupload);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    private void getComments(String postid,TextView comments)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("View All "+dataSnapshot.getChildrenCount() +" Comments");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void islikes(String postid,final ImageView imageView)
    {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.fillred);
                    imageView.setTag("Liked");
                }
                else {
                    imageView.setImageResource(R.drawable.unfillheart);
                    imageView.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrLikes(TextView likes,String postid)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
likes.setText(dataSnapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}