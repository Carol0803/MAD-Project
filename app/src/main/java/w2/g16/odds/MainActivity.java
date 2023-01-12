package w2.g16.odds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import w2.g16.odds.authentication.LoginActivity;
import w2.g16.odds.databinding.ActivityMainBinding;
import w2.g16.odds.model.Category;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.UserEmail;
import w2.g16.odds.ordering.CartActivity;
import w2.g16.odds.ordering.OrderActivity;
import w2.g16.odds.product_browsing.CategoryAdapter;
import w2.g16.odds.product_browsing.ProductAdapter;
import w2.g16.odds.product_browsing.SearchActivity;
import w2.g16.odds.product_browsing.ViewCategoryActivity;
import w2.g16.odds.product_browsing.ViewProductActivity;
import w2.g16.odds.setting.SettingsActivity;
import w2.g16.odds.shop_recommendation.shop_recommendation;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Category category;
    private Vector<Category> categories;
    private CategoryAdapter adapter;
    private Vector<Products> products;
    private ProductAdapter adapterProduct;
    private String email;
    GpsTracker gpsTracker;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = UserEmail.getEmail(getApplicationContext());

        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            // Update one field, creating the document if it does not already exist.
            Map<String, Object> data = new HashMap<>();
            data.put("curLatitude", latitude);
            data.put("curLongitude", longitude);

            db.collection("customer").document(email)
                    .set(data, SetOptions.merge());

        }else{
            gpsTracker.showSettingsAlert();
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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
                                double price = Double.parseDouble(document.get("price").toString());
                                products.add(new Products(SKU, name, img, price, shopID));
                                adapterProduct.notifyItemInserted(products.size());
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
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
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