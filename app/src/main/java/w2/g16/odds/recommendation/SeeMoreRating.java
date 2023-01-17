package w2.g16.odds.recommendation;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import w2.g16.odds.GpsTracker;
import w2.g16.odds.R;
import w2.g16.odds.model.Shop;
import w2.g16.odds.model.UserEmail;

public class SeeMoreRating extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Shop> shopArrayList;
    MyAdapterHRating myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    private String state2;
    private String email;
    GpsTracker gpsTracker;

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

        db = FirebaseFirestore.getInstance();
        email = UserEmail.getEmail(getApplicationContext());

        DocumentReference docRef = db.collection("customer").document(email)
                .collection("address").document("001");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document2 = task.getResult();
                    if (document2.exists()) {

                        state2 = document2.get("state").toString();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shopArrayList = new ArrayList<Shop>();
        myAdapter = new MyAdapterHRating(SeeMoreRating.this, shopArrayList);

        recyclerView.setAdapter(myAdapter);
        showRating();
    }

    private void showRating() {

        gpsTracker = new GpsTracker(SeeMoreRating.this);
        if(gpsTracker.canGetLocation()) {
            db.collection("shop").whereGreaterThanOrEqualTo("shop_rating", 4.5)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            if (error != null) {
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();

                                Log.e("Error with Firestore", error.getMessage());
                                return;
                            }

                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {

                                    double lat1 = Double.parseDouble(dc.getDocument().get("shop_latitude").toString());
                                    double lon1 = Double.parseDouble(dc.getDocument().get("shop_longitude").toString());

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

                                    //display products that owned by shop in 50km radius from user
                                    if (dist <= 50 ) {
                                        Shop shop = dc.getDocument().toObject(Shop.class);
                                        shop.setShopID(dc.getDocument().getId());
                                        shopArrayList.add(shop);
                                    }
                                }

                                myAdapter.notifyDataSetChanged();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    });
        }
        else{
            db.collection("shop")
                    .whereGreaterThanOrEqualTo("shop_rating", 4.5)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            final String TAG = "Read Data Activity";
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    String shop_state = document.get("shop_state").toString();
                                    String shopID = document.getId();
                                    String shopname = document.get("shop_name").toString();
                                    String shop_open = document.get("shop_open").toString();
                                    String shop_close = document.get("shop_close").toString();
                                    double shop_rating = Double.parseDouble(document.get("shop_rating").toString());


                                    if(state2.equals(shop_state)) {
                                        shopArrayList.add(new Shop(shopID, shopname, shop_open, shop_close, shop_rating));
                                        myAdapter.notifyItemInserted(shopArrayList.size());
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}