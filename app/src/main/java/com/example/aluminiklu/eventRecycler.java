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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Model.Artist;
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
import java.util.Collections;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class eventRecycler extends Fragment {
    private RecyclerView recyclerView;
    private ArtistsAdapter adapter;
    private List<Artist> artistList;
    Button ok,close;
    String user;
    private FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    DatabaseReference databaseReference,getDatabaseReference;
    EditText name,setdate,link,description;
    DatabaseReference dbArtists;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view=inflater.inflate(R.layout.activity_event_recycler,container,false);
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton floatingActionButton=view.findViewById(R.id.add_event);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        artistList = new ArrayList<>();

        adapter = new ArtistsAdapter(getActivity(), artistList);
        recyclerView.setAdapter(adapter);

        //1. SELECT * FROM Artists
        dbArtists = FirebaseDatabase.getInstance().getReference("Events");
dbArtists.addListenerForSingleValueEvent(valueEventListener);
        //2. SELECT * FROM Artists WHERE id = "-LAJ7xKNj4UdBjaYr8Ju"
 /*       Query query = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("id")
                .equalTo("-LAJ7xKNj4UdBjaYr8Ju");

        //3. SELECT * FROM Artists WHERE country = "India"
        Query query3 = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("country")
                .equalTo("India");

        //4. SELECT * FROM Artists LIMIT 2
        Query query4 = FirebaseDatabase.getInstance().getReference("Artists").limitToFirst(2);


        //5. SELECT * FROM Artists WHERE age < 30
        Query query5 = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("age")
                .endAt(29);


        //6. SELECT * FROM Artists WHERE name = "A%"
        Query query6 = FirebaseDatabase.getInstance().getReference("Artists")
                .orderByChild("name")
                .startAt("A")
                .endAt("A\uf8ff");

        ;

  */
        /*
         * You just need to attach the value event listener to read the values
         * for example
         * query6.addListenerForSingleValueEvent(valueEventListener)
         *
         * */
        return view;
    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            artistList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Artist artist = snapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                Collections.reverse(artistList);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void showDialog() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(getActivity());
        }
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.event_data, null);
        name = view.findViewById(R.id.edit_text);
        // fstore=FirebaseDatabase.getInstance();
//databaseReference=fstore.getReference("Events");
        ok = view.findViewById(R.id.ok);
        close = view.findViewById(R.id.cancel);
        link = view.findViewById(R.id.link);
        description = view.findViewById(R.id.edit_description);
        setdate = view.findViewById(R.id.edit_date);

        setdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener, year, month, day);
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
                databaseReference = FirebaseDatabase.getInstance().getReference("Events");
                user = mAuth.getCurrentUser().getUid();
                String path = databaseReference.push().getKey();

                String des = description.getText().toString();
                if (des.trim().length() < 30) {
                    Toast.makeText(getActivity(), "description must contain atleast 30 characters...", Toast.LENGTH_LONG).show();
                } else {
                    events_data helperclass = new events_data(name.getText().toString(), setdate.getText().toString(), link.getText().toString(), user, des, path);
                    databaseReference.child(path).setValue(helperclass);


                    String s = name.getText().toString() + " " + setdate.getText().toString() + " " + link.getText().toString();
                    Toast.makeText(getActivity(), s + " " + path, Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = month + "/" + dayOfMonth + "/" + year;
                setdate.setText(date);
            }
        };
    }

}



