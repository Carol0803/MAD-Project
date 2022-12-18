package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private String shopID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        final String TAG = "Read Data Activity";
        db.collection("customer/username/cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                shopID = document.getId();
                                String shopname = document.get("shop_name").toString();

                                shops.add(new Shop(shopID, shopname));

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

//                                                        variations.add (new Variation(variationID, variation_name, img, price, quantity));
//                                                        products.add(new Products(SKU, name, variations));
//                                                        carts.add(new Cart(shop, products));
                                                        carts.add(new Cart(SKU, name, variationID, variation_name, img, price, quantity));
                                                        //adapter.notifyItemRangeInserted(adapter.getItemCount(),carts.size());
                                                        adapter.notifyItemChanged(carts.size());
                                                        //adapter.notifyItemChanged(recipePosition);
                                                        //binding.recCart.getAdapter().notifyItemInserted(recipe.getDishs().size()-1);

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

        shops = new Vector<>();
        carts = new Vector<>();
        adapter = new CartAdapter(this, shops, carts);

        binding.recCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recCart.setAdapter(adapter);


    }

    public void fnCheckout(View view) {
        startActivity(new Intent(this, CheckoutActivity.class));
    }
}