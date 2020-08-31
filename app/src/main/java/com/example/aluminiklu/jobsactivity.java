package com.example.aluminiklu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Fragments.APIServer;
import com.example.aluminiklu.Model.jobs;
import com.example.aluminiklu.Notifications.Client;
import com.example.aluminiklu.Notifications.MyResponse;
import com.example.aluminiklu.Notifications.Sender;
import com.example.aluminiklu.Notifications.data;
import com.example.aluminiklu.Notifications.token;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.aluminiklu.App.CHANNEL_1_ID;
import static com.example.aluminiklu.App.CHANNEL_2_ID;

public class jobsactivity extends Fragment {

    private RecyclerView recyclerView;
    private jobsAdapter adapter;
    private List<jobs> artistList;
    Button ok,close;
    ImageView notf;
    String user;
    APIServer apiService;
    private FirebaseUser fuser;
    APIServer services;
    private ProgressBar mProgressCircle;
    private FirebaseAuth mAuth;
    private NotificationManagerCompat notificationManager;

    String tkn;
    List<String> listView=new ArrayList<>();
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    DatabaseReference databaseReference,getDatabaseReference;
    EditText name,link,description,jobtype;

    TextView setdate12;
    ImageView cal;
    boolean notify=false;
    DatabaseReference dbArtists,reference,mreference ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        tkn= FirebaseInstanceId.getInstance().getToken();
        notificationManager= NotificationManagerCompat.from(getContext());
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIServer.class);
        View view=inflater.inflate(R.layout.activity_jobsactivity,container,false);
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton floatingActionButton=view.findViewById(R.id.add_event);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                //     new Notify().execute();
                //       sendOnChannel1(v);
                //notify=true;
                UpdateToken();
                int leng=adapter.getItemNo();

                //      String sop=String.valueOf(leng);
                //       Toast.makeText(getActivity(),sop,Toast.LENGTH_LONG).show();
            }

            private void UpdateToken(){

                String refreshToken= FirebaseInstanceId.getInstance().getToken();
                token token= new token(refreshToken);
                FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
            }
        });
        //    apiService= Client.getClient("https://fcm.googleapis.com/").create(APIServer.class);
        mProgressCircle = view.findViewById(R.id.progress_circle1);
        notf=view.findViewById(R.id.notfor);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        artistList = new ArrayList<>();
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        adapter = new jobsAdapter(getActivity(), artistList);

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                showDialog1(viewHolder);

            }

            void showDialog1(RecyclerView.ViewHolder viewHolder){

                LayoutInflater inflater=LayoutInflater.from(getActivity());
                View view=inflater.inflate(R.layout.alert_dialog1,null);
                AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).setView(view).create();
                ImageView accept=view.findViewById(R.id.accept);

                TextView textView=view.findViewById(R.id.cancel);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                        adapter.deleteItem(viewHolder.getAdapterPosition());
                        dbArtists = FirebaseDatabase.getInstance().getReference("Jobs");
                        dbArtists.addListenerForSingleValueEvent(valueEventListener);

                    }
                });
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        dbArtists = FirebaseDatabase.getInstance().getReference("Jobs");
                        dbArtists.addListenerForSingleValueEvent(valueEventListener);
                    }
                });
                alertDialog.show();
            }
        }).attachToRecyclerView(recyclerView);


        dbArtists = FirebaseDatabase.getInstance().getReference("Jobs");
        dbArtists.addListenerForSingleValueEvent(valueEventListener);

        return view;

    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            artistList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    jobs artist = snapshot.getValue(jobs.class);
                    artistList.add(artist);
                }

                Collections.reverse(artistList);
                adapter.artist1234(artistList);
                adapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }else {
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            mProgressCircle.setVisibility(View.INVISIBLE);
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

        View view = inflater.inflate(R.layout.jobs_data, null);
        name = view.findViewById(R.id.edit_text);
        jobtype=view.findViewById(R.id.jobtyp);
        // fstore=FirebaseDatabase.getInstance();
//databaseReference=fstore.getReference("Jobs");
        ok = view.findViewById(R.id.ok);
        close = view.findViewById(R.id.cancel);
        link = view.findViewById(R.id.link);
        description = view.findViewById(R.id.edit_description);
        setdate12 = view.findViewById(R.id.edit_date);
        cal=view.findViewById(R.id.calen);
        cal.setOnClickListener(new View.OnClickListener() {
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
        setdate12.setOnClickListener(new View.OnClickListener() {
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


        alert.setView(view);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   new Notify().execute();
                notify=true;
                databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");
                user = mAuth.getCurrentUser().getUid();
                String path = databaseReference.push().getKey();
                String url=link.getText().toString();

                String des = description.getText().toString();
                if (des.trim().length() < 30) {
                    Toast.makeText(getActivity(), "description must contain atleast 30 characters...", Toast.LENGTH_LONG).show();
                } else if (url != null && (url.startsWith("http://") || url.startsWith("https://")))
                {
                    jobs helperclass = new jobs(name.getText().toString(),jobtype.getText().toString(), setdate12.getText().toString(), link.getText().toString(), user, des, path);
                    String s = name.getText().toString() + " " + setdate12.getText().toString() + " " + link.getText().toString();



                    databaseReference.child(path).setValue(helperclass);



                    Toast.makeText(getActivity(), s + " " + path, Toast.LENGTH_SHORT).show();


                    dialog.dismiss();
                    dbArtists = FirebaseDatabase.getInstance().getReference("Jobs");
                    dbArtists.addListenerForSingleValueEvent(valueEventListener);


                    final String hem="New Event Added";
                    reference = FirebaseDatabase.getInstance().getReference("Users1").child(fuser.getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            com.example.aluminiklu.Model.user user=dataSnapshot.getValue(com.example.aluminiklu.Model.user.class);

                            mreference=FirebaseDatabase.getInstance().getReference().child("Users1");
                            mreference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        // System.out.println(snapshot.getKey()+"****(*)");
                                        String user1=fuser.getUid();
                                        if(snapshot.getKey().equals(user1))
                                        { System.out.println(snapshot.getKey()+"------------------------"+user1);
                                        }
                                        else
                                        {
                                            System.out.println(snapshot.getKey()+"*************************"+user1);
                                            if(notify) {
                                                sendNotification1(snapshot.getKey().toString(), user.getUsername(), hem);
                                            }
                                        }
                                    }
                                    notify=false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                                private void sendNotification1(String receiver,String username,String message){
                                    DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
                                    Query query=tokens.orderByKey().equalTo(receiver);
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {
                                                token token1 = dataSnapshot1.getValue(token.class);


                                                data data1 = new data(fuser.getUid(), R.mipmap.ic_launcher, username + ": " + message, "New Message", receiver);
                                                Sender sender = new Sender(data1, token1.getToken());
                                                apiService.sendNotification(sender)
                                                        .enqueue(new Callback<MyResponse>() {
                                                            @Override
                                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                                if (response.code() == 200) {
                                                                    if (response.body().success != 1) {
                                                                        System.out.println(response.body().success);
                                                                        try{  Toast.makeText(getActivity(), "Failed not!", Toast.LENGTH_LONG).show();}catch(Exception e){

                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<MyResponse> call, Throwable t) {

                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            //sendNotification("3RUoJsr2KUhuA1Uk3HF7xXA3wEZ2", user.getUsername(), hem);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(),"Please enter a valid link",Toast.LENGTH_LONG).show();
                }

            }


        });

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
                setdate12.setText(date);
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu1, menu);
        MenuItem item=menu.findItem(R.id.action_search);

        SearchView searchView=(SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                adapter.getFilter().filter(newText);
                int l=adapter.getItemCount();
                System.out.println(l);
                if(l == 0)
                {
                    notf.setVisibility(View.VISIBLE);
                }else {
                    notf.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }


    public class Notify extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization","key=AAAAuUHW_fQ:APA91bFgHsf97Vzoi8CW5QpAC0I9AQ94Fvog-NcVOgYq6xX7luEJrp4At6OKuba1BTtDFryVKxwcw9-Ej4PC5MhXorMbD_JQsX9BLQVC9kVzPfhNR7t-krn4nmLcYEJwAZWZ_8-n2ndh");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();

                json.put("to", tkn);


                JSONObject info = new JSONObject();
                info.put("title", "TechnoWeb");   // Notification title
                info.put("body", "Hello Test notification"); // Notification body

                json.put("notification", info);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.getInputStream();

            }catch (Exception e){
                Log.d("Error",""+e);
            }

            return null;
        }
    }

    public void sendOnChannel1(View v) {
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_mail1)
                .setContentTitle("title")
                .setContentText("message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
    public void sendOnChannel2(View v) {
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("titlename")
                .setContentText("messagename")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(2, notification);
    }

    public void browse(String url)
    {
        Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

}
