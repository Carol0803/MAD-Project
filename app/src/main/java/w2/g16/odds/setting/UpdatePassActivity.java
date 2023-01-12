package w2.g16.odds.setting;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.R;
import w2.g16.odds.model.UserEmail;

public class UpdatePassActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    Button save;
    EditText newpass,renewpass;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        email = UserEmail.getEmail(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword()
    {

        newpass = findViewById(R.id.newPass);
        renewpass = findViewById(R.id.rePass);
        String newPassword = newpass.getText().toString();
        String renewPassword = renewpass.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();

        if(newPassword.equals(renewPassword))
        {
            // Validate the new password
            if (!isValidPassword(newPassword))
            {
                Toast.makeText(getApplicationContext(), "Password must at least 6 characters long with minimum 1 uppercase, lowercase and number ",Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(user!=null) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Successful.",Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(this, SettingsActivity.class));
                                } else {
                                    if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                        Toast.makeText(getApplicationContext(), "Weak Password.",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error Updating Password.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "No user currently logged in.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Password Mismatch",Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isValidPassword(String password) {
        // Minimum 8 characters
        if (password.length() < 6) {
            return false;
        }
        // At least 1 uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // At least 1 lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        // At least 1 number
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }
}