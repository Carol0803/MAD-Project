package w2.g16.odds.ordering;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityOrderPlacedBinding;
import w2.g16.odds.model.Address;
import w2.g16.odds.model.Order;

public class OrderPlacedActivity extends AppCompatActivity {

    private ActivityOrderPlacedBinding binding;
    private Address address;
    private String order_status;
    private Timestamp delivery_time;
    private ArrayList<Order> order_lists;
    private Checkout2Adapter adapter;
    private String orderID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String data = prefs.getString("string_id", "no id"); //no id: default value
        String payment_method = prefs.getString("payment_method", "");
        if(payment_method.equals("Cash")) {
            order_status = "UNPAID";
            binding.icOrderStatus.setImageResource(R.drawable.ic_order_status_unpaid);
        }
        else if(payment_method.equals("PayPal")) {
            order_status = "TO-PACK";
            binding.icOrderStatus.setImageResource(R.drawable.ic_order_status_pack);

        }
        String json_address = prefs.getString("address", "");
        address = gson.fromJson(json_address, Address.class);
        String json_delivery_time = prefs.getString("delivery_time", "");
        Date deliveryTime = gson.fromJson(json_delivery_time, Date.class);
        String shopname = prefs.getString("shopname", "");
        String shopID = prefs.getString("shopID", "");
        String total = prefs.getString("total", "");
        String json_order_lists = prefs.getString("order", "");
        Type type = new TypeToken< ArrayList < Order >>() {}.getType();
        order_lists = gson.fromJson(json_order_lists, type);

        /*Intent intent = getIntent();
        delivery_method = intent.getStringExtra("delivery_method");
        address = (Address) intent.getSerializableExtra("address");
        deliveryTime = (Date) intent.getSerializableExtra("delivery_time");
        delivery_time = new Timestamp(deliveryTime);
        shopname = intent.getStringExtra("shopname");
        total = intent.getStringExtra("total");*/

        String delivery_method = prefs.getString("delivery_method", "");
        if(delivery_method.equals("Home Delivery")){
            binding.tvRecipientName.setText(address.getReceiver_name());
            binding.tvTel.setText(address.getReceiver_tel());
            binding.tvAddr1.setText(address.getAddr1());
            binding.tvAddr2.setText(address.getAddr2());
            binding.tvCity.setText(address.getCity());
            binding.tvPostcode.setText(address.getPostcode());
            binding.tvState.setText(address.getState());
        }
        if(delivery_method.equals("Self Collection")){
            binding.tvAddress.setText("Pick Up Address");
            dbGetShopAddress(shopID);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        binding.tvDeliveryTimeDate.setText(formatter.format(deliveryTime));
        binding.tvShopName.setText(shopname);
        binding.tvOrderDate.setText(formatter.format(new Date()));
        binding.tvDeliveryMethod.setText(delivery_method);

//        order_lists = (ArrayList<Order>) intent.getSerializableExtra("order");
        adapter = new Checkout2Adapter(getLayoutInflater(), order_lists);
//        binding.recItems.setNestedScrollingEnabled(false);
        binding.recItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recItems.setAdapter(adapter);

        binding.tvTotal.setText(total);

        // add order to database
        Map<String, Object> order = new HashMap<>();
        order.put("delivery_method", delivery_method);
        order.put("delivery_time", new Timestamp(deliveryTime));
        order.put("order_amount", total);
        order.put("order_by", "");
        order.put("order_date", new Timestamp(new Date()));
        order.put("order_from", shopID);
        order.put("shop_name", shopname);
        order.put("order_status", order_status);
        order.put("payment_method", payment_method);
//        order.put("addr1", address.getAddr1());
//        order.put("addr2", address.getAddr2());
//        order.put("city", address.getCity());
//        order.put("postcode", address.getPostcode());
//        order.put("receiver_name", address.getReceiver_name());
//        order.put("receiver_tel", address.getReceiver_tel());
//        order.put("state", address.getState());

        final String TAG = "Read Data Activity";
        db.collection("order")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        orderID = documentReference.getId();
                        binding.tvOrderID.setText(orderID);

                        db.collection("order").document(orderID)
                                .collection("address").document("001").set(address);

                        int i = 1;
                        for(Order order: order_lists){
                            String ordered_product_ID = String.format("%03d", i);

                            Map<String, Object> ordered_product = new HashMap<>();
                            ordered_product.put("SKU", order.getSKU());
                            ordered_product.put("image", order.getProduct_img());
                            ordered_product.put("product_name", order.getProduct_name());
                            ordered_product.put("quantity", order.getQuantity());
                            ordered_product.put("price", order.getProduct_price());

                            db.collection("order").document(orderID)
                                    .collection("ordered_product").document(ordered_product_ID)
                                    .set(ordered_product)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");

                                            db.collection("customer").document("username")
                                                    .collection("cart").document(shopID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error deleting document", e);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

                            i++;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void fnGoOrder(View view) {
        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
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