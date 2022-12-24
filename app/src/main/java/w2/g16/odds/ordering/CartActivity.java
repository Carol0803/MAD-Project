package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import w2.g16.odds.MainActivity;
import w2.g16.odds.model.Cart;
import w2.g16.odds.model.Order;
import w2.g16.odds.model.Product;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCartBinding;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Shop;
import w2.g16.odds.model.Variation;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;

    private Vector<Cart> carts;
    private Vector<Shop> shops;
    private CartAdapter adapter;
    private Vector<String> shop_lists;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int cart_item_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //set toolbar as actionbar
        //back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("return_total"));
        /*final String TAG = "Read Data Activity";
        db.collection("customer/username/cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String shopID = document.getId();
                                String shopname = document.get("shop_name").toString();

                                db.collection("customer/username/cart/" + shopID + "/cart_product")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                                        String SKU = document.get("SKU").toString();
                                                        String name = document.get("product_name").toString();
                                                        String variationID = document.get("variationID").toString();
                                                        String variation_name = document.get("variation_name").toString();
                                                        String price = document.get("product_price").toString();
                                                        String quantity = document.get("quantity").toString();
                                                        String img = document.get("product_image").toString();

//                                                        Shop shop = new Shop(shopID, shopname);
//                                                        variations.add (new Variation(variationID, variation_name, img, price, quantity));
//                                                        products.add(new Products(SKU, name, variations));
//                                                        carts.add(new Cart(shop, products));
                                                        carts.add(new Cart(shopname, SKU, name, variationID, variation_name, img, price, quantity));
                                                        adapter.notifyItemInserted(carts.size());

                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        carts = new Vector<>();
        adapter = new CartAdapter(getLayoutInflater(), carts);

        binding.recCart.setAdapter(adapter);
        binding.recCart.setLayoutManager(new LinearLayoutManager(this));*/

        /*CollectionReference collection = db.collection("customer/username/cart");
        AggregateQuery countQuery = collection.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                Log.d(TAG, "Count: " + snapshot.getCount());
                cart_item_count = Long.valueOf(snapshot.getCount()).intValue();
            } else {
                Log.d(TAG, "Count failed: ", task.getException());
            }
        });*/

        final String TAG = "Read Data Activity";
        db.collection("customer/username/cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String shopID = document.getId();
                                String shopname = document.get("shop_name").toString();

                                shops.add(new Shop(shopID, shopname));
//                                shop_lists.add(shopID);

                                db.collection("customer/username/cart/" + shopID + "/cart_product")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String cartID = null, deliver_by = null, SKU = null, name = null, variationID = null,
                                                            variation_name = null, price = null, quantity = null, img = null;
//                                                    carts.clear();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                                        cartID = document.getId();
                                                        deliver_by = document.get("deliver_by").toString();
                                                        SKU = document.get("SKU").toString();
                                                        name = document.get("product_name").toString();
                                                        variationID = document.get("variationID").toString();
                                                        variation_name = document.get("variation_name").toString();
                                                        price = document.get("product_price").toString();
                                                        quantity = document.get("quantity").toString();
                                                        img = document.get("product_image").toString();

                                                        //if(deliver_by.equals(shopID) ){
//                                                        variations.add (new Variation(variationID, variation_name, img, price, quantity));
//                                                        products.add(new Products(SKU, name, variations));
//                                                        carts.add(new Cart(shop, products));
                                                        carts.add(new Cart(shopID, cartID, SKU, name, variationID, variation_name, img, price, quantity));
                                                            //adapter.notifyItemRangeInserted(adapter.getItemCount(),carts.size());
                                                        adapter.notifyItemChanged(carts.size());

                                                            //adapter.notifyItemChanged(recipePosition);
                                                            //binding.recCart.getAdapter().notifyItemInserted(recipe.getDishs().size()-1);
                                                        //}
                                                    }
//                                                    carts = new Vector<>();

                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

//                                CollectionReference itemsRef = db.collection("cart_product");
//                                Query query = itemsRef.whereEqualTo("deliver_by", shopID);


                            }
                        /*    for(String shop_list : shop_lists){
                                db.collection("customer/username/cart/" + shop_list + "/cart_product")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String cartID = null, deliver_by = null, SKU = null, name = null, variationID = null,
                                                            variation_name = null, price = null, quantity = null, img = null;
//                                                    carts.clear();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                                        cartID = document.getId();
                                                        deliver_by = document.get("deliver_by").toString();
                                                        SKU = document.get("SKU").toString();
                                                        name = document.get("product_name").toString();
                                                        variationID = document.get("variationID").toString();
                                                        variation_name = document.get("variation_name").toString();
                                                        price = document.get("product_price").toString();
                                                        quantity = document.get("quantity").toString();
                                                        img = document.get("product_image").toString();

                                                        //if(deliver_by.equals(shopID) ){
//                                                        variations.add (new Variation(variationID, variation_name, img, price, quantity));
//                                                        products.add(new Products(SKU, name, variations));
//                                                        carts.add(new Cart(shop, products));
                                                        carts.add(new Cart(shop_list, cartID, SKU, name, variationID, variation_name, img, price, quantity));
                                                        //adapter.notifyItemRangeInserted(adapter.getItemCount(),carts.size());

                                                        //adapter.notifyItemChanged(recipePosition);
                                                        //binding.recCart.getAdapter().notifyItemInserted(recipe.getDishs().size()-1);
                                                        //}
                                                    }
                                                    adapter.notifyItemChanged(carts.size());
                                                    carts = new Vector<>();

                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }*/
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//        shop_lists = new Vector<>();
        shops = new Vector<>();
        carts = new Vector<>();
        adapter = new CartAdapter(this, shops, carts);

        binding.recCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recCart.setAdapter(adapter);
    }

    public void fnCheckout(View view) {
//        Intent intent = new Intent();
//        intent.putExtra("objAddress", address_chosen);
        startActivity(new Intent(this, CheckoutActivity.class));
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String total = intent.getStringExtra("total");
//            List<Order> items = (List<Order>) intent.getSerializableExtra("order");
            binding.tvTotalAmount.setText("RM: " + total);
        }
    };
    // https://stackoverflow.com/questions/35008860/how-to-pass-values-from-recycleadapter-to-mainactivity-or-other-activities
}