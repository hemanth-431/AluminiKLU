package my.alumni.klu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.androtaku.countrylist.CountryList;
import my.alumni.klu.Fragments.ChatsFragment;

public class registerexample extends AppCompatActivity {
    private TextInputEditText userName,email,pass,work,number;
    AutoCompleteTextView country;
    CheckBox checkBox;
    private ProgressDialog process;
    //    private TextView logintext,forgetpass;
    Button signUp;
    private FirebaseDatabase fstore;
    String user;
    private DatabaseReference database,database1;
    private FirebaseAuth mAuth;


    ArrayList<String> Entry1 = new ArrayList<String>();
    ArrayList<String>graduation1 = new ArrayList<String>();

    private static final String[] Branch1= new String[]{
            "CSE", "ECE", "BBA", "CIVIL", "MEC","None"
    };
    private static final String[] course1 = new String[]{
            "Faculty","B.Tech", "B.Arch", "M.B.A", "M.Tech", "Diploma"
    };
    private static final String[] specialization1 = new String[]{
            "Specialization","ComputerNetworks", "I.O.T", "AI", "Ds", "BigData","None"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerexample);

        List<String> countryNames = CountryList.getCountryNames(registerexample.this);
        String[] COUNTRIES=new String[countryNames.size()];
        for(int i=0;i<(int)countryNames.size();i++){
            COUNTRIES[i]=countryNames.get(i).toString();
        }
        checkBox=findViewById(R.id.checkBox);
        Window window = registerexample.this.getWindow();
        ChatsFragment c=new ChatsFragment();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(registerexample.this.getResources().getColor(R.color.darkblue));
        int s= Calendar.getInstance().get(Calendar.YEAR);
        Entry1.add("Joining Year");
        graduation1.add("Graduation Year");
        for (int i = 1990; i <= s; i++) {
            Entry1.add(String.valueOf(i));
            graduation1.add(String.valueOf(i));
        }
        graduation1.add("On Going");
        mAuth = FirebaseAuth.getInstance();
        //   specialisation = findViewById(R.id.specialisation);
        number=findViewById(R.id.mobileNo);
        process = new ProgressDialog(this);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.emailId);
        work = findViewById(R.id.workCom);
        signUp = findViewById(R.id.signUp);
        // forgetpass=findViewById(R.id.forgetpass);
    /*    forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(registerexample.this,Forget_password.class);
                startActivity(i);
            }
        });

     */
        fstore=FirebaseDatabase.getInstance();
        pass = findViewById(R.id.Password);
        //   final Spinner spinner_house4 = (Spinner) findViewById(R.id.specialisation);
        final Spinner spinner_house = (Spinner) findViewById(R.id.date);
        final Spinner spinner_house1 = (Spinner) findViewById(R.id.year);
        final Spinner spinner_house2 = (Spinner) findViewById(R.id.branch);
        final Spinner spinner_house3 = (Spinner) findViewById(R.id.specilization);
//        final String ye = "", ge = "", bran = "", cour = "", special = "", name1 = "", mail1 = "", country1 = "",pass1="";

   /*     if (!TextUtils.isEmpty(name1) || !TextUtils.isEmpty(mail1) || !TextUtils.isEmpty(ye) || !TextUtils.isEmpty(ge) || !TextUtils.isEmpty(bran) || !TextUtils.isEmpty(cour) || !TextUtils.isEmpty(special) || !TextUtils.isEmpty(country1))  {
            process.setTitle("Registoring User");
            process.setTitle("Please wait a second!");
            process.setCanceledOnTouchOutside(false);

            process.show();
            Toast.makeText(this,name1+" "+mail1+" "+ye+" "+ge+" "+bran+" "+cour+" "+special+" "+country1,Toast.LENGTH_LONG).show();
          //  registor(name1, mail1,ye,ge,bran,cour,special,country1);

        }*/


        //String[] countries = getResources().getStringArray(R.array.countries);
        AutoCompleteTextView editText = findViewById(R.id.actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, COUNTRIES);
        editText.setAdapter(adapter);


    /*    ArrayAdapter<String> adapter1 = new ArrayAdapter(registerexample.this, android.R.layout.simple_list_item_1, specialization1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house4.setAdapter(adapter1);


     */

        ArrayAdapter<String> adapter6 = new ArrayAdapter(registerexample.this, android.R.layout.simple_list_item_1, Entry1);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house.setAdapter(adapter6);
