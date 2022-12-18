package w2.g16.odds.ordering;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckoutBinding;
import w2.g16.odds.databinding.ActivityViewOrderBinding;
import w2.g16.odds.model.Order;

public class ViewOrderActivity extends AppCompatActivity {

    private ActivityViewOrderBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //set toolbar as actionbar
        //back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderActivity.class));
            }
        });

        String orderID = getIntent().getStringExtra("orderID");

        DocumentReference docRef = db.collection("order").document(orderID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        binding.tvShopName.setText(document.get("order_from").toString());
                        binding.tvOrderDate.setText(document.get("order_date").toString());
                        String order_status = document.get("order_status").toString();
                        binding.tvDeliveryMethod.setText(document.get("delivery_method").toString());
                        binding.tvDeliveryTimeDate.setText(document.get("delivery_time").toString());
                        binding.tvTotal.setText(document.get("order_amount").toString());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}