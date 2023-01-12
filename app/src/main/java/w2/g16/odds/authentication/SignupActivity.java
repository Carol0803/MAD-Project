package w2.g16.odds.authentication;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.R;

public class SignupActivity extends AppCompatActivity {

    ImageView button;
    TextView textView;
    Button btnregister;
    EditText FName, Contact, Email, Add1, Add2, City, Postcode, State, Username, Password, rePass;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnregister=findViewById(R.id.register);
        FName=findViewById(R.id.editTextFName);
        Contact=findViewById(R.id.editTextContact);
        Email=findViewById(R.id.editTextEmail);
        Add1=findViewById(R.id.editTextAddress2);
        Add2=findViewById(R.id.editTextAddress1);
        City=findViewById(R.id.editTextCity);
        Postcode=findViewById(R.id.editTextPostcode);
        State=findViewById(R.id.editTextState);
        Username=findViewById(R.id.editTextUsername);
        Password=findViewById(R.id.editTextTextPassword);
        rePass=findViewById(R.id.editTextTextPassword2);

        textView = findViewById(R.id.textViewLogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register(){

        Map<String, Object> user = new HashMap<>();
        user.put("tel", Contact.getText().toString());
        user.put("username", Username.getText().toString());

        Map<String, Object> user_address = new HashMap<>();
        user_address.put("receiver_name", FName.getText().toString());
        user_address.put("receiver_tel", Contact.getText().toString());
        user_address.put("addr1", Add1.getText().toString());
        user_address.put("addr2", Add2.getText().toString());
        user_address.put("city", City.getText().toString());
        user_address.put("postcode", Postcode.getText().toString());
        user_address.put("state", State.getText().toString());
        user_address.put("default", true);

        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(FName.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing Full Name", Toast.LENGTH_SHORT).show();
        }

        else if(Contact.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing Contact Number", Toast.LENGTH_SHORT).show();
        }

        else if(Email.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Check your Email Format", Toast.LENGTH_SHORT).show();
        }

        else if(Add1.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing Address 1", Toast.LENGTH_SHORT).show();
        }

        else if(Add2.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing Address 2", Toast.LENGTH_SHORT).show();
        }

        else if(City.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing City", Toast.LENGTH_SHORT).show();
        }

        else if(Postcode.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing Postcode", Toast.LENGTH_SHORT).show();
        }

        else if(State.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing State", Toast.LENGTH_SHORT).show();
        }

        else if(Username.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing Username", Toast.LENGTH_SHORT).show();
        }

        else if(Password.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Missing Password", Toast.LENGTH_SHORT).show();
        }

        else if(!rePass.getText().toString().equals(Password.getText().toString()))
        {
            Toast.makeText(SignupActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
        }

        else
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                db.collection("customer").document(email)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                db.collection("customer").document(email).collection("address").document("001")
                                                        .set(user_address)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // Data stored successfully, start the data activity
                                                                Toast.makeText(SignupActivity.this, "Successfully Register Account", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Data store failed, show an error message
                                                                Toast.makeText(SignupActivity.this, "Check Your Email and Password ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Data store failed, show an error message
                                                Toast.makeText(SignupActivity.this, "Check Your Email and Password ", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Registration failed, show an error message
                                Toast.makeText(SignupActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}