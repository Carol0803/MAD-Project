package w2.g16.odds.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;

import w2.g16.odds.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button confirmBtn;
    EditText forgotEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmForgotPass();
            }
        });
    }

    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void confirmForgotPass()
    {

        forgotEmail = findViewById(R.id.Email);

        String email = forgotEmail.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Password reset email sent!", Toast.LENGTH_SHORT).show();
                            // The password reset email has been sent, the user can now update their password

                            // Login success, start the data activity
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        }
                        else {
                            // Login failed, display a message to the user
                            Toast.makeText(getApplicationContext(), "Error updating password in Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}