package my.alumni.klu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class add_discription extends AppCompatActivity {
    private EditText dis;
    private TextView b;
    TextView skip;
    private Task<Void> firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discription);

        Window window = add_discription.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(add_discription.this.getResources().getColor(R.color.primary));

        dis = findViewById(R.id.para);
        b = findViewById(R.id.add);
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(add_discription.this,Side_drawer.class);
                startActivity(i);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = dis.getText().toString();
                if (s.length() < 30) {
                    Toast.makeText(add_discription.this, "It must contain atleast 30 characters", Toast.LENGTH_LONG).show();

                } else if(s.length() > 80){
                    Toast.makeText(add_discription.this, "It must contain less than 80 characters", Toast.LENGTH_LONG).show();
                }else {
                    Bundle bundle = getIntent().getExtras();

                    if (bundle.getString("STRING_I_NEED") != null) {
                        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("uploads").child(bundle.getString("STRING_I_NEED")).child("mData").setValue(s);


//                        Toast.makeText(add_discription.this, bundle.getString("STRING_I_NEED"), Toast.LENGTH_LONG).show();

                        //  Intent i=new Intent(add_discription.this,ImagesActivity.class);
                        //  startActivity(i);
                        Intent i = new Intent(add_discription.this, Side_drawer.class);
                        startActivity(i);
                    }
                }
            }

        });
    }
}