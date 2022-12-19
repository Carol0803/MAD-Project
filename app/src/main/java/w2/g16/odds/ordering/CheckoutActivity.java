package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckoutBinding;
import w2.g16.odds.model.Order;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //set toolbar as actionbar
        //back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });

        final String TAG = "Read Data Activity";
        db.collection("customer/username/address")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String receiver_name = null;
                            String receiver_tel = null;
                            String addr1 = null;
                            String addr2 = null;
                            String city = null;
                            String postcode = null;
                            String state = null;
                            String location = null;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String addrID = document.getId();
                                addr1 = document.get("addr1").toString();
                                addr2 = document.get("addr2").toString();
                                city = document.get("city").toString();
                                postcode = document.get("postcode").toString();
                                state = document.get("state").toString();
                                location = document.get("location").toString();
                                receiver_name = document.get("receiver_name").toString();
                                receiver_tel = document.get("receiver_tel").toString();
                            }

                            binding.tvRecipientName.setText(receiver_name);
                            binding.tvTel.setText(receiver_tel);
                            binding.tvAddr1.setText(addr1);
                            binding.tvAddr2.setText(addr2);
                            binding.tvCity.setText(city);
                            binding.tvPostcode.setText(postcode);
                            binding.tvState.setText(state);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public void fnChangeAddress(View view) {
        startActivity(new Intent(this, ChooseAddressActivity.class));
    }

    public void fnNext(View view) {
        startActivity(new Intent(this, CheckoutActivity2.class));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbDelivery:
                if (checked)
                    binding.rbDelivery.setBackgroundResource(R.drawable.ic_delivery_checked);
                    binding.rbSelfCollection.setBackgroundResource(R.drawable.ic_self_collection);
                    break;
            case R.id.rbSelfCollection:
                if (checked)
                    binding.rbSelfCollection.setBackgroundResource(R.drawable.ic_self_collection_checked);
                    binding.rbDelivery.setBackgroundResource(R.drawable.ic_delivery);
                    break;
        }
    }
}