package com.example.aluminiklu;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Events extends Fragment {
RecyclerView rvGroup;
EditText name,date,link;
ArrayList<String> arrayListGroup;
LinearLayoutManager layoutManager;
GroupAdb adapterGroup;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_events,container,false);
FloatingActionButton floatingActionButton=view.findViewById(R.id.add_event);
floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       showDialog();
    }
});

        rvGroup=view.findViewById(R.id.rv_group);
        arrayListGroup=new ArrayList<>();
        for (int i=0;i<=10;i++){
            arrayListGroup.add("Group "+i);
        }
        //initialize group adapter

        adapterGroup=new GroupAdb(getActivity(),arrayListGroup);
        //initialize layout manager
        layoutManager =new LinearLayoutManager(getActivity());

        rvGroup.setLayoutManager(layoutManager);
        rvGroup.setAdapter(adapterGroup);

        return view;
    }
    private void showDialog(){
        AlertDialog.Builder alert;
if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
alert=new AlertDialog.Builder(getActivity(),android.R.style.Theme_Material_Dialog_Alert);
}else {
    alert=new AlertDialog.Builder(getActivity());
}
LayoutInflater inflater=getLayoutInflater();
View view=inflater.inflate(R.layout.event_data,null);

    }
}
