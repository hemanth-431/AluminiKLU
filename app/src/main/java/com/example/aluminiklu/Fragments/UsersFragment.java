package com.example.aluminiklu.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aluminiklu.Adapter.UserAdapter;
import com.example.aluminiklu.Model.user;
import com.example.aluminiklu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class UsersFragment extends Fragment  {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    ImageView up,down;
private ProgressBar progressBar;
    private DatabaseReference database;
    private Spinner branch,date;
    ArrayList<String> Entry1 = new ArrayList<>();
    int count=0;
    private static final String[] Branch1= new String[]{
       "Select","All","CSE", "ECE", "BBA", "CIVIL", "MEC","None"
    };
    TextView searchby;
    EditText search_users;
private HashSet<user> hashSet;
    private List<user> musers;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_users,container,false);
        recyclerView=view.findViewById(R.id.recycler);
        //   up=view.findViewById(R.id.up);
        //  down=view.findViewById(R.id.down);
        branch=view.findViewById(R.id.branch);
        progressBar = view.findViewById(R.id.progress_circle);
        progressBar.setVisibility(View.VISIBLE);
        date=view.findViewById(R.id.date);
        Spinner date=view.findViewById(R.id.date);
        Spinner branch=view.findViewById(R.id.branch);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hashSet=new HashSet<>();
        musers=new ArrayList<>();
        readusers(0);
        int s= Calendar.getInstance().get(Calendar.YEAR);
        Entry1.add("Select");
        Entry1.add("All");
        for (int i = 1990; i <= s; i++) {
            Entry1.add(String.valueOf(i));

        }
        ArrayAdapter<String> adapter6 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Entry1);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date.setAdapter(adapter6);
        ArrayAdapter<String> adapter4 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Branch1);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        branch.setAdapter(adapter4);

        //  date.setOnTouchListener(new View.OnTouchListener() {
        //        @Override
        //       public boolean onTouch(View v, MotionEvent event) {
        //           Toast.makeText(getActivity(),date.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
        //           return false;
        //       }
        //    });



    branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int item = branch.getSelectedItemPosition();
            String y = Branch1[item].toString();
           //   musers.clear();
            //    if(!y.equals("All"))

            readusers(1);
            System.out.println(musers.size()+"---------------------");
        ///    Toast.makeText(getActivity(), y, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });

    date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //    musers.clear();

            int item = date.getSelectedItemPosition();
            String y = Entry1.get(item).toString();
               if(!y.equals("All"))
          //  musers.clear();

            readusers(1);
            System.out.println(musers.size()+"------------------------------");
            //   Toast.makeText(getActivity(), y, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });


        //   String bran = branch.getSelectedItem().toString();
        //    String year = date.getSelectedItem().toString();

//searchby=view.findViewById(R.id.text_view);
//registerForContextMenu(searchby);
        /*
searchby.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        up.setVisibility(View.INVISIBLE);
        down.setVisibility(View.VISIBLE);
        PopupMenu popupMenu=new PopupMenu(getActivity(),v);
        SubMenu menu4 = popupMenu.getMenu().addSubMenu(Menu.NONE, 2, 4,"Year of entry");
        menu4.add(6, 2, 1, "All");
        menu4.add(6, 3, 2, "SubMenu No. 1");
        menu4.add(6, 4, 3, "SubMenu No. 2");
        menu4.setGroupCheckable(6,true,true);
        popupMenu.getMenuInflater().inflate(R.menu.searchmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                switch (i){
                    case 2:
                        Toast.makeText(getActivity(),"All",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(),"SubMenu1",Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(),"SubMenu2",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.option_1:
                        Toast.makeText(getActivity(),"one",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
});
*/

        search_users=view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchusers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }



    public void searchusers(String s)
    {
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("Users1").orderByChild("search")//.orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                hashSet.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    user user=dataSnapshot1.getValue(com.example.aluminiklu.Model.user.class);
                    assert fuser != null;
                    assert user != null;


                    if(!user.getId().equals(fuser.getUid())){    /////
                        musers.add(user);

                    }

                }


                HashSet<user> hashSet1=new HashSet<>(musers);
                ArrayList<user> namesList = new ArrayList<>(hashSet1);
                userAdapter=new UserAdapter(getContext(),namesList,false);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
    private void readusers(int i)
    {
        count=1;
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    musers.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        user user = dataSnapshot1.getValue(com.example.aluminiklu.Model.user.class);
                        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(user.getId());
 reference1.addValueEventListener(new ValueEventListener() {
     @Override
     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
         String Year1="",Branch1="";
try{        Branch1=dataSnapshot.child("Branch").getValue().toString();
          Year1=dataSnapshot.child("Join Date").getValue().toString();}catch (Exception e){}
         String branch1=branch.getSelectedItem().toString();
         String date1=date.getSelectedItem().toString();

         assert user != null;


         try {
             if(branch1.equals("All") && date1.equals("All")) {

                 if (!user.getId().equals(firebaseUser.getUid())) {
                     musers.add(user);


                 }
             }else if(branch1.equals("Select") && date1.equals("All")) {

                 if (!user.getId().equals(firebaseUser.getUid())) {
                     musers.add(user);


                 }
             }else  if(branch1.equals("All") && date1.equals("Select")) {
                 if (!user.getId().equals(firebaseUser.getUid())) {
                     musers.add(user);


                 }
             }
             else
              if(branch1.equals(Branch1) && (date1.equals("All") || date1.equals("Select"))){
                 if (!user.getId().equals(firebaseUser.getUid())) {
                     musers.add(user);



                 }
             }else if(date1.equals(Year1) && (branch1.equals("All") || branch1.equals("Select"))){
                 if (!user.getId().equals(firebaseUser.getUid())) {
                     musers.add(user);


                 }
             }else if(date1.equals(Year1) && branch1.equals(Branch1)){
                 if (!user.getId().equals(firebaseUser.getUid())) {
                     musers.add(user);


                 }
             }
             else {
                 if(i==0){


                             if ((!user.getId().equals(firebaseUser.getUid()))) {
                                 musers.add(user);
                               //  Toast.makeText(getActivity(),String.valueOf(sq),Toast.LENGTH_LONG).show();


                             }



                 }

             }
         }catch (Exception e){

         }

         userAdapter = new UserAdapter(getContext(), musers,false);
         recyclerView.setAdapter(userAdapter);
         progressBar.setVisibility(View.GONE);
     }

     @Override
     public void onCancelled(@NonNull DatabaseError databaseError) {

     }

 });



                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}