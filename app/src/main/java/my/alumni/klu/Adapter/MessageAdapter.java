package my.alumni.klu.Adapter;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import my.alumni.klu.Model.chat;
import my.alumni.klu.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
private Context mcontext;
private List<chat> mchat;
private OnitemclickListener mlistener;
private String imageurl;
FirebaseUser firebaseUser;
private DatabaseReference databaseReference;
public static final int MSG_TYPE_LEFT=0;
public static final int MSG_TYPE_RIGHT=1;
public MessageAdapter(Context mcontext,List<chat> mchat,String imageurl)
        {
        this.mcontext=mcontext;
        this.mchat=mchat;
        this.imageurl=imageurl;
        }

@NonNull
@Override
public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if(viewType==MSG_TYPE_RIGHT) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);

        return new MessageAdapter.ViewHolder(view);
    }
    else {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);

        return new MessageAdapter.ViewHolder(view);
    }
        }




    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
chat chat=mchat.get(position);

////////////////////////////////////////////////////////



/*
holder.show_message.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       // String s=Integer.toString(position);
        chat selectedItem = mchat.get(position);
        final String selectedKey = selectedItem.getIdkey();
        try {
            onDeleteClick(selectedKey);
        }catch (Exception e){

        }
        Toast.makeText(v.getContext(),selectedKey,Toast.LENGTH_LONG).show();
    }
});
*/
//////////////////////////////
try{holder.show_message.setText(chat.getMessage());

}
catch (Exception e)
{
   // Toast.makeText(MessageAdapter.this,"nbmkn",Toast.LENGTH_LONG).show();
}



if(imageurl!=null)
if(imageurl.equals("default")){
    holder.ImageUrl.setImageResource(R.mipmap.ic_launcher);

}else {
    Picasso.with(mcontext).load(imageurl).into(holder.ImageUrl);
    
}
if(position==mchat.size()-1){
    if(chat.isIsseen()){
        holder.txt_seen.setText("Seen");
    }else {
        holder.txt_seen.setText("Delivered");
    }
}
else {
    holder.txt_seen.setVisibility(View.GONE);
}
        }

@Override
public int getItemCount() {
        return mchat.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
    public TextView show_message;
    public ImageView ImageUrl;
    public TextView txt_seen;

    public ViewHolder(View itemView)
    {
        super(itemView);
        show_message=itemView.findViewById(R.id.show_message);
        ImageUrl=itemView.findViewById(R.id.profile_image);
        txt_seen=itemView.findViewById(R.id.txt_seen);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mlistener !=null){
            int position=getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
               //mlistener.onDeleteClick(position);
                String s=Integer.toString(position);
//                Toast.makeText(v.getContext(),s,Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem delete=menu.add(Menu.NONE,1,1,"remove");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                    int position=getAdapterPosition();


                        switch (item.getItemId())
                        {
                            case 1:
                                chat selectedItem = mchat.get(position);
                                final String selectedKey = selectedItem.getIdkey();
                                try {
                                    onDeleteClick(selectedKey);
                                }catch (Exception e){

                                }
//                                Toast.makeText(mcontext, position+"", Toast.LENGTH_SHORT).show();
//                                onDeleteClick(String.valueOf(position));
                                return true;
                        }


                return false;
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (mlistener !=null){
            int position=getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
               switch (item.getItemId())
               {
                   case 1:
                       mlistener.onDeleteClick(position);
                       return true;
               }
            }
        }
        return false;
    }
}

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }


    public void onDeleteClick(String Select) {
databaseReference= FirebaseDatabase.getInstance().getReference().child("Chats");
        databaseReference.child(Select).removeValue();

    }



    public interface OnitemclickListener{
    void onDeleteClick(int position);

    }
    public void setOnitemclickListener(OnitemclickListener listener){
mlistener=listener;
    }
}