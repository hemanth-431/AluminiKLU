package my.alumni.klu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private Button button,login;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Window window = Forget_password.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(Forget_password.this.getResources().getColor(R.color.darkblue));
        mail=findViewById(R.id.mailfor);
        button=findViewById(R.id.buttonfor);
        login=findViewById(R.id.Loginpage);
        firebaseAuth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Forget_password.this,loginexample.class);
                startActivity(i);
            }
        });
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
                                startActivity(new Intent(Forget_password.this,loginexample.class));
                            }
                            else
                            {
                                Toast.makeText(Forget_password.this,"Failed!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent start=new Intent(Forget_password.this,loginexample.class);
        startActivity(start);
        finish();
    }
}
