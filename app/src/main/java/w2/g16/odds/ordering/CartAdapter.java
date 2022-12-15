package w2.g16.odds.ordering;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import w2.g16.odds.model.Product;
import w2.g16.odds.R;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Vector<Product> products;

    public CartAdapter(LayoutInflater layoutInflater, Vector<Product> products) {
        this.layoutInflater = layoutInflater;
        this.products = products;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(layoutInflater.inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.setProduct(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
