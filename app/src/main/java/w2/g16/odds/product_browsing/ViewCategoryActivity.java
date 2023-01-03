package w2.g16.odds.product_browsing;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Vector;

import w2.g16.odds.ProductAdapter;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityViewCategoryBinding;
import w2.g16.odds.model.Products;

public class ViewCategoryActivity extends AppCompatActivity {

    private ActivityViewCategoryBinding binding;
    private String categoryID, category_name;
    private Vector<Products> products;
    private ProductAdapter adapterProduct;
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

        Intent intent = getIntent();
        categoryID = intent.getStringExtra("category_ID");


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
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String SKU = document.getId();
                                String name = document.get("product_name").toString();
                                String img = document.get("image").toString();
                                String price = document.get("price").toString();
                                int sold_item = Integer.parseInt(document.get("sold_item").toString());

                                products.add(new Products(SKU, name, img, price, null, sold_item));
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
    }
}