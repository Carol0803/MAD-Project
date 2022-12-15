package w2.g16.odds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import w2.g16.odds.databinding.ActivityMainBinding;
import w2.g16.odds.model.Category;
import w2.g16.odds.model.Product;
import w2.g16.odds.ordering.CartActivity;
import w2.g16.odds.ordering.OrderActivity;
import w2.g16.odds.shop_recommendation.shop_recommendation;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Category category;
    private Vector<Category> categories;
    private CategoryAdapter adapter;
    private Product product;
    private Vector<Product> products;
    private ProductAdapter adapterProduct;
    //private String name, shopname, img, price;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                    case R.id.notification:
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

                                String name = document.get("category_name").toString();
                                String img = document.get("icon_url").toString();

                                categories.add(new Category(name, img));
                                adapter.notifyItemInserted(categories.size());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        categories = new Vector<>();
        adapter = new CategoryAdapter(getLayoutInflater(), categories);

        binding.recCategory.setAdapter(adapter);
        binding.recCategory.setLayoutManager(new LinearLayoutManager(this,  RecyclerView.HORIZONTAL, false));


        db.collection("product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String SKU = document.getId();
                                String name = document.get("product_name").toString();
                                String shopname = document.get("owned by").toString();
                                //img = document.get("variation_image").toString();
                                //price = document.get("variation_price").toString();

                                /*if(document.getId() == "001"){
                                    String img = document.get("variation_image").toString();
                                    String price = document.get("variation_price").toString();

                                    products.add(new Product(name, price, null, null, img, shopname));
                                    adapterProduct.notifyItemInserted(products.size());
                                }*/

                                DocumentReference docRef = db.collection("product").document(SKU)
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

                                                products.add(new Product(name, price, img, shopname));
                                                adapterProduct.notifyItemInserted(products.size());
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });

                                /*db.collection("product/"+SKU+"/variation")
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
        adapterProduct = new ProductAdapter(getLayoutInflater(), products);

        binding.recProduct.setAdapter(adapterProduct);
        binding.recProduct.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));


    }

    public void fnGoCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }
}