package w2.g16.odds.product_browsing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import w2.g16.odds.MainActivity;
import w2.g16.odds.ProductAdapter;
import w2.g16.odds.model.Category;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Shop;
import w2.g16.odds.product_browsing.SearchActivity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    public static ArrayList<Products> productList = new ArrayList<Products>();
    public static ArrayList<Shop> shopList = new ArrayList<Shop>();
    public static ArrayList<Category> catList = new ArrayList<Category>();
    private String selectedFilter = "product";
    private String currentSearchText = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchView.requestFocus();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.rbByProduct.setChecked(true);
        binding.rbByProduct.setBackgroundResource(R.drawable.rounded_rectangle_bg_checked);
        binding.rbByProduct.setTextColor(Color.WHITE);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText;

                if(selectedFilter == "product") {
                    binding.listSearch.setAdapter(null);

                    ArrayList<Products> filteredProducts = new ArrayList<Products>();

                    for (Products product : productList) {
                        if (product.getProduct_name().toLowerCase().contains(newText.toLowerCase()))
                                filteredProducts.add(product);
                    }
                    SearchPorductAdapter adapter = new SearchPorductAdapter(getApplicationContext(), 0, filteredProducts);
                    binding.listSearch.setAdapter(adapter);

                }
                else if(selectedFilter == "shop") {
                    binding.listSearch.setAdapter(null);

                    ArrayList<Shop> filteredShop = new ArrayList<Shop>();

                    for (Shop shop : shopList) {
                        if (shop.getShopname().toLowerCase().contains(newText.toLowerCase()))
                            filteredShop.add(shop);
                    }
                    SearchShopAdapter adapter = new SearchShopAdapter(getApplicationContext(), 0, filteredShop);
                    binding.listSearch.setAdapter(adapter);
                }
                else if(selectedFilter == "category") {
                    binding.listSearch.setAdapter(null);

                    ArrayList<Category> filteredCategory = new ArrayList<Category>();

                    for (Category category : catList) {
                        if (category.getCategoryName().toLowerCase().contains(newText.toLowerCase()))
                            filteredCategory.add(category);
                    }
                    SearchCategoryAdapter adapter = new SearchCategoryAdapter(getApplicationContext(), 0, filteredCategory);
                    binding.listSearch.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbByProduct:
                if (checked)
                {
                    selectedFilter = "product";
                    binding.rbByProduct.setBackgroundResource(R.drawable.rounded_rectangle_bg_checked);
                    binding.rbByProduct.setTextColor(Color.WHITE);
                    binding.rbByCategory.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByCategory.setTextColor(Color.BLACK);
                    db.collection("products")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    final String TAG = "Read Data Activity";
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());

                                            String SKU = document.getId();
                                            String product_name = document.get("product_name").toString();
                                            String product_description = document.get("product_description").toString();
                                            String product_rating = document.get("product_rating").toString();
                                            String product_status = document.get("product_status").toString();
                                            String image = document.get("image").toString();
                                            String price = document.get("price").toString();
                                            String owned_by = document.get("owned by").toString();
                                            int sold_item = Integer.parseInt(document.get("sold_item").toString());
                                            String stock = document.get("stock").toString();
                                            String under_category = document.get("under_category").toString();

                                            productList.add(new Products(SKU, product_name, product_description, product_rating,
                                                    product_status, image, price, owned_by, sold_item, stock, under_category));
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    break;
                }
            case R.id.rbByShop:
                if (checked)
                {
                    selectedFilter = "shop";
                    binding.rbByShop.setBackgroundResource(R.drawable.rounded_rectangle_bg_checked);
                    binding.rbByShop.setTextColor(Color.WHITE);
                    binding.rbByProduct.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByProduct.setTextColor(Color.BLACK);
                    binding.rbByCategory.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByCategory.setTextColor(Color.BLACK);
                    binding.rbByShop.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByShop.setTextColor(Color.BLACK);
                    db.collection("shop")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    final String TAG = "Read Data Activity";
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());

                                            String shopID = document.getId();
                                            String shop_name = document.get("shop_name").toString();

                                            shopList.add(new Shop(shopID, shop_name));
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    break;
                }
            case R.id.rbByCategory:
                if (checked)
                {
                    selectedFilter = "category";
                    binding.rbByCategory.setBackgroundResource(R.drawable.rounded_rectangle_bg_checked);
                    binding.rbByCategory.setTextColor(Color.WHITE);
                    binding.rbByProduct.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByProduct.setTextColor(Color.BLACK);
                    binding.rbByShop.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByShop.setTextColor(Color.BLACK);
                    db.collection("category")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    final String TAG = "Read Data Activity";
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());

                                            String catID = document.getId();
                                            String category_name = document.get("category_name").toString();
                                            String icon_url = document.get("icon_url").toString();

                                            catList.add(new Category(catID, category_name, icon_url));
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    break;
                }
        }
    }

    /*private void filterList(String status)
    {
        selectedFilter = status;

        ArrayList<Products> filteredProducts = new ArrayList<Products>();

        for(Products product: productList)
        {
            if(product.getProduct_name().toLowerCase().contains(status))
            {
                if(currentSearchText == "")
                {
                    filteredProducts.add(product);
                }
                else
                {
                    if(product.getProduct_name().toLowerCase().contains(currentSearchText.toLowerCase()))
                    {
                        filteredProducts.add(product);
                    }
                }
            }
        }

//        ShapeAdapter adapter = new ShapeAdapter(getApplicationContext(), 0, filteredShapes);
//        listView.setAdapter(adapter);
    }*/
}