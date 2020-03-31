package com.example.aluminiklu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_password extends AppCompatActivity {
    private EditText mail;
    private Button button;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mail=findViewById(R.id.mailfor);
        button=findViewById(R.id.buttonfor);
        firebaseAuth=FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=mail.getText().toString().trim();
                if(user.equals(""))
                {
                    Toast.makeText(Forget_password.this,"Please Enter a valied EmailId",Toast.LENGTH_LONG).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Forget_password.this,"Password Reset is Sucessful /n Please Check Your mail",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(Forget_password.this,Login.class));
                            }
                            else
                            {
                                Toast.makeText(Forget_password.this,"hi",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent start=new Intent(Forget_password.this,Login.class);
        startActivity(start);
        finish();
    }
}
