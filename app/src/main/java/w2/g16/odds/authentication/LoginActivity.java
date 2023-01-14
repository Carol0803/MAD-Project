package w2.g16.odds.authentication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.HomePage;
import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.model.UserEmail;

public class LoginActivity extends AppCompatActivity {

    ImageView button;
    TextView signup2;
    TextView forgotPass;
    Button signin;
    EditText Email, userPass;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
//        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_logininto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        signin = findViewById(R.id.signin);
//        button = (ImageView) findViewById(R.id.backbtn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMain();
//            }
//        });

        signup2 = findViewById(R.id.signup2);
        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });

        forgotPass = findViewById(R.id.forgotpass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPass();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openLogin();
            }
        });
    }

    public void openMain(){
//        Intent intent = new Intent(this, HomePage.class);
//        startActivity(intent);
    }

    public void openSignup(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void openForgotPass(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void openLogin()
    {
        Email = findViewById(R.id.Email);
        userPass = findViewById(R.id.userPass);

        String email = Email.getText().toString();
        String password = userPass.getText().toString();

        if(Email.getText().toString().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
        }
        else if(userPass.getText().toString().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Login success, start the data activity
                                UserEmail.setEmail(getApplicationContext(), email);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // Login failed, display a message to the user
                                Toast.makeText(LoginActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}