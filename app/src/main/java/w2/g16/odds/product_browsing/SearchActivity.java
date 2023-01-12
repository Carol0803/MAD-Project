package w2.g16.odds.product_browsing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Vector;

import w2.g16.odds.model.Category;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Shop;
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
                                double product_rating = Double.parseDouble(document.get("product_rating").toString());
                                String product_status = document.get("product_status").toString();
                                String image = document.get("image").toString();
                                double price = Double.parseDouble(document.get("price").toString());
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

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                currentSearchText = newText;

                if(selectedFilter == "product") {

                    Vector<Products> filteredProducts = new Vector<Products>();

                    for (Products product : productList) {
                        if (product.getProduct_name().toLowerCase().contains(newText.toLowerCase()))
                            filteredProducts.add(product);
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Could not find this product.", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 1000);
                        }
                    }
//                    SearchPorductAdapter adapter = new SearchPorductAdapter(getApplicationContext(), 0, filteredProducts);
//                    binding.listSearch.setAdapter(adapter);
//                    Utility.setListViewHeightBasedOnChildren(binding.listSearch);

                    binding.listSearch.setVisibility(View.GONE);
                    binding.recSearch.setVisibility(View.VISIBLE);
                    ProductAdapter adapter = new ProductAdapter(SearchActivity.this, getLayoutInflater(), filteredProducts);
                    binding.recSearch.setAdapter(adapter);
                    binding.recSearch.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));
                }
                else if(selectedFilter == "shop") {
                    ArrayList<Shop> filteredShop = new ArrayList<Shop>();

                    for (Shop shop : shopList) {
                        if (shop.getShopname().toLowerCase().contains(newText.toLowerCase()))
                            filteredShop.add(shop);
                        else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Could not find this shop.", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 1000);
                        }
                    }
                    SearchShopAdapter adapter = new SearchShopAdapter(getApplicationContext(), 0, filteredShop);
                    binding.listSearch.setAdapter(adapter);
                    Utility.setListViewHeightBasedOnChildren(binding.listSearch);
                }
                else if(selectedFilter == "category") {
                    ArrayList<Category> filteredCategory = new ArrayList<Category>();

                    for (Category category : catList) {
                        if (category.getCategoryName().toLowerCase().contains(newText.toLowerCase()))
                            filteredCategory.add(category);
                        else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Could not find this category.", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 1000);
                        }
                    }
                    SearchCategoryAdapter adapter = new SearchCategoryAdapter(getApplicationContext(), 0, filteredCategory);
                    binding.listSearch.setAdapter(adapter);
                    Utility.setListViewHeightBasedOnChildren(binding.listSearch);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText;

                if(selectedFilter == "product") {

                    Vector<Products> filteredProducts = new Vector<Products>();

                    for (Products product : productList) {
                        if (product.getProduct_name().toLowerCase().contains(newText.toLowerCase()))
                                filteredProducts.add(product);
                    }
                    binding.listSearch.setVisibility(View.GONE);
                    binding.recSearch.setVisibility(View.VISIBLE);
                    ProductAdapter adapter = new ProductAdapter(SearchActivity.this, getLayoutInflater(), filteredProducts);
                    binding.recSearch.setAdapter(adapter);
                    binding.recSearch.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));

                }
                else if(selectedFilter == "shop") {
                    ArrayList<Shop> filteredShop = new ArrayList<Shop>();

                    for (Shop shop : shopList) {
                        if (shop.getShopname().toLowerCase().contains(newText.toLowerCase()))
                            filteredShop.add(shop);
                    }
                    binding.listSearch.setVisibility(View.VISIBLE);
                    binding.recSearch.setVisibility(View.GONE);
                    SearchShopAdapter adapter = new SearchShopAdapter(getApplicationContext(), 0, filteredShop);
                    binding.listSearch.setAdapter(adapter);
                    Utility.setListViewHeightBasedOnChildren(binding.listSearch);
                }
                else if(selectedFilter == "category") {
                    ArrayList<Category> filteredCategory = new ArrayList<Category>();

                    for (Category category : catList) {
                        if (category.getCategoryName().toLowerCase().contains(newText.toLowerCase()))
                            filteredCategory.add(category);
                    }
                    binding.listSearch.setVisibility(View.VISIBLE);
                    binding.recSearch.setVisibility(View.GONE);
                    SearchCategoryAdapter adapter = new SearchCategoryAdapter(getApplicationContext(), 0, filteredCategory);
                    binding.listSearch.setAdapter(adapter);
                    Utility.setListViewHeightBasedOnChildren(binding.listSearch);
                }
                return false;
            }
        });

        binding.listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedFilter == "shop") {
                    Shop selectShop = (Shop) (binding.listSearch.getItemAtPosition(position));
                    Intent showDetail = new Intent(getApplicationContext(), ViewShopActivity.class);
                    showDetail.putExtra("shopID", selectShop.getShopID());
                    startActivity(showDetail);
                }
                else if(selectedFilter == "category") {
                    Category selectCat = (Category) (binding.listSearch.getItemAtPosition(position));
                    Intent showDetail = new Intent(getApplicationContext(), ViewCategoryActivity.class);
                    showDetail.putExtra("category_ID", selectCat.getCategoryID());
                    startActivity(showDetail);
                }
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
                    binding.listSearch.setAdapter(null);
                    binding.recSearch.setAdapter(null);
                    binding.rbByProduct.setBackgroundResource(R.drawable.rounded_rectangle_bg_checked);
                    binding.rbByProduct.setTextColor(Color.WHITE);
                    binding.rbByCategory.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByCategory.setTextColor(Color.BLACK);
                    productList = new ArrayList<>();
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
                                            double product_rating = Double.parseDouble(document.get("product_rating").toString());
                                            String product_status = document.get("product_status").toString();
                                            String image = document.get("image").toString();
                                            double price = Double.parseDouble(document.get("price").toString());
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
                    binding.listSearch.setAdapter(null);
                    binding.recSearch.setVisibility(View.GONE);
                    binding.rbByShop.setBackgroundResource(R.drawable.rounded_rectangle_bg_checked);
                    binding.rbByShop.setTextColor(Color.WHITE);
                    binding.rbByProduct.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByProduct.setTextColor(Color.BLACK);
                    binding.rbByCategory.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByCategory.setTextColor(Color.BLACK);
                    binding.rbByShop.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByShop.setTextColor(Color.BLACK);
                    shopList = new ArrayList<>();
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
                    binding.listSearch.setAdapter(null);
                    binding.recSearch.setVisibility(View.GONE);
                    binding.rbByCategory.setBackgroundResource(R.drawable.rounded_rectangle_bg_checked);
                    binding.rbByCategory.setTextColor(Color.WHITE);
                    binding.rbByProduct.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByProduct.setTextColor(Color.BLACK);
                    binding.rbByShop.setBackgroundResource(R.drawable.rounded_rectangle_bg);
                    binding.rbByShop.setTextColor(Color.BLACK);
                    catList = new ArrayList<>();
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
                                        SearchCategoryAdapter adapter = new SearchCategoryAdapter(getApplicationContext(), 0, catList);
                                        binding.listSearch.setAdapter(adapter);
                                        Utility.setListViewHeightBasedOnChildren(binding.listSearch);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    break;
                }
        }
    }
}