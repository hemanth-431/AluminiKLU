package com.example.aluminiklu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class GroupAdb extends RecyclerView.Adapter<GroupAdb.ViewHolder> {
    private Activity activity;
    ArrayList<String> arrayListGroup;
    GroupAdb(Activity activity,ArrayList<String> arrayListGroup){
        this.activity=activity;
        this.arrayListGroup=arrayListGroup;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_group,parent,false);

        return new GroupAdb.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//set Group name on Text View
        holder.tvName.setText(arrayListGroup.get(position));
        //Initialize member ArrayList
        ArrayList<String> arrayListMember=new ArrayList<>();

        for (int i=1;i<=4;i++)
        {
            arrayListMember.add("Member "+i);
        }
        //initialize member adapter
        MemberAdb adapterMember=new MemberAdb(arrayListMember);
        //initialize LayOut Manager
        LinearLayoutManager layoutManagerMember=new LinearLayoutManager(activity);
        //setLayOutManager
        holder.rvMember.setLayoutManager(layoutManagerMember);
        //set Adapter
        holder.rvMember.setAdapter(adapterMember);
    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//initialize variable
        TextView tvName;
        RecyclerView rvMember;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //assain variables
            tvName=itemView.findViewById(R.id.tv_name);
            rvMember=itemView.findViewById(R.id.rv_member);

        }
    }
}
