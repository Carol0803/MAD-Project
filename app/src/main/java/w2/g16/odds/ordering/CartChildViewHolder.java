package w2.g16.odds.ordering;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Enumeration;
import java.util.ListIterator;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Cart;
import w2.g16.odds.model.Products;
import w2.g16.odds.model.Variation;
/*
public class CartChildViewHolder extends RecyclerView.ViewHolder {

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

    public void setProduct(Cart cart) {
        Vector<Products> product = cart.getProducts();
        for(int i=0; i<product.size(); i++) {
            tvName.setText(product.get(i).getProduct_name());
            Variation variation = product.get(i).getVariation().get(0);
            tvPrice.setText(variation.getPrice());
            tvVariation.setText(variation.getVariation_type());
            quantity.setText(variation.getQuantity());
            Picasso.get()
                    .load(variation.getImage())
                    .into(img);
        }
    }
}*/

/*public class CartChildViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvName, tvVariation, tvPrice, quantity;
    private final ImageView img;

    public CartChildViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvName = itemView.findViewById(R.id.tv_product_name);
        this.tvPrice = itemView.findViewById(R.id.tv_price);
        this.tvVariation = itemView.findViewById(R.id.tv_variation);
        this.quantity = itemView.findViewById(R.id.txt_quantity);
        this.img = itemView.findViewById(R.id.img_product);
    }

    public void setProduct(Cart cart) {
        tvName.setText(cart.getProduct_name());
        tvPrice.setText(cart.getPrice());
        tvVariation.setText(cart.getVariation_type());
        quantity.setText(cart.getQuantity());
        Picasso.get()
                .load(cart.getImage())
                .into(img);
    }

}*/


