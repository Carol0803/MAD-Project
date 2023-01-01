package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import w2.g16.odds.MainActivity;
import w2.g16.odds.model.Cart;
import w2.g16.odds.model.Order;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCartBinding;
import w2.g16.odds.model.Shop;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    ArrayList<Shop> shop_list;
    List<Shop> shops;
//    Shop shop;
    private CartAdapter adapter;
    private Vector<Shop> shop_lists;
    private String selected_order_shopname;
    private ArrayList<Order> orders;
    private String total;
    private String deliver_by = null, SKU = null, name = null, variationID = null,
            variation_name = null, price = null, quantity = null, img = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int cart_item_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
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

                                shops.add(new Shop(shopID, shopname));

                                db.collection("customer/username/cart/" + shopID + "/cart_product")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String cartID = null, deliver_by = null, SKU = null, name = null, variationID = null,
                                                            variation_name = null, price = null, quantity = null, img = null;
                                                    carts.clear();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                                        deliver_by = document.get("deliver_by").toString();
                                                        SKU = document.getId();
                                                        name = document.get("product_name").toString();
                                                        price = document.get("product_price").toString();
                                                        quantity = document.get("quantity").toString();
                                                        img = document.get("product_image").toString();

                                                        //if(deliver_by.equals(shopID) ){
//                                                        variations.add (new Variation(variationID, variation_name, img, price, quantity));
//                                                        products.add(new Products(SKU, name, variations));
//                                                        carts.add(new Cart(shop, products));
                                                        carts.add(new Cart(shopID, SKU, name, img, price, quantity));
                                                            //adapter.notifyItemRangeInserted(adapter.getItemCount(),carts.size());
//                                                        binding.recCart.scrollToPosition(carts.size() - 1);
                                                        adapter.notifyItemChanged(carts.size()-1);

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
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
        shops = new ArrayList<>();

//        binding.recCart.setHasFixedSize(false);
        binding.recCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter();
        binding.recCart.setAdapter(adapter);

        final String TAG = "Read Data Activity";
        db.collection("customer/username/cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Shop shop = new Shop();
                                String shopID = document.getId();
                                String shopname = document.get("shop_name").toString();

                                shop.setShopID(shopID);
                                shop.setShopname(shopname);

//                                ArrayList<Cart> carts = new ArrayList<Cart>();
//                                carts = readCartProduct(shopID);
                                shops.add(shop);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        for(int i=0; i<shops.size(); i++) {

                            String shop_id = shops.get(i).getShopID();
                            String shop_name = shops.get(i).getShopname();

                            List<Cart> carts = new ArrayList<Cart>();
                            shop_list = new ArrayList<Shop>();
                            db.collection("customer/username/cart/" + shop_id + "/cart_product")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
//                                                    List<Cart> carts = new ArrayList<Cart>();
//                                                    carts.clear();
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                                    Cart cart = new Cart();

                                                    deliver_by = document.get("deliver_by").toString();
                                                    SKU = document.getId();
                                                    name = document.get("product_name").toString();
                                                    price = document.get("product_price").toString();
                                                    quantity = document.get("quantity").toString();
                                                    img = document.get("product_image").toString();

                                                    cart.setShopID(deliver_by);
                                                    cart.setSKU(SKU);
                                                    cart.setProduct_name(name);
                                                    cart.setPrice(price);
                                                    cart.setQuantity(quantity);
                                                    cart.setImage(img);

                                                    carts.add(cart);
                                                }
                                                Shop shop = new Shop();

                                                shop.setShopID(shop_id);
                                                shop.setShopname(shop_name);
                                                shop.setCartList(carts);
//                                                shop_list.clear();
                                                shop_list.add(shop);
                                                adapter.setParentItemList(shop_list);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                    }
                });


//        shop_lists = new Vector<>();
//        shops = new Vector<>();
//        carts = new Vector<>();



    }

    public void fnCheckout(View view) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("shopname", selected_order_shopname);
        intent.putExtra("order", orders);
        intent.putExtra("total", total);
        startActivity(intent);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            total = intent.getStringExtra("total");
            selected_order_shopname = intent.getStringExtra("shopname");
            orders = (ArrayList<Order>) intent.getSerializableExtra("order");
            binding.tvTotalAmount.setText("RM: " + total);
        }
    };

    /*public ArrayList<Cart> readCartProduct(String shopID){
        final String TAG = "Read Data Activity";

        return carts;
    }*/
}
