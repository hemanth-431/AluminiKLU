package com.example.aluminiklu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.google.firebase.database.FirebaseDatabase;

public class loginexample extends AppCompatActivity {
    private TextInputEditText mail;
    private TextInputEditText pass;
    private TextView button2;
    private TextView button4;
    private Button button;
    private CheckBox rem_use;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private  static final String PREFS_NAME="prefs";
    private  static final String KEY_REMEMBER="remember";
    private  static final String KEY_USERNAME="username";
    private  static final String KEY_PASS="password";
    private ProgressDialog process;
    private FirebaseAuth mAuth;
    String s;
    public String str;
    private FirebaseDatabase firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginexample);

        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle.getString("STRING_I_NEED") != null) {
                s = bundle.getString("STRING_I_NEED");
                //Toast.makeText(Login.this,bundle.getString("STRING_I_NEED"),Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

        }


        sharedPreferences=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        process=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null && user.isEmailVerified()) {
            finish();
       //     Toast.makeText(loginexample.this,user.getUid(),Toast.LENGTH_LONG).show();
            startActivity(new Intent(loginexample.this, Side_drawer.class));
        }
        else {
         try{   if(user.isEmailVerified())
            { }else {Toast.makeText(loginexample.this,"Please verify your mail",Toast.LENGTH_LONG).show();}}catch (Exception e){

         }
        }
        button2=findViewById(R.id.button2);
        button4=findViewById(R.id.button4);
        mail=(TextInputEditText)findViewById(R.id.maillog);
        rem_use=findViewById(R.id.checkBox);
        pass=(TextInputEditText)findViewById(R.id.passwordlog);

        getPreferencesData();
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
showDialog();
            //    Intent i=new Intent(loginexample.this,registerexample.class);
           //     startActivity(i);
            }

        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginexample.this,Forget_password.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        button=findViewById(R.id.buttonlog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                process.setTitle("Logging in");
                process.setTitle("Wait a second!");
                process.setCanceledOnTouchOutside(false);
                process.show();

                String mail1=mail.getText().toString();
                String pass1=pass.getText().toString();
                if(!TextUtils.isEmpty(mail1) || !TextUtils.isEmpty(pass1))
                { registor(mail1,pass1);}
                process.dismiss();
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

                Intent intent=new Intent(loginexample.this,registerexample.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
    private void getPreferencesData() {
        SharedPreferences sp=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u=sp.getString("pref_name","not found.");
            mail.setText(u.toString());
        }
        if(sp.contains("pref_pass")){
            String p=sp.getString("pref_pass","not found");
            pass.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b=sp.getBoolean("pref_check",false);
            rem_use.setChecked(b);

        }
    }
    private void registor(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                process.dismiss();

                                Intent intent = new Intent(loginexample.this, Side_drawer.class);





                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("STRING_I_NEED", s);
                                startActivity(intent);
                                finish();
                            }else {
                                process.dismiss();
                                Toast.makeText(loginexample.this,"Please Check Your Mail!",Toast.LENGTH_LONG).show();

                            }





                            if(rem_use.isChecked()){
                                Boolean boolischecked=rem_use.isChecked();
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("pref_name",mail.getText().toString());
                                editor.putString("pref_pass",pass.getText().toString());
                                editor.putBoolean("pref_check",boolischecked);
                                editor.apply();
                            }
                            else
                            {
                                sharedPreferences.edit().clear().apply();
                            }
                            // Sign in success, update UI with the signed-in user's information


                            mail.getText().clear();
                            pass.getText().clear();



                        } else {
                            // If sign in fails, display a message to the user.
                            process.hide();
                            Toast.makeText(loginexample.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();

                        }
process.dismiss();
                        // ...
                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
