package w2.g16.odds.ordering;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import w2.g16.odds.model.Cart;
import w2.g16.odds.R;
import w2.g16.odds.model.Order;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Shop;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>  {

    /*private Activity activity;
    private final Vector<Shop> shops;
    private final Vector<Cart> carts;
    private String selected_shopname;
    private ArrayList<Order> orders;
    private int selectedPosition = -1;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CartAdapter(Activity activity, Vector<Shop> shops, Vector<Cart> carts) {
        this.activity = activity;
        this.shops = shops;
        this.carts = carts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Shop shop = shops.get(position);
        holder.tvShopname.setText(shop.getShopname());

        CartChildAdapter cartChildAdapter = new CartChildAdapter(carts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        holder.recProduct.setLayoutManager(linearLayoutManager);
        holder.recProduct.setAdapter(cartChildAdapter);

        holder.checkBox.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectedPosition==position){
            holder.checkBox.setChecked(true);

            selected_shopname = shop.getShopname();
            db.collection("customer/username/cart/" + shop.getShopID() + "/cart_product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Double total = 0.00;
                                Double subtotal = 0.00;
                                orders = new ArrayList<Order>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    String name = document.get("product_name").toString();
                                    String price = document.get("product_price").toString();
                                    String quantity = document.get("quantity").toString();
                                    String image = document.get("product_image").toString();

                                    subtotal = Double.parseDouble(price) * Double.parseDouble(quantity);
                                    total += subtotal;

                                    orders.add(new Order(name, price, quantity, image));
                                }
                                Intent intent = new Intent("return_total");
                                intent.putExtra("total",""+df.format(total));
                                intent.putExtra("shopname", selected_shopname);
                                intent.putExtra("order", orders);
                                LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvShopname;
        RecyclerView recProduct;
        private CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopname = itemView.findViewById(R.id.tv_shopname);
            recProduct = itemView.findViewById(R.id.recProduct);
            checkBox = itemView.findViewById(R.id.checkBox_select);
        }
    }*/

    private Activity activity;
//    private final List<Shop> shops;
//    private final Vector<Cart> carts;
    private String selected_shopname;
    private ArrayList<Order> orders;
    private int selectedPosition = -1;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<Shop> shops;

    public void setParentItemList(List<Shop> shops){
        this.shops = shops;
    }

    public CartAdapter(){}

    public CartAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Shop shop = shops.get(position);
        holder.tvShopname.setText(shop.getShopname());

//        holder.recProduct.setHasFixedSize(false);
        holder.recProduct.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        CartChildAdapter cartChildAdapter = new CartChildAdapter();

        cartChildAdapter.setChildItemList(shop.getCartList());
        holder.recProduct.setAdapter(cartChildAdapter);
        cartChildAdapter.notifyDataSetChanged();

        holder.checkBox.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectedPosition==position){
            holder.checkBox.setChecked(true);

            selected_shopname = shop.getShopname();
            db.collection("customer/username/cart/" + shop.getShopID() + "/cart_product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Double total = 0.00;
                                Double subtotal = 0.00;
                                orders = new ArrayList<Order>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    String name = document.get("product_name").toString();
                                    String price = document.get("product_price").toString();
                                    String quantity = document.get("quantity").toString();
                                    String image = document.get("product_image").toString();

                                    subtotal = Double.parseDouble(price) * Double.parseDouble(quantity);
                                    total += subtotal;

                                    orders.add(new Order(name, price, quantity, image));
                                }
                                Intent intent = new Intent("return_total");
                                intent.putExtra("total",""+df.format(total));
                                intent.putExtra("shopname", selected_shopname);
                                intent.putExtra("order", orders);
                                LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (shops != null){
            return shops.size();
        }else{
            return 0;
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvShopname;
        private RecyclerView recProduct;
        private CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopname = itemView.findViewById(R.id.tv_shopname);
            recProduct = itemView.findViewById(R.id.recProduct);
            checkBox = itemView.findViewById(R.id.checkBox_select);
        }
    }

}
