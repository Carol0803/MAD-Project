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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Cart;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Shop;
import w2.g16.odds.model.Variation;

/*
public class CartChildAdapter extends RecyclerView.Adapter<CartChildAdapter.CartChildViewHolder> {

    private final Vector<Products> products;

    public CartChildAdapter(Vector<Products> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public CartChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_child, parent, false);
        return new CartChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartChildViewHolder holder, int position) {
        Products product = products.get(position);

        holder.tvName.setText(product.getProduct_name());
        Variation variation = product.getVariation().get(0);
        holder.tvPrice.setText(variation.getPrice());
        holder.tvVariation.setText(variation.getVariation_type());
        holder.quantity.setText(variation.getQuantity());
        Picasso.get()
                .load(variation.getImage())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class CartChildViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName, tvVariation, tvPrice;
        private final EditText quantity;
        private final ImageView img;

        public CartChildViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_product_name);
            this.tvPrice = itemView.findViewById(R.id.tv_price);
            this.tvVariation = itemView.findViewById(R.id.tv_variation);
            this.quantity = itemView.findViewById(R.id.txt_quantity);
            this.img = itemView.findViewById(R.id.img_product);
        }
    }
}
*/

public class CartChildAdapter extends RecyclerView.Adapter<CartChildAdapter.CartChildViewHolder> {

    private final Vector<Cart> carts;
    private ArrayList<Integer> selectCheck;
    private int selectedPosition = 0;

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
        holder.tvVariation.setText("Variation: " + cart.getVariation_type());
        holder.quantity.setText(cart.getQuantity());
        Picasso.get()
                .load(cart.getImage())
                .into(holder.img);

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = Integer.parseInt(cart.getQuantity()) + 1;
                holder.quantity.setText("" + newQuantity);

                DocumentReference addQuantity = db.collection("customer").document("username")
                        .collection("cart").document(cart.getShopID())
                        .collection("cart_product").document(cart.getCartID());
                addQuantity.update("quantity", ""+newQuantity)
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
                int newQuantity = Integer.parseInt(cart.getQuantity()) - 1;
                holder.quantity.setText("" + newQuantity);

                DocumentReference addQuantity = db.collection("customer").document("username")
                        .collection("cart").document(cart.getShopID())
                        .collection("cart_product").document(cart.getCartID());
                addQuantity.update("quantity", ""+newQuantity)
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



        /*if (selectCheck.get(position) == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<selectCheck.size(); k++) {
                    if(k==position) {
                        selectCheck.set(k,1);
                    } else {
                        selectCheck.set(k,0);
                    }
                }
                notifyDataSetChanged();

            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true){
                    //Do whatever you want to do with selected value
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class CartChildViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvName, tvVariation, tvPrice, quantity;
        private final ImageView img;
        private final CheckBox checkBox;
        private final ImageButton btnAdd, btnMinus;

        public CartChildViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_product_name);
            this.tvPrice = itemView.findViewById(R.id.tv_price);
            this.tvVariation = itemView.findViewById(R.id.tv_variation);
            this.quantity = itemView.findViewById(R.id.txt_quantity);
            this.img = itemView.findViewById(R.id.img_product);
            this.checkBox = itemView.findViewById(R.id.checkBox_select);
            this.btnAdd = itemView.findViewById(R.id.btn_add);
            this.btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }
}
