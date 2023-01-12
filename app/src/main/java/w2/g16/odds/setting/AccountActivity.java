package w2.g16.odds.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.R;
import w2.g16.odds.model.Order;
import w2.g16.odds.model.UserEmail;


public class AccountActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    CollectionReference collectionRef;

    Button save;
    EditText name,contact,email_addr,add1,add2,city,postcode,state;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

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

        name = findViewById(R.id.FullName);
        contact = findViewById(R.id.Contact);
        email_addr = findViewById(R.id.Email);
        add1 = findViewById(R.id.Address1);
        add2 = findViewById(R.id.Address2);
        city = findViewById(R.id.City);
        postcode = findViewById(R.id.Postcode);
        state = findViewById(R.id.State);

        DocumentReference docRef =
                db.collection("customer").document(email)
                        .collection("address").document("001");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Document exists, fetch specific field
                    Object fieldValue = document.get("receiver_name");
                    Object fieldValue2 = document.get("receiver_tel");
                    Object fieldValue3 = document.get("addr1");
                    Object fieldValue4 = document.get("addr2");
                    Object fieldValue5 = document.get("city");
                    Object fieldValue6 = document.get("postcode");
                    Object fieldValue7 = document.get("state");

                    // update the EditText with the field value
                    name.setText(fieldValue.toString());
                    contact.setText(fieldValue2.toString());
                    email_addr.setText(email);
                    add1.setText(fieldValue3.toString());
                    add2.setText(fieldValue4.toString());
                    city.setText(fieldValue5.toString());
                    postcode.setText(fieldValue6.toString());
                    state.setText(fieldValue7.toString());

                } else {
                    // Document does not exist, inform user
                    System.err.println("Error: Document does not exist");
                }
            } else {
                System.err.println("Error getting document: " + task.getException());
            }
        });

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUpdate();
            }
        });
    }

    public void saveUpdate()
    {
        String newname = name.getText().toString();
        String newcontact = contact.getText().toString();
        String newadd1 = add1.getText().toString();
        String newadd2 = add2.getText().toString();
        String newcity = city.getText().toString();
        String newpostcode = postcode.getText().toString();
        String newstate = state.getText().toString();

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("receiver_name", newname);
        data.put("receiver_tel", newcontact);
        data.put("addr1", newadd1);
        data.put("addr2", newadd2);
        data.put("city", newcity);
        data.put("postcode", newpostcode);
        data.put("state", newstate);

        db.collection("customer").document(email)
                .collection("address").document("001")
                .set(data, SetOptions.merge());
        Toast.makeText(getApplicationContext(), "Successful.",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, SettingsActivity.class));

    }
}