//
        ArrayAdapter<String> adapter7 = new ArrayAdapter(registerexample.this, android.R.layout.simple_list_item_1, graduation1);
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house1.setAdapter(adapter7);
//
        ArrayAdapter<String> adapter4 = new ArrayAdapter(registerexample.this, android.R.layout.simple_list_item_1, Branch1);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_house2.setAdapter(adapter4);


        ArrayAdapter<String> adapter5 = new ArrayAdapter(registerexample.this, android.R.layout.simple_list_item_1, specialization1);
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


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//|| spinner_house3.getSelectedItem().toString() != null
                //|| spinner_house4.getSelectedItem().toString() != null

                if (userName.getText().toString() == null || userName.getText().length() == 0 ) {
                    Toast.makeText(registerexample.this,"Please enter your Name",Toast.LENGTH_LONG).show();
                }else if(email.getText().toString() == null || email.getText().toString().length() == 0 ){
                    Toast.makeText(registerexample.this,"Please enter a valid MailId",Toast.LENGTH_LONG).show();
                }  else if(pass.getText().toString().length() < 6)
                {
                    Toast.makeText(registerexample.this,"Please enter a Strong Password",Toast.LENGTH_LONG).show();
                }
                else if(editText.getText().toString().length() == 0 || editText.getText().toString() == null){
                    Toast.makeText(registerexample.this,"Please select a Country",Toast.LENGTH_LONG).show();

                }
                else if (number.getText().length() != 10){
                    Toast.makeText(registerexample.this,"Please enter a Valid Number",Toast.LENGTH_LONG).show();
                }
                else if (spinner_house.getSelectedItem().toString().equals("Joining Year")){
                    Toast.makeText(registerexample.this, "Select Joining Year", Toast.LENGTH_SHORT).show();
                }else if (spinner_house1.getSelectedItem().toString().equals("Graduation Year")){
                    Toast.makeText(registerexample.this, "Select Graduation Year", Toast.LENGTH_SHORT).show();
                }else if (spinner_house3.getSelectedItem().toString().equals("Specialization")){
                    Toast.makeText(registerexample.this, "Select Specialization", Toast.LENGTH_SHORT).show();
                }
                else if(checkBox.isChecked()){

                    final String name1 = userName.getText().toString();
                    String mail1 = email.getText().toString();
                    String pass1 = pass.getText().toString();
                    String num1 = number.getText().toString();
                    String special = spinner_house3.getSelectedItem().toString();
                    String country1 = editText.getText().toString();
                    //String cour = spinner_house3.getSelectedItem().toString();
                    String bran = spinner_house2.getSelectedItem().toString();
                    String ge = spinner_house1.getSelectedItem().toString();
                    String ye = spinner_house.getSelectedItem().toString();
                    registor(name1, mail1, pass1, country1, bran, ge, ye, num1, special);


                }else {
                    Toast.makeText(registerexample.this,"Please check the Box",Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(MainActivity.this,name1+" "+mail1+" "+country1+" "+special+" "+cour+" "+bran+" "+ge+" "+ye, Toast.LENGTH_LONG).show();

                // Toast.makeText(this, name1 + " " + mail1 + " " + ye + " " + ge + " " + bran + " " + cour + " " + special + " " + country1, Toast.LENGTH_LONG).show();

                // showDialog();
            }


        });

    }

    void showDialog(){

        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.alert_dialog,null);
        AlertDialog alertDialog=new AlertDialog.Builder(this).setView(view).create();
        ImageView accept=view.findViewById(R.id.accept);
        TextView textView=view.findViewById(R.id.cancel);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void registor(final String name1, final String email, final String password,final String country,final String bran,final String ge,final String ye,final String number,final String special) {
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
                            users.put("Call",number);
                            users.put("flag",0);
                            users.put("Specialization",special);
                            users.put("Country",country);
                            users.put("Course","No Data");
                            users.put("Branch",bran);
                            users.put("Graduation Year",ge);
                            users.put("Join Date",ye);
                            database.setValue(users);
                            process.dismiss();
                            checkemail();



                        }else {
                            // If sign in fails, display a message to the user.
                            process.hide();
                            Toast.makeText(registerexample.this, "Authentication failed.",
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
                        FirebaseAuth firebaseAuth;
                        firebaseAuth=FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        Toast.makeText(registerexample.this, "Successfully Registered,verification mail sent!", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        finish();

                        Intent i = new Intent(registerexample.this, loginexample.class);

                        i.putExtra("STRING_I_NEED", user);
                        startActivity(i);

                    } else {
                        Toast.makeText(registerexample.this, "Fail to Registor,Please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }));





        }


    }
    @Override
    public void onBackPressed() {
        Intent start=new Intent(registerexample.this,loginexample.class);
        startActivity(start);
        finish();
    }



}
