package com.example.aluminiklu;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class image_adapter extends RecyclerView.Adapter<image_adapter.ImageViewHolder> {
private TextView textView;
    private Context mContext;
    private List<upload> mUploads;
    private CircleImageView circleImageView;
    private OnItemClickListener mListener;
    private FirebaseAuth firebaseAuth;
    String boom=null;
    public   ImageView unfill,fill;
    public    String check="unfill";
    public   TextView like;
    public int count=0;
    private Handler myhandler;
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
        holder.textViewName.setText(uploadCurrent.getName());
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
           /*     textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     String s=boom;
                        Toast.makeText(v.getContext(),s,Toast.LENGTH_LONG).show();
                    }
                });

            */

                unfill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            if(check.equals("unfill") && position==1)
                            {count++;
                                animateHeart(fill);
                               fill.setVisibility(View.VISIBLE);
                                unfill.setVisibility(View.GONE);
                            }
                            String s=Integer.toString(count);
                            like.setText(s);
                            String pos=Integer.toString(position);
                            Snackbar.make(v,pos,Snackbar.LENGTH_LONG).setAction("Action",null).show();
                           // check="fill";

                    }
                });


          /*    fill.setOnClickListener(new View.OnClickListener() {
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



    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;


        public ImageViewHolder(View itemView) {
            super(itemView);

            unfill=itemView.findViewById(R.id.unfill);
            fill=itemView.findViewById(R.id.filled);
            like=itemView.findViewById(R.id.likes);
            textViewName = itemView.findViewById(R.id.textview_name);
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




    private static Animation prepareAnimationn(Animation animation){
        animation.setRepeatCount(0);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    private static void dislikeheart(final ImageView view){
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f
                ,Animation.RELATIVE_TO_SELF,0.5f);
        prepareAnimationn(scaleAnimation);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,1.0f);
        prepareAnimationn(alphaAnimation);
        AnimationSet animation=new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(700);
        view.startAnimation(animation);


    }

    public static void animateHeart(final ImageView view)
    {
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        prepareAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,1.0f);
        prepareAnimation(alphaAnimation);

        AnimationSet animation=new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    public static Animation prepareAnimation(Animation animation)
    {
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }


}