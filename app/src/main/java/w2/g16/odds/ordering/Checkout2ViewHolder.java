package w2.g16.odds.ordering;

import static java.lang.Double.parseDouble;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import w2.g16.odds.R;
import w2.g16.odds.model.Order;

public class Checkout2ViewHolder extends RecyclerView.ViewHolder{

    private final TextView tvName, tvPrice, tvQuantity;
    private final ImageView imgProduct;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Checkout2ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvName = itemView.findViewById(R.id.tv_product_name);
        this.tvPrice = itemView.findViewById(R.id.tv_price);
        this.tvQuantity = itemView.findViewById(R.id.tv_quantity);
        this.imgProduct = itemView.findViewById(R.id.img_product);
    }

    public void setOrderProduct(Order order) {
        tvName.setText(order.getProduct_name());
        tvPrice.setText("RM " + df.format(parseDouble(order.getProduct_price())));
        tvQuantity.setText("Quantity: " + order.getQuantity());
        Picasso.get()
                .load(order.getProduct_img())
                .into(imgProduct);
    }
}
