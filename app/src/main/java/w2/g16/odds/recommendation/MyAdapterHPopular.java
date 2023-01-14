package w2.g16.odds.recommendation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import w2.g16.odds.R;
import w2.g16.odds.model.Shop;

public class MyAdapterHPopular extends RecyclerView.Adapter<MyAdapterHPopular.MyViewHolder> {

    Context context;
    ArrayList <Shop> shopArrayList;

    public MyAdapterHPopular(Context context, ArrayList<Shop> shopArrayList) {
        this.context = context;
        this.shopArrayList = shopArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itempopular,parent,false);

        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Shop shop = shopArrayList.get(position);

        holder.shop_name.setText(shop.getShop_name());
        holder.operation_time.setText(shop.getShop_open() + " - " + shop.getShop_close());
        holder.sales.setText(""+shop.getShop_rating());
    }

    @Override
    public int getItemCount() {
        return shopArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shop_name, operation_time, sales;

        public MyViewHolder(View itemView) {
            super(itemView);

            shop_name = itemView.findViewById(R.id.tvshopname);
            operation_time = itemView.findViewById(R.id.tvoperationtime);
            sales = itemView.findViewById(R.id.tvsales);
        }
    }

}
