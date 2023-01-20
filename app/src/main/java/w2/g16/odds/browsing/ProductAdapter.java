package w2.g16.odds.browsing;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Products;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Vector<Products> products;
    private final Activity activity;
    private int selectedPosition = -1;

    public ProductAdapter(Activity activity, LayoutInflater layoutInflater, Vector<Products> products) {
        this.activity = activity;
        this.layoutInflater = layoutInflater;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//        holder.setProduct(products.get(position));

        Products product = products.get(position);
        Picasso.get()
                .load(product.getImage())
                .into(holder.imgProduct);
        holder.tvName.setText(product.getProduct_name());
        holder.tvPrice.setText("RM " + product.getPrice());

        holder.lytProduct.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
//            notifyDataSetChanged();

            if(selectedPosition==position){
                String SKU = product.getSKU();

                Intent intent = new Intent("product_SKU");
                intent.putExtra("SKU", SKU);
                LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
            }
        });

        holder.btnCart.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
//            notifyDataSetChanged();

            if(selectedPosition==position){
                String SKU = product.getSKU();

                Intent intent = new Intent("product_SKU");
                intent.putExtra("SKU", SKU);
                LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(products == null)
            return 0;
        else
            return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgProduct;
        private final TextView tvName, tvPrice;
        public final RelativeLayout lytProduct;
        public final ImageButton btnCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgProduct = itemView.findViewById(R.id.img_item);
            this.tvName = itemView.findViewById(R.id.tv_productname);
            this.tvPrice = itemView.findViewById(R.id.tv_productprice);
            this.lytProduct = itemView.findViewById(R.id.lytProduct);
            this.btnCart = itemView.findViewById(R.id.btnCart);
        }
    }
}
