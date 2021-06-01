

package com.example.aluminiklu;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class image_adapter extends RecyclerView.Adapter<image_adapter.ImageViewHolder> implements Filterable {

    private Context mContext;
    private List<upload> mUploads;
    private List<upload> getmUploads;
    private OnItemClickListener mListener;
    private FirebaseAuth firebaseAuth;
    String boom=null;
public int len=0;
    public    String check="unfill";

    public int count=0;

    private DatabaseReference databaseReference,getnam;
    public image_adapter(Context context, List<upload> uploads) {


        mContext = context;
        mUploads = uploads;




    }

    void  image_adapter1(List<upload> uploads){
        getmUploads=new ArrayList<>(uploads);
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
        databaseReference= FirebaseDatabase.getInstance().getReference().child("uploads").child(uploadCurrent.getKey());//*****
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                String time = currentDateandTime;
                String name = "no name";
                String desc = "no";
                try {

                    time = dataSnapshot.child("mtime").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    desc = dataSnapshot.child("mData").getValue().toString();
                } catch (Exception e) {

                }
                try {
                    holder.time.setText(time);
              //      holder.nameofpic.setText(name);
                } catch (Exception e) {

                }
                if (desc.length() == 0) {
                    holder.descrip.setVisibility(View.INVISIBLE);
                    holder.descrip.getLayoutParams().height = 0;
                }
                    holder.descrip.setText(desc);

                /////////////////////////////////////////////////////////////////////////////////////////
                try {
                    synchronized (this) {
                        wait(300);
                    }
                } catch (InterruptedException ex) {
                }

                try {
                    boom = dataSnapshot.child("key").getValue().toString();   /////////////////////////////error
                } catch (Exception e) {


                }
                try {
                    synchronized (this) {
                        wait(300);
                    }
                } catch (InterruptedException ex) {
                }
////////////////////////////////////////////////////////////////////////////////////////////////////


            /*    holder.nameofpic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), boom, Toast.LENGTH_LONG).show();
                    }
                });

             */



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
                        holder.textView.setText(data1);
                        String data2 = dataSnapshot.child("imageUrl").getValue().toString();
                        Picasso.with(mContext).load(data2).networkPolicy(NetworkPolicy.OFFLINE).into(holder.circleImageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
Picasso.with(mContext).load(data2).into(holder.circleImageView);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, personal_info.class);
                        intent.putExtra("STRING_I_NEED", boom);
                        mContext.startActivity(intent);

                        int len = getmUploads.size();
                        String s22 = String.valueOf(len);
                        if (s22 == null) {
                            Toast.makeText(v.getContext(), "No", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(v.getContext(), s22, Toast.LENGTH_LONG).show();
                        }

                    }

                });

                holder.circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), boom, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(mContext, personal_info.class);
                        intent.putExtra("STRING_I_NEED", boom);
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
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(uploadCurrent.getImageUrl())
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(holder.imageView);
                    }
                });


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


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<upload> filteredList=new ArrayList<>();
            if(constraint == null || constraint.length() ==0){
                filteredList.addAll(getmUploads);
            }
            else {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for (upload item: getmUploads){
                    if(item.getmSNme().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
                 len=filteredList.size();

            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }
public int count(){
            return len;
}
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUploads.clear();
            mUploads.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };





    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName,likes,comments,time,nameofpic,descrip;
        public ImageView imageView,like,comment,notfound;
        public CircleImageView circleImageView;
        private TextView textView;
        public ImageViewHolder(View itemView) {
            super(itemView);

            like=itemView.findViewById(R.id.unfill);
            comment=itemView.findViewById(R.id.comment);
            comments=itemView.findViewById(R.id.comments);
            likes=itemView.findViewById(R.id.likes);
            time=itemView.findViewById(R.id.time);
         //   nameofpic=itemView.findViewById(R.id.name_of_profile);
            descrip=itemView.findViewById(R.id.paragraph1);
            textViewName = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.image_view_up);
            circleImageView=itemView.findViewById(R.id.upload_profile);
            textView=itemView.findViewById(R.id.nameupload);           //     -------------------------
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
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Share");
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
                comments.setText("view all "+dataSnapshot.getChildrenCount() +" comments");

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
                try {

                    if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                        imageView.setImageResource(R.drawable.fillred);
                        imageView.setTag("Liked");
                    } else {
                        imageView.setImageResource(R.drawable.heartunfill);
                        imageView.setTag("Like");
                    }

                } catch (Exception e) {

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