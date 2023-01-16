package w2.g16.odds.recommendation;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import w2.g16.odds.GpsTracker;
import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.model.Order;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Shop;
import w2.g16.odds.model.UserEmail;
import w2.g16.odds.ordering.OrderActivity;
import w2.g16.odds.setting.SettingsActivity;

public class RecommendationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView ratingRecyclerview;
    RecyclerView nearbyRecyclerview;

    ArrayList<Shop> shopArrayList;
    ArrayList<Shop> shopArrayList2;
    ArrayList<Shop> shopArrayList3;

    MyAdapterHPopular myAdapter;
    MyAdapterHRating myAdapterHRating;
    MyAdapterHDistance myAdapterDistance;

    Button seePopular;
    Button seeRating;
    Button seeDistance;
    ImageView seeSetting;
    BottomNavigationView btmNav;
    ProgressDialog progressDialog;
    TextView tvNoGps;

    private String email;
    private int quantity = 0;
    private double dist;
    private int count = 0;
    GpsTracker gpsTracker;
    ArrayList<Order> orders = new ArrayList<Order>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btmNav = findViewById(R.id.btm_nav);
        btmNav.setSelectedItemId(R.id.recommendation);
        btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.recommendation:
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

        email = UserEmail.getEmail(getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data.....");
        progressDialog.show();

//        recyclerView = findViewById(R.id.recyclerview);
//        shopPopular();

        ratingRecyclerview = findViewById(R.id.recyclerview2);
        shopRating();

        nearbyRecyclerview = findViewById(R.id.recyclerview3);
        shopNearby();

//        seePopular = findViewById(R.id.tvseemore);
//        seePopular.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                seeMore();
//            }
//        });

        seeRating = findViewById(R.id.tvseerating);
        seeRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeMoreRating();
            }
        });

        seeDistance = findViewById(R.id.tvseedistance);
        seeDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeMoreDistance();
            }
        });
    }
/*

    private void shopPopular() {
//        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        shopArrayList = new ArrayList<Shop>();
        myAdapter = new MyAdapterHPopular(getApplicationContext(), shopArrayList);
        recyclerView.setAdapter(myAdapter);

        ArrayList<String> shops = new ArrayList<String>();
        db.collection("shop")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                shops.add(document.getId());
                            }

                            for(int i = 0; i < shops.size(); i++)
                            {
                                String shop = shops.get(i);
                                ArrayList<String> order_lists = new ArrayList<String>();
                                int finalI = i;
                                int finalI1 = i;
                                db.collection("order")
                                        .whereEqualTo("order_from", shops.get(i))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @RequiresApi(api = Build.VERSION_CODES.N)
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                                        order_lists.add(document.getId());
                                                    }

                                                    for (int j = 0; j < order_lists.size(); j++)
                                                    {
                                                        String order_list = order_lists.get(j);
                                                        db.collection("order").document(order_lists.get(j))
                                                                .collection("ordered_product")
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                Log.d(TAG, document.getId() + " => " + document.getData());

                                                                                quantity += Integer.parseInt(document.get("quantity").toString());
                                                                            }
                                                                        } else {
                                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    orders.add(new Order(shop, "" + quantity));

                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        sort(orders);
                                                    }

                                                    for(int i = 0; i < orders.size(); i++)
                                                    {
                                                        Order order = orders.get(i);
                                                        DocumentReference docRef = db.collection("shop").document(order.getShopname());
                                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {
                                                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                                        String shopname = document.get("shop_name").toString();
                                                                        String shop_open = document.get("shop_open").toString();
                                                                        String shop_close = document.get("shop_close").toString();
                                                                        String item_sold = order.getQuantity();

                                                                        shopArrayList.add(new Shop(shopname, shop_open, shop_close, item_sold));
                                                                        myAdapter.notifyItemInserted(shopArrayList.size());

                                                                        if (progressDialog.isShowing())
                                                                            progressDialog.dismiss();

                                                                    } else {
                                                                        Log.d(TAG, "No such document");
                                                                    }
                                                                } else {
                                                                    Log.d(TAG, "get failed with ", task.getException());
                                                                }
                                                            }
                                                        });
                                                    }

                                                    shops.remove(shop);
                                                }
                                                else{
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
    }
*/

    private void shopRating() {
//        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        ratingRecyclerview.setLayoutManager(layoutManager2);

        shopArrayList2 = new ArrayList<Shop>();
        myAdapterHRating = new MyAdapterHRating(getApplicationContext(), shopArrayList2);
        ratingRecyclerview.setAdapter(myAdapterHRating);

        db.collection("shop").limit(3).whereGreaterThanOrEqualTo("shop_rating", 4.5)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null)
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Error with Firestore",error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges())
                        {
                            if(dc.getType() == DocumentChange.Type.ADDED)
                            {
                                Shop shop = dc.getDocument().toObject(Shop.class);
                                shop.setShopID(dc.getDocument().getId());
                                shopArrayList2.add(shop);
                            }

                            myAdapterHRating.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
/*
        db.collection("shop")
                .limit(3)
                .whereGreaterThanOrEqualTo("shop_rating", 4.5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String shopname = document.get("shop_name").toString();
                                String shop_open = document.get("shop_open").toString();
                                String shop_close = document.get("shop_close").toString();
                                String shop_rating = document.get("shop_rating").toString();

                                shopArrayList3.add(new Shop(shopname, shop_open, shop_close, shop_rating));
                                myAdapter.notifyItemInserted(shopArrayList3.size());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
    }

    private void shopNearby() {
//        nearbyRecyclerview.setHasFixedSize(false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        nearbyRecyclerview.setLayoutManager(layoutManager3);

        shopArrayList3 = new ArrayList<Shop>();
        myAdapterDistance = new MyAdapterHDistance(getApplicationContext(), shopArrayList3);
        nearbyRecyclerview.setAdapter(myAdapterDistance);

        /*db.collection("shop").orderBy("shop_rating", Query.Direction.DESCENDING).limit(3)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null)
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Error with Firestore",error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges())
                        {
                            if(dc.getType() == DocumentChange.Type.ADDED)
                            {
                                shopArrayList3.add(dc.getDocument().toObject(Shop.class));
                            }

                            myAdapterDistance.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });*/

        gpsTracker = new GpsTracker(RecommendationActivity.this);
        if(gpsTracker.canGetLocation()) {
            db.collection("shop")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();

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

                                    if (dist <= 10 && shopArrayList3.size()<3) {
                                        String shopID = document.getId();
                                        String shopname = document.get("shop_name").toString();
                                        String shop_open = document.get("shop_open").toString();
                                        String shop_close = document.get("shop_close").toString();

                                        shopArrayList3.add(new Shop(shopID, shopname, shop_open, shop_close, dist));
                                        myAdapterDistance.notifyItemInserted(shopArrayList3.size());
                                    }
                                    else
                                        break;
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else{
            if(progressDialog.isShowing())
                progressDialog.dismiss();

            tvNoGps = findViewById(R.id.tvNoGps);
            tvNoGps.setVisibility(View.VISIBLE);

            nearbyRecyclerview.setVisibility(View.GONE);
        }
    }

    private void seeMoreRating()
    {
        Intent intent = new Intent(RecommendationActivity.this, SeeMoreRating.class);
        startActivity(intent);
    }

    private void seeMoreDistance()
    {
        Intent intent = new Intent(RecommendationActivity.this, SeeMoreDistance.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sort(ArrayList<Order> list) {

        list.sort((o1, o2)
                -> o1.getQuantity().compareTo(
                o2.getQuantity()));
    }
}