package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityChooseAddressBinding;
import w2.g16.odds.model.Address;
import w2.g16.odds.model.UserEmail;

public class ChooseAddressActivity extends AppCompatActivity {

    private ActivityChooseAddressBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Vector<Address> addresses;
    private ChooseAddressAdapter adapter;
    private Address address_chosen;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //set toolbar as actionbar
        //back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
            }
        });

        email = UserEmail.getEmail(getApplicationContext());

        final String TAG = "Read Data Activity";
        db.collection("customer/" + email + "/address")
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
                            String isDefault = null;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String addrID = document.getId();
                                addr1 = document.get("addr1").toString();
                                addr2 = document.get("addr2").toString();
                                city = document.get("city").toString();
                                postcode = document.get("postcode").toString();
                                state = document.get("state").toString();
//                                location = document.get("location").toString();
                                receiver_name = document.get("receiver_name").toString();
                                receiver_tel = document.get("receiver_tel").toString();
                                isDefault = document.get("default").toString();

                                addresses.add(new Address(receiver_name, receiver_tel, addr1, addr2, city, postcode, state, isDefault));
                                adapter.notifyItemChanged(addresses.size());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        addresses = new Vector<>();
        adapter = new ChooseAddressAdapter(this, addresses);

        binding.recAddressList.setLayoutManager(new LinearLayoutManager(this));
        binding.recAddressList.setAdapter(adapter);

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.putExtra("objAddress", address_chosen);
                setResult(RESULT_OK, intent2);
                finish();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("return_address"));


    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            address_chosen = (Address) intent.getSerializableExtra("objAddress");
        }
    };

}