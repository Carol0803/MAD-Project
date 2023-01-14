package w2.g16.odds;

import static android.content.ContentValues.TAG;

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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import w2.g16.odds.databinding.ActivityMainBinding;
import w2.g16.odds.model.Category;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.UserEmail;
import w2.g16.odds.ordering.CartActivity;
import w2.g16.odds.ordering.OrderActivity;
import w2.g16.odds.browsing.CategoryAdapter;
import w2.g16.odds.browsing.ProductAdapter;
import w2.g16.odds.browsing.SearchActivity;
import w2.g16.odds.browsing.ViewCategoryActivity;
import w2.g16.odds.browsing.ViewProductActivity;
import w2.g16.odds.recommendation.RecommendationActivity;
import w2.g16.odds.setting.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Category category;
    private Vector<Category> categories;
    private CategoryAdapter adapter;
    private Vector<Products> products;
    private ProductAdapter adapterProduct;
    private String email;
    GpsTracker gpsTracker;
    Executor executor;
    Handler handler;
    private double dist;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
            Intent i = new Intent(MainActivity.this, HomePage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        email = UserEmail.getEmail(getApplicationContext());

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

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
                        startActivity(new Intent(getApplicationContext(), RecommendationActivity.class));
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

                                gpsTracker = new GpsTracker(MainActivity.this);
                                if(gpsTracker.canGetLocation()) {

                                    DocumentReference docRef = db.collection("shop").document(shopID);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    double lat1 = Double.parseDouble(document.get("shop_latitude").toString());
                                                    double lon1 = Double.parseDouble(document.get("shop_longitude").toString());

                                                    double lat2 = gpsTracker.getLatitude();
                                                    double lon2 = gpsTracker.getLongitude();

                                                    // distance between latitudes and longitudes
                                                    double dLat = Math.toRadians(lat2 - lat1);
                                                    double dLon = Math.toRadians(lon2 - lon1);

                                                    // convert to radians
                                                    lat1 = Math.toRadians(lat1);
                                                    lat2 = Math.toRadians(lat2);

                                                    // apply formulae
                                                    double a = Math.pow(Math.sin(dLat / 2), 2) +
                                                            Math.pow(Math.sin(dLon / 2), 2) *
                                                                    Math.cos(lat1) *
                                                                    Math.cos(lat2);
                                                    double rad = 6371;
                                                    double c = 2 * Math.asin(Math.sqrt(a));
                                                    dist = (rad * c);
                                                    int n = 3;
                                                    dist = Math.round(dist * Math.pow(10, n))
                                                            / Math.pow(10, n);

                                                    //display products that owned by shop in 50km radius from user
                                                    if (dist <= 50) {
                                                        products.add(new Products(SKU, name, img, price, shopID));
                                                        adapterProduct.notifyItemInserted(products.size());
                                                    }

                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                                else{
                                    DocumentReference docRef = db.collection("shop").document(shopID);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    String shop_state = document.get("shop_state").toString();

                                                    DocumentReference docRef = db.collection("customer").document(email)
                                                            .collection("address").document("001");
                                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {

                                                                    String state = document.get("state").toString();

                                                                    if(state.equals(shop_state)) {
                                                                        products.add(new Products(SKU, name, img, price, shopID));
                                                                        adapterProduct.notifyItemInserted(products.size());
                                                                    }

                                                                } else {
                                                                    Log.d(TAG, "No such document");
                                                                }
                                                            } else {
                                                                Log.d(TAG, "get failed with ", task.getException());
                                                            }
                                                        }
                                                    });

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