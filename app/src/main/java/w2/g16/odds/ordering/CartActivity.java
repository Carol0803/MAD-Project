package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Vector;

import w2.g16.odds.MainActivity;
import w2.g16.odds.model.Product;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;

    private Product product;
    private Vector<Product> products;
    private CartAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //set toolbar as actionbar
        //back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        final String TAG = "Read Data Activity";
        db.collection("customer/username/cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String name = document.get("product_name").toString();
                                String price = document.get("product_price").toString();
                                String variation = document.get("product_variation").toString();
                                String quantity = document.get("quantity").toString();
                                String img = "https://firebasestorage.googleapis.com/v0/b/odds-38a12.appspot.com/o/product%2FFOE%2FFOE-0001.jpeg?alt=media&token=19785812-88ae-4ab9-aad3-7d1542f12e3a";

                                products.add(new Product(name, price, variation, quantity, img, null));
                                adapter.notifyItemInserted(products.size());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        products = new Vector<>();
        adapter = new CartAdapter(getLayoutInflater(), products);

        binding.recCart.setAdapter(adapter);
        binding.recCart.setLayoutManager(new LinearLayoutManager(this));
    }

    public void fnCheckout(View view) {
        startActivity(new Intent(this, CheckoutActivity.class));
    }
}