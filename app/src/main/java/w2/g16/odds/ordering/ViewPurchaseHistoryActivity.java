package w2.g16.odds.ordering;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityViewProductBinding;
import w2.g16.odds.databinding.ActivityViewPurchaseHistoryBinding;
import w2.g16.odds.model.Order;

public class ViewPurchaseHistoryActivity extends AppCompatActivity {

    private ActivityViewPurchaseHistoryBinding binding;
    private ArrayList<Order> ordered_product;
    private ViewPurchaseHistoryAdapter adapter;
    private String orderID;
    private String order_status;
    private String shopID;
    private String shopname;
    private String total;

    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPurchaseHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");

        DocumentReference docRef = db.collection("order").document(orderID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        shopname = document.get("shop_name").toString();
                        binding.tvShopName.setText(shopname);
                        binding.tvOrderID.setText(document.getId());
                        Timestamp timestamp = (Timestamp) document.get("order_date");
                        Date order_date = timestamp.toDate();
                        binding.tvOrderDate.setText(formatter.format(order_date));
                        order_status = document.get("order_status").toString();
                        shopID = document.get("order_from").toString();
                        String delivery_method = document.get("delivery_method").toString();
                        binding.tvDeliveryMethod.setText(delivery_method);
                        Timestamp timestamp2 = (Timestamp) document.get("order_date");
                        Date delivery_time = timestamp2.toDate();
                        total = document.get("order_amount").toString();
                        binding.tvTotal.setText(total);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        db.collection("order").document(orderID)
                .collection("ordered_product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String SKU = document.get("SKU").toString();
                                String image = document.get("image").toString();
                                String product_name = document.get("product_name").toString();
                                String quantity = document.get("quantity").toString();
                                String price = document.get("price").toString();
                                String rated = document.get("rated").toString();

                                ordered_product.add(new Order(orderID, SKU, product_name, price, quantity, image, rated));
                                adapter.notifyItemInserted(ordered_product.size());
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        ordered_product = new ArrayList<>();
        adapter = new ViewPurchaseHistoryAdapter(getLayoutInflater(), ordered_product);
        binding.recItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recItems.setAdapter(adapter);

        binding.btnRepurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                intent.putExtra("shopname", shopname);
                intent.putExtra("shopID", shopID);
                intent.putExtra("order", ordered_product);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });
    }
}