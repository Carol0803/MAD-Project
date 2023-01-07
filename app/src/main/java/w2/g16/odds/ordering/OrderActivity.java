package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Vector;

import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityOrderBinding;
import w2.g16.odds.model.Order;
import w2.g16.odds.product_browsing.ViewProductActivity;
import w2.g16.odds.shop_recommendation.shop_recommendation;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;

    private Order order;
    private Vector<Order> orders;
    private OrderAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btmNav.setSelectedItemId(R.id.order);
        binding.btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.recommendation:
                        startActivity(new Intent(getApplicationContext(), shop_recommendation.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.order:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), shop_recommendation.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        final String TAG = "Read Data Activity";
        db.collection("order")
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
                                adapter = new OrderAdapter(getParent(), getApplicationContext(), getLayoutInflater(), orders);
                  /*                      new OrderAdapter.OrderAdapterListener() {
                                    @Override
                                    public void tvMore(View v, int position) {
                                        Log.d(TAG, "tvMore at position "+position);
                                        Order order = orders.get(position);
                                        String orderID = order.getOrderID();

                                        Intent intent = new Intent(OrderActivity.this, ViewOrderActivity.class);
                                        intent.putExtra("orderID", orderID);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void btnView(View v, int position) {
                                        Log.d(TAG, "btnView at position "+position);

                                    }
                                });*/

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