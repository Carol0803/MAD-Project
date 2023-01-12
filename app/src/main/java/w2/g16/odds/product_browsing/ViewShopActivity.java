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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityViewShopBinding;
import w2.g16.odds.model.Products;

public class ViewShopActivity extends AppCompatActivity {

    private ActivityViewShopBinding binding;
    private String shopID, shopname, rating, sold_item;
    private Vector<Products> products;
    private ProductAdapter adapterProduct;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewShopBinding.inflate(getLayoutInflater());
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
        shopID = intent.getStringExtra("shopID");

        DocumentReference docRef2 = db.collection("shop").document(shopID);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        shopname = document.get("shop_name").toString();
                        rating = document.get("shop_rating").toString();

                        binding.tvShopName.setText(shopname);
                        binding.ratingBar.setRating(Float.parseFloat(rating));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        db.collection("products")
                .whereEqualTo("owned by", shopID)
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
                                double rating = Double.parseDouble(document.get("product_rating").toString());
                                double price = Double.parseDouble(document.get("price").toString());
                                int sold_item = Integer.parseInt(document.get("sold_item").toString());

                                products.add(new Products(SKU, name, img, price, shopID, sold_item, rating));
                                adapterProduct.notifyItemInserted(products.size());
                            }

//                            https://www.geeksforgeeks.org/how-to-sort-an-arraylist-of-objects-by-property-in-java/
                            Collections.sort(products);

                            if(products.size()>=3) {
                                Picasso.get()
                                        .load(products.get(0).getImage())
                                        .into(binding.imgNo1);
                                Picasso.get()
                                        .load(products.get(1).getImage())
                                        .into(binding.imgNo2);
                                Picasso.get()
                                        .load(products.get(2).getImage())
                                        .into(binding.imgNo3);
                            }
                            else if(products.size()==2){
                                Picasso.get()
                                        .load(products.get(0).getImage())
                                        .into(binding.imgNo1);
                                Picasso.get()
                                        .load(products.get(1).getImage())
                                        .into(binding.imgNo2);
                            }
                            else if(products.size()==1){
                                Picasso.get()
                                        .load(products.get(0).getImage())
                                        .into(binding.imgNo1);
                            }

                            binding.imgNo1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ViewProductActivity.class);
                                    intent.putExtra("SKU", products.get(0).getSKU());
                                    startActivity(intent);
                                }
                            });
                            binding.imgNo2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ViewProductActivity.class);
                                    intent.putExtra("SKU", products.get(1).getSKU());
                                    startActivity(intent);
                                }
                            });
                            binding.imgNo3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ViewProductActivity.class);
                                    intent.putExtra("SKU", products.get(2).getSKU());
                                    startActivity(intent);
                                }
                            });
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