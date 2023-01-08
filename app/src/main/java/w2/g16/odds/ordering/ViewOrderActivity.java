package w2.g16.odds.ordering;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckoutBinding;
import w2.g16.odds.databinding.ActivityViewOrderBinding;
import w2.g16.odds.model.Order;
import w2.g16.odds.model.Products;

public class ViewOrderActivity extends AppCompatActivity {

    private ActivityViewOrderBinding binding;
    private ArrayList<Order> ordered_product;
    private Checkout2Adapter adapter;
    private String orderID;
    private String order_status;

    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
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



/*        // fragment
        fragment = new OrderDetailsFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.lytODFrag, fragment);
        fragmentTransaction.commit();*/

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

                        binding.tvShopName.setText(document.get("shop_name").toString());
                        binding.tvOrderID.setText(document.getId());
                        Timestamp timestamp = (Timestamp) document.get("order_date");
                        Date order_date = timestamp.toDate();
                        binding.tvOrderDate.setText(formatter.format(order_date));
                        order_status = document.get("order_status").toString();
                        if(order_status.equals("UNPAID"))
                            binding.icOrderStatus.setImageResource(R.drawable.ic_order_status_unpaid);
                        if(order_status.equals("TO-PACK"))
                            binding.icOrderStatus.setImageResource(R.drawable.ic_order_status_pack);
                        if(order_status.equals("TO-DELIVER")) {
                            binding.icOrderStatus.setImageResource(R.drawable.ic_order_status_deliver);
                            binding.btnOrderReceived.setEnabled(true);
                            binding.btnOrderReceived.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("order_status", "COMPLETED");

                                    db.collection("order").document(orderID)
                                            .set(data, SetOptions.merge());

                                    OrderCompletedPopUp orderCompletedPopUp = new OrderCompletedPopUp(getApplicationContext());
                                    orderCompletedPopUp.showPopupWindow(v);
                                }
                            });
                        }
                        if(order_status.equals("COMPLETED"))
                            binding.icOrderStatus.setImageResource(R.drawable.ic_order_status_completed);
                        if(order_status.equals("CANCELLED"))
                            binding.icOrderStatus.setImageResource(R.drawable.ic_order_status_cancel);
                        String shopID = document.get("order_from").toString();
                        String delivery_method = document.get("delivery_method").toString();
                        binding.tvDeliveryMethod.setText(delivery_method);
                        if(delivery_method.equals("Home Delivery")){
                            DocumentReference docRef2 = db.collection("order").document(orderID)
                                    .collection("address").document("001");
                            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                            binding.tvRecipientName.setText(document.get("receiver_name").toString());
                                            binding.tvTel.setText(document.get("receiver_tel").toString());
                                            binding.tvAddr1.setText(document.get("addr1").toString());
                                            binding.tvAddr2.setText(document.get("addr2").toString());
                                            binding.tvCity.setText(document.get("city").toString());
                                            binding.tvPostcode.setText(document.get("postcode").toString());
                                            binding.tvState.setText(document.get("state").toString());
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                        }
                        if(delivery_method.equals("Self Collection")){
                            binding.tvAddress.setText("Pick Up Address");
                            dbGetShopAddress(shopID);
                        }
                        Timestamp timestamp2 = (Timestamp) document.get("order_date");
                        Date delivery_time = timestamp2.toDate();
                        binding.tvDeliveryTimeDate.setText(formatter.format(delivery_time));
                        binding.tvTotal.setText(document.get("order_amount").toString());

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

                                ordered_product.add(new Order(SKU, product_name, price, quantity, image));
                                adapter.notifyItemInserted(ordered_product.size());
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        ordered_product = new ArrayList<>();
        adapter = new Checkout2Adapter(getLayoutInflater(), ordered_product);
        binding.recItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recItems.setAdapter(adapter);
    }

    public void dbGetShopAddress(String shopID){
        DocumentReference docRef = db.collection("shop").document(shopID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        binding.tvRecipientName.setText(document.get("shop_name").toString());
                        binding.tvTel.setText(document.get("shop_tel").toString());
                        binding.tvAddr1.setText(document.get("shop_addr1").toString());
                        binding.tvAddr2.setText(document.get("shop_addr2").toString());
                        binding.tvCity.setText(document.get("shop_city").toString());
                        binding.tvPostcode.setText(document.get("shop_postcode").toString());
                        binding.tvState.setText(document.get("shop_state").toString());

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