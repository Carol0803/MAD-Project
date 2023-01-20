/*
package w2.g16.odds.ordering;

import static java.lang.Double.parseDouble;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import w2.g16.odds.model.Order;
import w2.g16.odds.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvShopName, tvName, tvPrice, tvQuantity, tvTotal;
    private final ImageView imgProduct;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        //this.orderID = itemView.findViewById(R.id.tvOrderID);
        this.tvShopName = itemView.findViewById(R.id.tv_shopname);
        this.tvName = itemView.findViewById(R.id.tv_product_name);
        this.tvPrice = itemView.findViewById(R.id.tv_price);
        this.tvQuantity = itemView.findViewById(R.id.tv_quantity);
        this.tvTotal = itemView.findViewById(R.id.tv_total);
        this.imgProduct = itemView.findViewById(R.id.img_product);
    }

    public void setOrder(Order order) {
        //orderID.setText(order.getOrderID());
        tvShopName.setText(order.getShopname());
        tvName.setText(order.getProduct_name());
        tvPrice.setText("RM " + df.format(parseDouble(order.getSubtotal())));
        tvQuantity.setText("Quantity: " + order.getQuantity());
        tvTotal.setText("Total:   RM " + df.format(parseDouble(order.getAmount())));
        Picasso.get()
                .load(order.getProduct_img())
                .into(imgProduct);
    }

}
*/
