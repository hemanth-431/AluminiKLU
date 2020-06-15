package com.example.aluminiklu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Model.events_data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Events extends Fragment {
RecyclerView rvGroup;
String user;
ValueEventListener dblistner;
EditText name,setdate,link,description;
Button ok,close;
private List<events_data> mUploads;
ArrayList<String> arrayListGroup,soap;
DatabaseReference databaseReference,getDatabaseReference;
LinearLayoutManager layoutManager;

private FirebaseDatabase fstore;
    private FirebaseAuth mAuth;
private DatePickerDialog.OnDateSetListener onDateSetListener;
GroupAdb adapterGroup;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view=inflater.inflate(R.layout.activity_events,container,false);
        mAuth = FirebaseAuth.getInstance();

FloatingActionButton floatingActionButton=view.findViewById(R.id.add_event);
floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       showDialog();
    }
});


        rvGroup=view.findViewById(R.id.rv_group);
        arrayListGroup=new ArrayList<>();

        mUploads = new ArrayList<>();//////////////////////////
        getDatabaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        getDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        events_data upload = postSnapshot.getValue(events_data.class);
                        upload.setName(postSnapshot.getKey());
                        mUploads.add(upload);
                      //  soap.add(postSnapshot.getKey().toString());

                        //  Toast.makeText(ImagesActivity.this,s.get(0).toString(),Toast.LENGTH_LONG).show();




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

int len=0;
        try{
            len=soap.size();}
        catch (Exception e){

        }
        for (int i=0;i<=10;i++){
            arrayListGroup.add(soap.get(0)+"oops"+i);
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
        name=view.findViewById(R.id.edit_text);
       // fstore=FirebaseDatabase.getInstance();
//databaseReference=fstore.getReference("Events");
        ok=view.findViewById(R.id.ok);
        close=view.findViewById(R.id.cancel);
        link=view.findViewById(R.id.link);
        description=view.findViewById(R.id.edit_description);
        setdate=view.findViewById(R.id.edit_date);

        setdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();

                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        setdate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date, 0);
alert.setView(view);
alert.setCancelable(false);

ok.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        databaseReference= FirebaseDatabase.getInstance().getReference("Events");
        user=mAuth.getCurrentUser().getUid();
       String path =databaseReference.push().getKey();

String des=description.getText().toString();
if(des.trim().length()<30){
    Toast.makeText(getActivity(),"description must contain atleast 30 characters...",Toast.LENGTH_LONG).show();
}else {
    events_data helperclass = new events_data(name.getText().toString(), setdate.getText().toString(), link.getText().toString(), user,des,path);
    databaseReference.child(path).setValue(helperclass);


    String s = name.getText().toString() + " " + setdate.getText().toString() + " " + link.getText().toString();
    Toast.makeText(getActivity(), s + " " + path, Toast.LENGTH_SHORT).show();
}
    }
});
AlertDialog dialog=alert.create();
dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
onDateSetListener=new DatePickerDialog.OnDateSetListener(){
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
month=month+1;
        Log.d(TAG,"onDateSet: mm/dd/yyy: "+ month + "/" + dayOfMonth + "/" + year);
        String date=month+"/"+dayOfMonth+"/"+year;
        setdate.setText(date);
    }
};
    }
}
/*

 */