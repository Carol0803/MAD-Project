package w2.g16.odds.ordering;

import static java.lang.Double.parseDouble;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Order;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.PurchaseHistoryViewHolder> {

    private Activity activity;
    private Context context;
    private final LayoutInflater layoutInflater;
    private final Vector<Order> orders;
    private int selectedPosition = -1;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public PurchaseHistoryAdapter(Activity activity, Context context, LayoutInflater layoutInflater, Vector<Order> orders) {
        this.activity = activity;
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.orders = orders;
    }

    @NonNull
    @Override
    public PurchaseHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchaseHistoryViewHolder(layoutInflater.inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseHistoryViewHolder holder, int position) {
        //        holder.setOrder(orders.get(position));

        Order order = orders.get(position);
        holder.tvShopName.setText(order.getShopname());
        holder.tvName.setText(order.getProduct_name());
        holder.tvPrice.setText("RM " + df.format(parseDouble(order.getSubtotal())));
        holder.tvQuantity.setText("Quantity: " + order.getQuantity());
        holder.tvTotal.setText("Total:   RM " + df.format(parseDouble(order.getAmount())));
        Picasso.get()
                .load(order.getProduct_img())
                .into(holder.imgProduct);

        if(order.getOrder_status().equals("UNPAID"))
            holder.imgStatus.setImageResource(R.drawable.ic_order_status_unpaid);
        if(order.getOrder_status().equals("TO-PACK"))
            holder.imgStatus.setImageResource(R.drawable.ic_order_status_pack);
        if(order.getOrder_status().equals("TO-DELIVER"))
            holder.imgStatus.setImageResource(R.drawable.ic_order_status_deliver);
        if(order.getOrder_status().equals("COMPLETED"))
            holder.imgStatus.setImageResource(R.drawable.ic_order_status_completed);
        if(order.getOrder_status().equals("CANCELLED"))
            holder.imgStatus.setImageResource(R.drawable.ic_order_status_cancel);

        holder.tvMore.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();

            if(selectedPosition==position){
                String orderID = order.getOrderID();

                Intent intent = new Intent(context, ViewPurchaseHistoryActivity.class);
                intent.putExtra("orderID", orderID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.btnView.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();

            if(selectedPosition==position){
                String orderID = order.getOrderID();

                Intent intent = new Intent(context, ViewPurchaseHistoryActivity.class);
                intent.putExtra("orderID", orderID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(orders == null)
            return 0;
        else
            return orders.size();
    }

    public class PurchaseHistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvShopName, tvName, tvPrice, tvQuantity, tvTotal, tvMore;
        private final ImageView imgProduct, imgStatus;
        private final Button btnView;

        public PurchaseHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvShopName = itemView.findViewById(R.id.tv_shopname);
            this.tvName = itemView.findViewById(R.id.tv_product_name);
            this.tvPrice = itemView.findViewById(R.id.tv_price);
            this.tvQuantity = itemView.findViewById(R.id.tv_quantity);
            this.tvTotal = itemView.findViewById(R.id.tv_total);
            this.imgProduct = itemView.findViewById(R.id.img_product);
            this.imgStatus = itemView.findViewById(R.id.imgStatus);
            this.tvMore = itemView.findViewById(R.id.tvMore);
            this.btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
