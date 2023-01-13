package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityAddNewAddressBinding;
import w2.g16.odds.databinding.ActivityCartBinding;
import w2.g16.odds.model.UserEmail;

public class AddNewAddressActivity extends AppCompatActivity {

    private ActivityAddNewAddressBinding binding;
    private String email;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email = UserEmail.getEmail(getApplicationContext());

        Intent intent = getIntent();
        int no_of_addr = intent.getIntExtra("no_of_addr", 0);
        String new_no_of_address = String.format("%03d", no_of_addr + 1);

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> data = new HashMap<>();
                data.put("receiver_name", binding.FullName.getText().toString());
                data.put("receiver_tel", binding.Contact.getText().toString());
                data.put("addr1", binding.Address1.getText().toString());
                data.put("addr2", binding.Address2.getText().toString());
                data.put("city", binding.City.getText().toString());
                data.put("postcode", binding.Postcode.getText().toString());
                data.put("state", binding.State.getText().toString());
                data.put("default", false);

                final String TAG = "Read Data Activity";
                db.collection("customer").document(email)
                        .collection("address").document(new_no_of_address)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                Toast.makeText(getApplicationContext(), "Address added.",Toast.LENGTH_SHORT).show();

                                Intent intent2 = new Intent();
                                setResult(RESULT_OK, intent2);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });
    }
}