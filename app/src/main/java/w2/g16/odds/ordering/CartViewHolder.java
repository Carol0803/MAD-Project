package w2.g16.odds.ordering;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import w2.g16.odds.model.Product;
import w2.g16.odds.R;

public class CartViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvName, tvPrice, tvVariation, tvQuantity;
    private final ImageView imgProduct;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvName = itemView.findViewById(R.id.tv_product_name);
        this.tvPrice = itemView.findViewById(R.id.tv_price);
        this.tvVariation = itemView.findViewById(R.id.tv_variation);
        this.tvQuantity = itemView.findViewById(R.id.txt_quantity);
        this.imgProduct = itemView.findViewById(R.id.img_prouct);

    }

    public void setProduct(Product product) {
        tvName.setText(product.getName());
        tvPrice.setText(product.getPrice());
        tvVariation.setText(product.getVariation());
        tvQuantity.setText(product.getQuantity());
        Picasso.get()
                .load(product.getImg())
                .into(imgProduct);
    }
}
