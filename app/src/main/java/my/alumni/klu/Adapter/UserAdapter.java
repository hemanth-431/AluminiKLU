package my.alumni.klu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import my.alumni.klu.MessageActivity;
import my.alumni.klu.Model.chat;
import my.alumni.klu.Model.user;
import my.alumni.klu.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mcontext;
    private List<user> musers;
    FirebaseUser firebaseUser;
    private boolean ischat;
    String thelastmessage;
    public UserAdapter(Context mcontext,List<user> musers,boolean ischat)
    {
        this.mcontext=mcontext;
        this.musers=musers;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
final user user=musers.get(position);


holder.name.setText(user.getUsername());
    try {
        if (user.getImageUrl().equals("default")) {
            holder.ImageUrl.setImageResource(R.mipmap.ic_launcher);

        } else {
            Picasso.with(mcontext).load(user.getImageUrl()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.ImageUrl, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mcontext).load(user.getImageUrl()).into(holder.ImageUrl);
                }
            });
          //  holder.ImageUrl.setImageURI(Uri.parse(user.getImageUrl()));
        }
    }
    catch (Exception e){

    }




       if(ischat){
            lastMessage(user.getId(),holder.last_msg);
        }else {
            holder.last_msg.setVisibility(View.GONE);
        }

        if(ischat){
            if(user.getStatus().equals("online")){
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            }else {
                holder.image_off.setVisibility(View.VISIBLE);
                holder.image_on.setVisibility(View.GONE);
            }
        }else {
            holder.image_on.setVisibility(View.GONE);
            holder.image_off.setVisibility(View.GONE);
        }


       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mcontext, MessageActivity.class);
                i.putExtra("userid",user.getId());

                mcontext.startActivity(i);
            }
        });
/*if(user.getMailId().equals("default")){

}
else {
    holder.ImageUrl.setText(musers.get);
}*/
    }

    @Override
    public int getItemCount() {
        return musers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView ImageUrl;
        private ImageView image_on;
        private ImageView image_off;
        private TextView last_msg;
private Button infor;
        public ViewHolder(View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.username);
            ImageUrl=itemView.findViewById(R.id.profile_image);
            image_on=itemView.findViewById(R.id.img_on);
            image_off=itemView.findViewById(R.id.img_off);
            last_msg=itemView.findViewById(R.id.last_msg);
          //  infor=itemView.findViewById(R.id.info);
        }
    }
   private void lastMessage(final String userid, final TextView last_msg){
thelastmessage="default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    chat chat = snapshot.getValue(my.alumni.klu.Model.chat.class);
                    try {


                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            thelastmessage = chat.getMessage();
                        }
                    }
                    catch (Exception e){

                    }
                }
                    switch (thelastmessage){
                        case "default":
                            last_msg.setText("No Message");
                            break;
                        default:
                            last_msg.setText(thelastmessage);
                            break;
                    }
                    thelastmessage="default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

   });
}}


