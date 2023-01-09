package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Vector;

import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCartBinding;
import w2.g16.odds.databinding.ActivityPurchaseHistoryBinding;
import w2.g16.odds.model.Order;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private ActivityPurchaseHistoryBinding binding;
    private Order order;
    private Vector<Order> orders;
    private PurchaseHistoryAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        final String TAG = "Read Data Activity";
        db.collection("order")
                .whereEqualTo("order_status", "COMPLETED")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String orderID = document.getId();
                                String amount = document.get("order_amount").toString();
                                String shopID = document.get("order_from").toString();
                                String shopName = document.get("shop_name").toString();
                                String order_status = document.get("order_status").toString();

                                DocumentReference docRef = db.collection("order").document(orderID)
                                        .collection("ordered_product").document("001");
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                String name = document.get("product_name").toString();
                                                String quantity = document.get("quantity").toString();
                                                String price = document.get("price").toString();
                                                String img = document.get("image").toString();

                                                orders.add(new Order(orderID, amount, shopName, name, quantity, price, img, order_status));
                                                adapter.notifyItemInserted(orders.size());
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                                orders = new Vector<>();
                                adapter = new PurchaseHistoryAdapter(getParent(), getApplicationContext(), getLayoutInflater(), orders);

                                binding.recOrderList.setAdapter(adapter);
                                binding.recOrderList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}