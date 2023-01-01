package w2.g16.odds.ordering;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Cart;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Shop;
import w2.g16.odds.model.Variation;

public class CartChildAdapter extends RecyclerView.Adapter<CartChildAdapter.CartChildViewHolder> {

    /*private final Vector<Cart> carts;
    private ArrayList<Integer> selectCheck;
    private int selectedPosition = 0;
    private int current_quantity;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "Read Data Activity";

    public CartChildAdapter(Vector<Cart> carts) {
        this.carts = carts;
    }

    @NonNull
    @Override
    public CartChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_child, parent,false);
        return new CartChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartChildViewHolder holder, int position) {
        Cart cart = carts.get(position);

        holder.tvName.setText(cart.getProduct_name());
        holder.tvPrice.setText("RM " + cart.getPrice());
        holder.quantity.setText(cart.getQuantity());
        Picasso.get()
                .load(cart.getImage())
                .into(holder.img);

        current_quantity = Integer.parseInt(cart.getQuantity());

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_quantity += 1;
                holder.quantity.setText("" + current_quantity);

                DocumentReference addQuantity = db.collection("customer").document("username")
                        .collection("cart").document(cart.getShopID())
                        .collection("cart_product").document(cart.getSKU());
                addQuantity.update("quantity", ""+current_quantity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_quantity -= 1;
                holder.quantity.setText("" + current_quantity);

                DocumentReference addQuantity = db.collection("customer").document("username")
                        .collection("cart").document(cart.getShopID())
                        .collection("cart_product").document(cart.getSKU());
                addQuantity.update("quantity", ""+current_quantity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class CartChildViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvName, tvPrice, quantity;
        private final ImageView img;
        private final CheckBox checkBox;
        private final ImageButton btnAdd, btnMinus;

        public CartChildViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_product_name);
            this.tvPrice = itemView.findViewById(R.id.tv_price);
            this.quantity = itemView.findViewById(R.id.txt_quantity);
            this.img = itemView.findViewById(R.id.img_product);
            this.checkBox = itemView.findViewById(R.id.checkBox_select);
            this.btnAdd = itemView.findViewById(R.id.btn_add);
            this.btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }*/

//    private final Vector<Cart> carts;
    private List<Cart> carts;
    private ArrayList<Integer> selectCheck;
    private int selectedPosition = 0;
    private int current_quantity;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "Read Data Activity";

    public void setChildItemList(List<Cart> carts){
        this.carts = carts;

//        this.carts.removeAll(Collections.singleton(null));
    }

//    public CartChildAdapter(Vector<Cart> carts) {
//        this.carts = carts;
//    }

    @NonNull
    @Override
    public CartChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_child, parent,false);
        return new CartChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartChildViewHolder holder, int position) {
        Cart cart = carts.get(position);

        holder.tvName.setText(cart.getProduct_name());
        holder.tvPrice.setText("RM " + cart.getPrice());
        holder.quantity.setText(cart.getQuantity());
        Picasso.get()
                .load(cart.getImage())
                .into(holder.img);

        current_quantity = Integer.parseInt(cart.getQuantity());

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_quantity += 1;
                holder.quantity.setText("" + current_quantity);

                DocumentReference addQuantity = db.collection("customer").document("username")
                        .collection("cart").document(cart.getShopID())
                        .collection("cart_product").document(cart.getSKU());
                addQuantity.update("quantity", ""+current_quantity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_quantity -= 1;
                holder.quantity.setText("" + current_quantity);

                DocumentReference addQuantity = db.collection("customer").document("username")
                        .collection("cart").document(cart.getShopID())
                        .collection("cart_product").document(cart.getSKU());
                addQuantity.update("quantity", ""+current_quantity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class CartChildViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvName, tvPrice, quantity;
        private final ImageView img;
        private final CheckBox checkBox;
        private final ImageButton btnAdd, btnMinus;

        public CartChildViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_product_name);
            this.tvPrice = itemView.findViewById(R.id.tv_price);
            this.quantity = itemView.findViewById(R.id.txt_quantity);
            this.img = itemView.findViewById(R.id.img_product);
            this.checkBox = itemView.findViewById(R.id.checkBox_select);
            this.btnAdd = itemView.findViewById(R.id.btn_add);
            this.btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }
}
