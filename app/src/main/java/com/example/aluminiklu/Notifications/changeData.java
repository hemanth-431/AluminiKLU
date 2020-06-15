package com.example.aluminiklu.Notifications;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aluminiklu.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class changeData extends AppCompatActivity {
    private TextInputEditText name,mail,pass,ge,bran,cour,spec;
    AutoCompleteTextView country;
    private ProgressDialog process;
    private ImageView save;
    private EditText num;
    private FirebaseUser fuser;
    private FirebaseDatabase fstore;
    private Task<Void> databaseReference;
    String user;
    private DatabaseReference database,database1;
    private FirebaseAuth mAuth;
    private Spinner branch,course,specialisation,date,date1;
    private static final String[] COUNTRIES = new String[]{
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola"
    };

    ArrayList<Integer> Entry1 = new ArrayList<Integer>();
    ArrayList<String>graduation1 = new ArrayList<String>();


    private static final String[] Branch1= new String[]{
            "CSE", "ECE", "BBA", "CIVIL", "MEC","None"
    };
    private static final String[] course1 = new String[]{
            "Faculty","B.Tech", "B.Arch", "M.B.A", "M.Tech", "Diploma"
    };
    private static final String[] specialization1 = new String[]{
            "ComputerNetworks", "I.O.T", "AI", "Ds", "BigData","None"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);

        int s= Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1990; i <= s; i++) {
            Entry1.add(i);
            graduation1.add(String.valueOf(i));
        }
        graduation1.add("On Going");
        mAuth = FirebaseAuth.getInstance();
        save = findViewById(R.id.save);
        num=findViewById(R.id.number);
        specialisation = findViewById(R.id.specialisation);

        process = new ProgressDialog(this);
        name = findViewById(R.id.NameUser);
        mail = findViewById(R.id.mail);
        fstore=FirebaseDatabase.getInstance();
        pass = findViewById(R.id.password);
        country = findViewById(R.id.actv);
        final Spinner spinner_house4 = (Spinner) findViewById(R.id.specialisation);
        final Spinner spinner_house = (Spinner) findViewById(R.id.date);
        final Spinner spinner_house1 = (Spinner) findViewById(R.id.date1);
        final Spinner spinner_house2 = (Spinner) findViewById(R.id.branch);
        final Spinner spinner_house3 = (Spinner) findViewById(R.id.course);
        final String ye = "", ge = "", bran = "", cour = "", special = "", name1 = "", mail1 = "", country1 = "",pass1="",number1="";


        String[] countries = getResources().getStringArray(R.array.countries);
        AutoCompleteTextView editText = findViewById(R.id.actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, COUNTRIES);
        editText.setAdapter(adapter);


        ArrayAdapter<String> adapter1 = new ArrayAdapter(changeData.this, android.R.layout.simple_list_item_1, specialization1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house4.setAdapter(adapter1);


        ArrayAdapter<String> adapter6 = new ArrayAdapter(changeData.this, android.R.layout.simple_list_item_1, Entry1);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter(changeData.this, android.R.layout.simple_list_item_1, graduation1);
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house1.setAdapter(adapter7);

        ArrayAdapter<String> adapter4 = new ArrayAdapter(changeData.this, android.R.layout.simple_list_item_1, Branch1);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house2.setAdapter(adapter4);


        ArrayAdapter<String> adapter5 = new ArrayAdapter(changeData.this, android.R.layout.simple_list_item_1, course1);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house3.setAdapter(adapter5);

save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (name.getText().toString() != null || mail.getText().toString() != null || pass.getText().toString()!=null || country.getText().toString() != null || spinner_house4.getSelectedItem().toString() != null || spinner_house3.getSelectedItem().toString() != null || spinner_house2.getSelectedItem().toString() != null || spinner_house1.getSelectedItem().toString() != null || spinner_house.getSelectedItem().toString() != null || num.getText().toString()!=null) {
            final String name1 = name.getText().toString();
            String mail1 = mail.getText().toString();

            String special = spinner_house4.getSelectedItem().toString();
            String country1 = country.getText().toString();
            String cour = spinner_house3.getSelectedItem().toString();
            String bran = spinner_house2.getSelectedItem().toString();
            String ge = spinner_house1.getSelectedItem().toString();
            String ye = spinner_house.getSelectedItem().toString();
            String number1=num.getText().toString();
            mAuth=FirebaseAuth.getInstance();
            String s=mAuth.getCurrentUser().getUid();

            databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(s).child("Branch").setValue(bran);
            if(number1.trim().length() == 10) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("Call").setValue(number1);
            }
            else {
                Toast.makeText(changeData.this,"please enter a valid number...",Toast.LENGTH_SHORT).show();
            }


            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("Country").setValue(country1);
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("Course").setValue(cour);
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("FullName").setValue(name1);
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("Graduation Year").setValue(ge);
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("Join Date").setValue(ye);
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("MailId").setValue(mail1);
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(s).child("Specialization").setValue(special);



            Toast.makeText(changeData.this,s,Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(changeData.this,"Please fill all the details...",Toast.LENGTH_SHORT).show();
        }






    }
});

     //   databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("Branch").setValue(s);
     //   fuser= FirebaseAuth.getInstance().getCurrentUser();
    }
}