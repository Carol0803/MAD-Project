package w2.g16.odds.browsing;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Vector;

import w2.g16.odds.GpsTracker;
import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityViewCategoryBinding;
import w2.g16.odds.model.PriceComparator;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.RatingComparator;
import w2.g16.odds.model.UserEmail;

public class ViewCategoryActivity extends AppCompatActivity {

    private ActivityViewCategoryBinding binding;
    private String categoryID, category_name;
    private Vector<Products> products;
    private Vector<Products> rated_products;
    private ProductAdapter adapterProduct;
    private double dist;
    private String email;
    GpsTracker gpsTracker;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        email = UserEmail.getEmail(getApplicationContext());

        Intent intent = getIntent();
        categoryID = intent.getStringExtra("category_ID");

        binding.btnSortPrice.setBackgroundColor(Color.parseColor("#FF4E457D"));
        binding.btnSortPrice.setTextColor(Color.WHITE);
        binding.lytPrice.setVisibility(View.VISIBLE);
        binding.lytRating.setVisibility(View.GONE);
        binding.rbHigh.setChecked(true);

        DocumentReference docRef = db.collection("category").document(categoryID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        category_name = document.get("category_name").toString();
                        binding.tvTitle.setText(category_name);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        db.collection("products")
                .whereEqualTo("under_category", categoryID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String SKU = document.getId();
                                String name = document.get("product_name").toString();
                                String img = document.get("image").toString();
                                double rating = Double.parseDouble(document.get("product_rating").toString());
                                double price = Double.parseDouble(document.get("price").toString());
                                int sold_item = Integer.parseInt(document.get("sold_item").toString());
                                String shopID = document.get("owned by").toString();

                                gpsTracker = new GpsTracker(ViewCategoryActivity.this);
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
                                                        products.add(new Products(SKU, name, img, price, shopID, sold_item, rating));
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
                                                                        products.add(new Products(SKU, name, img, price, shopID, sold_item, rating));
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
                            Collections.sort(products,new PriceComparator().reversed());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        products = new Vector<>();
        adapterProduct = new ProductAdapter(this, getLayoutInflater(), products);

        binding.recProduct.setAdapter(adapterProduct);
        binding.recProduct.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));

        binding.btnSortPrice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                binding.btnSortPrice.setBackgroundColor(Color.parseColor("#FF4E457D"));
                binding.btnSortPrice.setTextColor(Color.WHITE);
                binding.btnSortRating.setBackgroundColor(Color.WHITE);
                binding.btnSortRating.setTextColor(Color.BLACK);
                binding.btnSortBestSeller.setBackgroundColor(Color.WHITE);
                binding.btnSortBestSeller.setTextColor(Color.BLACK);
                binding.lytPrice.setVisibility(View.VISIBLE);
                binding.lytRating.setVisibility(View.GONE);
                binding.rbHigh.setChecked(true);

                Collections.sort(products,new PriceComparator().reversed());
                adapterProduct = new ProductAdapter(ViewCategoryActivity.this, getLayoutInflater(), products);
                binding.recProduct.setAdapter(adapterProduct);
                binding.recProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));
            }
        });

        binding.btnSortRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnSortRating.setBackgroundColor(Color.parseColor("#FF4E457D"));
                binding.btnSortRating.setTextColor(Color.WHITE);
                binding.btnSortPrice.setBackgroundColor(Color.WHITE);
                binding.btnSortPrice.setTextColor(Color.BLACK);
                binding.btnSortBestSeller.setBackgroundColor(Color.WHITE);
                binding.btnSortBestSeller.setTextColor(Color.BLACK);
                binding.lytRating.setVisibility(View.VISIBLE);
                binding.lytPrice.setVisibility(View.GONE);
                binding.rbRatingHigh.setChecked(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(products,new RatingComparator().reversed());
                }
                adapterProduct = new ProductAdapter(ViewCategoryActivity.this, getLayoutInflater(), products);
                binding.recProduct.setAdapter(adapterProduct);
                binding.recProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));
            }
        });

        binding.btnSortBestSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnSortBestSeller.setBackgroundColor(Color.parseColor("#FF4E457D"));
                binding.btnSortBestSeller.setTextColor(Color.WHITE);
                binding.btnSortPrice.setBackgroundColor(Color.WHITE);
                binding.btnSortPrice.setTextColor(Color.BLACK);
                binding.btnSortRating.setBackgroundColor(Color.WHITE);
                binding.btnSortRating.setTextColor(Color.BLACK);
                binding.lytPrice.setVisibility(View.GONE);
                binding.lytRating.setVisibility(View.GONE);

                Collections.sort(products);
                adapterProduct = new ProductAdapter(ViewCategoryActivity.this, getLayoutInflater(), products);

                binding.recProduct.setAdapter(adapterProduct);
                binding.recProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRbPriceHighClicked(View view) {
        Collections.sort(products,new PriceComparator().reversed());
        adapterProduct = new ProductAdapter(this, getLayoutInflater(), products);
        binding.recProduct.setAdapter(adapterProduct);
        binding.recProduct.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
    }

    public void onRbPriceLowClicked(View view) {
        Collections.sort(products,new PriceComparator());
        adapterProduct = new ProductAdapter(this, getLayoutInflater(), products);
        binding.recProduct.setAdapter(adapterProduct);
        binding.recProduct.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRbRatingHighClicked(View view) {
        Collections.sort(products,new RatingComparator().reversed());
        adapterProduct = new ProductAdapter(this, getLayoutInflater(), products);
        binding.recProduct.setAdapter(adapterProduct);
        binding.recProduct.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
    }

    public void onRbRatingLowClicked(View view) {
        Collections.sort(products,new RatingComparator());
        adapterProduct = new ProductAdapter(this, getLayoutInflater(), products);
        binding.recProduct.setAdapter(adapterProduct);
        binding.recProduct.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
    }

}