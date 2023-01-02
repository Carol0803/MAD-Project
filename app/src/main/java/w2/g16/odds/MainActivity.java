package w2.g16.odds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Vector;

import w2.g16.odds.databinding.ActivityMainBinding;
import w2.g16.odds.model.Category;
import w2.g16.odds.model.Products;
import w2.g16.odds.ordering.CartActivity;
import w2.g16.odds.ordering.OrderActivity;
import w2.g16.odds.product_browsing.SearchActivity;
import w2.g16.odds.product_browsing.ViewCategoryActivity;
import w2.g16.odds.product_browsing.ViewProductActivity;
import w2.g16.odds.shop_recommendation.shop_recommendation;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Category category;
    private Vector<Category> categories;
    private CategoryAdapter adapter;
//    private Product product;
    private Vector<Products> products;
    private ProductAdapter adapterProduct;
    //private String name, shopname, img, price;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        binding.btmNav.setSelectedItemId(com.google.android.material.R.id.home);
        binding.btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.recommendation:
                        startActivity(new Intent(getApplicationContext(), shop_recommendation.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.order:
                        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                        overridePendingTransition(0,0);
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
        db.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String categoryID = document.getId();
                                String name = document.get("category_name").toString();
                                String img = document.get("icon_url").toString();

                                categories.add(new Category(categoryID, name, img));
                                adapter.notifyItemInserted(categories.size());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        categories = new Vector<>();
        adapter = new CategoryAdapter(this, getLayoutInflater(), categories);

        binding.recCategory.setAdapter(adapter);
        binding.recCategory.setLayoutManager(new LinearLayoutManager(this,  RecyclerView.HORIZONTAL, false));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2,
                new IntentFilter("category_ID"));

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String SKU = document.getId();
                                String name = document.get("product_name").toString();
                                String shopID = document.get("owned by").toString();
                                String img = document.get("image").toString();
                                String price = document.get("price").toString();
                                products.add(new Products(SKU, name, img, price, shopID));
                                adapterProduct.notifyItemInserted(products.size());


//                                    products.add(new Product(SKU, name, price, null, null, img, shopname));
//                                    adapterProduct.notifyItemInserted(products.size());
//                                }

                             /*   DocumentReference docRef = db.collection("products").document(SKU)
                                        .collection("variation").document("001");
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                String img = document.get("variation_image").toString();
                                                String price = document.get("variation_price").toString();

                                                products.add(new Product(SKU, name, price, img, shopname));
                                                adapterProduct.notifyItemInserted(products.size());
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });*/

                              /*  db.collection("product/"+SKU+"/variation")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                                        if(document.getId() == "001") {
                                                            //String name = document.get("product_name").toString();
                                                            //String shopname = document.get("owned by").toString();
                                                            String img = document.get("variation_image").toString();
                                                            String price = document.get("variation_price").toString();

                                                            Log.d(TAG, name + shopname + img + price);

                                                            products.add(new Product(name, price, null, null, img, shopname));
                                                            adapterProduct.notifyItemInserted(products.size());
                                                        }

                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });*/
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        products = new Vector<>();
        adapterProduct = new ProductAdapter(this, getLayoutInflater(), products);

        binding.recProduct.setAdapter(adapterProduct);
        binding.recProduct.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("product_SKU"));
    }

    public void fnGoCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String SKU = intent.getStringExtra("SKU");
            Intent intent2 = new Intent(getApplicationContext(), ViewProductActivity.class);
            intent2.putExtra("SKU", SKU);
            startActivity(intent2);
        }
    };

    public BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String categoryID = intent.getStringExtra("category_ID");
            Intent intent3 = new Intent(getApplicationContext(), ViewCategoryActivity.class);
            intent3.putExtra("category_ID", categoryID);
            startActivity(intent3);
        }
    };
}