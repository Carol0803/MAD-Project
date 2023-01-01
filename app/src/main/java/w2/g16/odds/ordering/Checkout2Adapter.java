package w2.g16.odds.ordering;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Order;

public class Checkout2Adapter extends RecyclerView.Adapter<Checkout2ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final ArrayList<Order> orders;

    public Checkout2Adapter(LayoutInflater layoutInflater, ArrayList<Order> orders) {
        this.layoutInflater = layoutInflater;
        this.orders = orders;
    }

    @NonNull
    @Override
    public Checkout2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Checkout2ViewHolder(layoutInflater.inflate(R.layout.item_order_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Checkout2ViewHolder holder, int position) {
        holder.setOrderProduct(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}
