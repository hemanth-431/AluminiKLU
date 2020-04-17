package com.example.aluminiklu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText name,mail,pass,ge,bran,cour,spec;
    AutoCompleteTextView country;
    private ProgressDialog process;
    private Button button;
    private FirebaseDatabase fstore;
    String user;
    private DatabaseReference database,database1;
    private FirebaseAuth mAuth;
    private Spinner branch,course,specialisation,date,date1;
    private static final String[] COUNTRIES = new String[]{
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola"
    };

    ArrayList<Integer>Entry1 = new ArrayList<Integer>();
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
        setContentView(R.layout.activity_main);
        int s= Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1990; i <= s; i++) {
            Entry1.add(i);
            graduation1.add(String.valueOf(i));
        }
graduation1.add("On Going");
        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button3);
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
        final String ye = "", ge = "", bran = "", cour = "", special = "", name1 = "", mail1 = "", country1 = "",pass1="";

   /*     if (!TextUtils.isEmpty(name1) || !TextUtils.isEmpty(mail1) || !TextUtils.isEmpty(ye) || !TextUtils.isEmpty(ge) || !TextUtils.isEmpty(bran) || !TextUtils.isEmpty(cour) || !TextUtils.isEmpty(special) || !TextUtils.isEmpty(country1))  {
            process.setTitle("Registoring User");
            process.setTitle("Please wait a second!");
            process.setCanceledOnTouchOutside(false);

            process.show();
            Toast.makeText(this,name1+" "+mail1+" "+ye+" "+ge+" "+bran+" "+cour+" "+special+" "+country1,Toast.LENGTH_LONG).show();
          //  registor(name1, mail1,ye,ge,bran,cour,special,country1);

        }*/


        String[] countries = getResources().getStringArray(R.array.countries);
        AutoCompleteTextView editText = findViewById(R.id.actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, COUNTRIES);
        editText.setAdapter(adapter);


        ArrayAdapter<String> adapter1 = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, specialization1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house4.setAdapter(adapter1);


        ArrayAdapter<String> adapter6 = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, Entry1);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house.setAdapter(adapter6);

        ArrayAdapter<String> adapter7 = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, graduation1);
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house1.setAdapter(adapter7);

        ArrayAdapter<String> adapter4 = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, Branch1);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house2.setAdapter(adapter4);


        ArrayAdapter<String> adapter5 = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, course1);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house3.setAdapter(adapter5);


    /*    button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,home.class);
                startActivity(i);
            }
        });
    */


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (name.getText().toString() != null || mail.getText().toString() != null || pass.getText().toString()!=null || country.getText().toString() != null || spinner_house4.getSelectedItem().toString() != null || spinner_house3.getSelectedItem().toString() != null || spinner_house2.getSelectedItem().toString() != null || spinner_house1.getSelectedItem().toString() != null || spinner_house.getSelectedItem().toString() != null) {
                    final   String name1 = name.getText().toString();
                    String  mail1 = mail.getText().toString();
                    String pass1=pass.getText().toString();
                    String special = spinner_house4.getSelectedItem().toString();
                    String  country1 = country.getText().toString();
                    String cour = spinner_house3.getSelectedItem().toString();
                    String bran = spinner_house2.getSelectedItem().toString();
                    String ge = spinner_house1.getSelectedItem().toString();
                    String  ye = spinner_house.getSelectedItem().toString();
                    registor(name1, mail1, pass1,special,country1,cour,bran,ge,ye);



                 //  Toast.makeText(MainActivity.this,name1+" "+mail1+" "+country1+" "+special+" "+cour+" "+bran+" "+ge+" "+ye, Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this,"Please fill all the details!",Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(this, name1 + " " + mail1 + " " + ye + " " + ge + " " + bran + " " + cour + " " + special + " " + country1, Toast.LENGTH_LONG).show();

            }
        });

    }


    private void registor(final String name1, final String email, final String password,final String special,final String country,final String cour,final String bran,final String ge,final String ye) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            user=mAuth.getCurrentUser().getUid();
database=fstore.getReference("Users").child(user);


                            Map<String,Object> users=new HashMap<>();
                            users.put("FullName",name1);
                            users.put("MailId",email);
                            //hashMap.put("search",username.toLowerCase());
                            //hashMap.put("status","offline");
                            users.put("Specialization",special);
                            users.put("Country",country);
                            users.put("Course",cour);
                            users.put("Branch",bran);
                            users.put("Graduation Year",ge);
                            users.put("Join Date",ye);
database.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {

        database1=fstore.getReference("Users1").child(user);
        Map<String,Object> users1=new HashMap<>();
        users1.put("id",user);
        users1.put("imageUrl","default");
        users1.put("username",name1);
        users1.put("status","offline");
        users1.put("search",name1.toLowerCase());
        database1.setValue(users1);

        Toast.makeText(MainActivity.this, "Success.", Toast.LENGTH_LONG).show();
    }
});

                            process.dismiss();
                           checkemail();



                        } else {
                            // If sign in fails, display a message to the user.
                            process.hide();
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });


    }

    private void checkemail() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener((new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(MainActivity.this, "Successfully Registered,verification mail sent!", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        finish();

                        Intent i = new Intent(MainActivity.this, Login.class);

                        i.putExtra("STRING_I_NEED", user);
                        startActivity(i);

                    } else {
                        Toast.makeText(MainActivity.this, "Fail to Registor,Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }));





        }

    }
    @Override
    public void onBackPressed() {
        Intent start=new Intent(MainActivity.this,Login.class);
        startActivity(start);
        finish();
    }
}
