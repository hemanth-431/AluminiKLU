package my.alumni.klu;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addaluminiben extends AppCompatActivity {
ImageView imageView,canel;
EditText editText;
FirebaseAuth mAuth;
DatabaseReference databaseReference,getDatabaseReference,abc;
FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaluminiben);
        mAuth = FirebaseAuth.getInstance();
floatingActionButton=findViewById(R.id.add_event);
editText=findViewById(R.id.adddata);
        Window window = addaluminiben.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(addaluminiben.this.getResources().getColor(R.color.primary));
try {
    getDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Benfits");
    getDatabaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            editText.setText(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}catch (Exception e){

}


        canel=findViewById(R.id.cancel);
        canel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
imageView=findViewById(R.id.back);



imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});



floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String s=mAuth.getCurrentUser().getUid();
        if(s.equals("3RUoJsr2KUhuA1Uk3HF7xXA3wEZ2"))
        {databaseReference= FirebaseDatabase.getInstance().getReference("Benfits");

        databaseReference.setValue(editText.getText().toString());}else {
            Toast.makeText(addaluminiben.this,"You can't edit it.",Toast.LENGTH_LONG).show();
        }

        finish();
    }
});


    }
}
