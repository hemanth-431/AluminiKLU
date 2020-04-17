package com.example.aluminiklu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class add_discription extends AppCompatActivity {
    private EditText dis;
    private Button b;
    TextView skip;
    private Task<Void> firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discription);
        dis = findViewById(R.id.para);
        b = findViewById(R.id.add);
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(add_discription.this,ImagesActivity.class);
                startActivity(i);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = dis.getText().toString();

                Bundle bundle = getIntent().getExtras();

                if (bundle.getString("STRING_I_NEED") != null) {
                    firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("uploads").child(bundle.getString("STRING_I_NEED")).child("mData").setValue(s);


                    Toast.makeText(add_discription.this, bundle.getString("STRING_I_NEED"), Toast.LENGTH_LONG).show();

                    Intent i=new Intent(add_discription.this,ImagesActivity.class);
                    startActivity(i);
                }
            }

        });
    }
}