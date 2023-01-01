package w2.g16.odds.product_browsing;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityViewProductBinding;
import w2.g16.odds.model.Cart;

public class ViewProductActivity extends AppCompatActivity {

    private ActivityViewProductBinding binding;
    private String SKU;
    private String shopID, shopname, name, price, rating, description, sold_item, image, currentStock="0";
    private int currentQuantity = 1;

    private static final DecimalFormat df = new DecimalFormat("0.0");

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "Read Data Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        SKU = intent.getStringExtra("SKU");

        DocumentReference docRef = db.collection("products").document(SKU);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        shopID = document.get("owned by").toString();
                        name = document.get("product_name").toString();
                        price = document.get("price").toString();
                        description = document.get("product_description").toString();
                        rating = document.get("product_rating").toString();
                        sold_item = document.get("sold_item").toString();
                        image = document.get("image").toString();
                        currentStock = document.get("stock").toString();

                        binding.tvProductName.setText(name);
                        binding.tvProductPrice.setText("RM " + price);
                        binding.tvProductDescription.setText(description);
                        binding.ratingBar.setRating(Float.parseFloat(rating));
                        binding.tvRating.setText(""+df.format(Double.parseDouble(rating)));
                        binding.tvItemSold.setText(sold_item + " sold");
                        Picasso.get()
                                .load(image)
                                .into(binding.imgProduct);

                        if(currentStock.equals("0")){
                            binding.txtQuantity.setText("0");
                            binding.btnMinus.setEnabled(false);
                            binding.btnAdd.setEnabled(false);
                            binding.btnAddToCart.setEnabled(false);
                            binding.btnAddToCart.setImageResource(R.drawable.ic_add_to_cart_disable);

                            Toast.makeText(getApplicationContext(),"This item is currently out of stock.",Toast.LENGTH_LONG).show();
                        }
                        else {

                            binding.btnMinus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentQuantity = Integer.parseInt(binding.txtQuantity.getText().toString());
                                    if (currentQuantity != 1) {
                                        currentQuantity -= 1;
                                        binding.txtQuantity.setText("" + currentQuantity);
                                    }
                                }
                            });

                            binding.btnAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentQuantity = Integer.parseInt(binding.txtQuantity.getText().toString());
                                    if (currentQuantity < Integer.parseInt(currentStock)) {
                                        currentQuantity += 1;
                                        binding.txtQuantity.setText("" + currentQuantity);
                                    }
                                }
                            });

                            binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DocumentReference docRef3 = db.collection("customer").document("username")
                                            .collection("cart").document(shopID);
                                    docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                    addProductToCart();

                                                } else {
                                                    Log.d(TAG, "No such document");

                                                    Map<String, Object> shop = new HashMap<>();
                                                    shop.put("shop_name", shopname);

                                                    db.collection("customer").document("username")
                                                            .collection("cart").document(shopID)
                                                            .set(shop)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                                                    addProductToCart();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error writing document", e);
                                                                }
                                                            });
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                            });
                        }

                        DocumentReference docRef2 = db.collection("shop").document(shopID);
                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                        shopname = document.get("shop_name").toString();
                                        binding.tvShopName.setText(shopname);

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

        binding.lytShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewShopActivity.class);
                intent.putExtra("shopID", shopID);
                startActivity(intent);
            }
        });
    }

    public void addProductToCart(){

        DocumentReference docRef = db.collection("customer").document("username")
                .collection("cart").document(shopID)
                .collection("cart_product").document(SKU);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        Toast toast=Toast.makeText(getApplicationContext(),"This item is already in cart.",Toast.LENGTH_SHORT);
                        toast.show();

                    } else {
                        Log.d(TAG, "No such document");

                        Map<String, Object> data = new HashMap<>();
                        data.put("deliver_by", shopID);
                        data.put("product_image", image);
                        data.put("product_name", name);
                        data.put("product_price", price);
                        data.put("quantity", currentQuantity);

                        db.collection("customer").document("username")
                                .collection("cart").document(shopID)
                                .collection("cart_product").document(SKU)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");

                                        Toast toast=Toast.makeText(getApplicationContext(),"Added to cart.",Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}