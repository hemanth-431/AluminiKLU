package com.example.aluminiklu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MemberAdb extends RecyclerView.Adapter<MemberAdb.ViewHolder> {
    ArrayList<String> arrayListMember;
    public MemberAdb(ArrayList<String> arrayListMember){
        this.arrayListMember=arrayListMember;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//Intialize View
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_member,parent,false);

        return new MemberAdb.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.tvNmae.setText(arrayListMember.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayListMember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
TextView tvNmae;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNmae=itemView.findViewById(R.id.tv_name);

        }

    }

}
