package w2.g16.odds.ordering;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import w2.g16.odds.Product;
import w2.g16.odds.R;

public class CartViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvName, tvPrice, tvVariation, tvQuantity;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvName = itemView.findViewById(R.id.tv_product_name);
        this.tvPrice = itemView.findViewById(R.id.tv_price);
        this.tvVariation = itemView.findViewById(R.id.tv_variation);
        this.tvQuantity = itemView.findViewById(R.id.txt_quantity);
    }

    public void setProduct(Product product) {
        tvName.setText(product.getName());
        tvPrice.setText(product.getPrice());
        tvVariation.setText(product.getVariation());
        tvQuantity.setText(product.getQuantity());
    }
}
