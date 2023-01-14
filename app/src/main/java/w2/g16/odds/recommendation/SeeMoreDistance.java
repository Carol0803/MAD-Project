/*
package w2.g16.odds.recommendation;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import w2.g16.odds.GpsTracker;
import w2.g16.odds.R;
import w2.g16.odds.model.Shop;

public class SeeMoreDistance extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Shop> shopArrayList;
    MyAdapterHDistance myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    GpsTracker gpsTracker;
    TextView tvNoGps;
    private double dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seemoredistance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data.....");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        shopArrayList = new ArrayList<Shop>();
        myAdapter = new MyAdapterHDistance(this, shopArrayList);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        gpsTracker = new GpsTracker(SeeMoreDistance.this);
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

                                    if (dist <= 10) {
                                        String shopname = document.get("shop_name").toString();
                                        String shop_open = document.get("shop_open").toString();
                                        String shop_close = document.get("shop_close").toString();

                                        shopArrayList.add(new Shop(shopname, shop_open, shop_close, dist));
                                        myAdapter.notifyItemInserted(shopArrayList.size());
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

            recyclerView.setVisibility(View.GONE);
        }
    }

    public void showDistance() {


    }
}*/

package w2.g16.odds.recommendation;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import w2.g16.odds.GpsTracker;
import w2.g16.odds.R;
import w2.g16.odds.model.Shop;

public class SeeMoreDistance extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Shop> shopArrayList;
    MyAdapterHDistance myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seemorerating);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data.....");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        shopArrayList = new ArrayList<Shop>();
        myAdapter = new MyAdapterHDistance(this, shopArrayList);
        recyclerView.setAdapter(myAdapter);
        showRating();
    }

    private void showRating() {

        GpsTracker gpsTracker = new GpsTracker(SeeMoreDistance.this);
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
                                    double dist = (rad * c);
                                    int n = 3;
                                    dist = Math.round(dist * Math.pow(10, n))
                                            / Math.pow(10, n);

                                    if (dist <= 10) {
                                        String shopID = document.getId();
                                        String shopname = document.get("shop_name").toString();
                                        String shop_open = document.get("shop_open").toString();
                                        String shop_close = document.get("shop_close").toString();

                                        shopArrayList.add(new Shop(shopID, shopname, shop_open, shop_close, dist));
                                        myAdapter.notifyItemInserted(shopArrayList.size());
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

            TextView tvNoGps = findViewById(R.id.tvNoGps);
            tvNoGps.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.GONE);
        }
    }
}
