package w2.g16.odds.recommendation;

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

public class MyAdapterHRating extends RecyclerView.Adapter<MyAdapterHRating.MyViewHolder> {

    Context context;
    ArrayList<Shop> shopArrayList2;


    public MyAdapterHRating(Context context, ArrayList<Shop> shopArrayList2) {
        this.context = context;
        this.shopArrayList2 = shopArrayList2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemrating,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Shop shop = shopArrayList2.get(position);

        holder.tv_rating_shopname.setText(shop.getShopname());
        holder.tv_rating_value.setText(shop.getRating());
        holder.operation_time.setText(shop.getShop_open() + " - " + shop.getShop_close());
    }

    @Override
    public int getItemCount() {
        return shopArrayList2.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_rating_shopname, operation_time, tv_rating_value;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_rating_shopname = itemView.findViewById(R.id.tv_rating_shopname);
            tv_rating_value = itemView.findViewById(R.id.tv_rating_value);
            operation_time = itemView.findViewById(R.id.tvoperationtime);
        }
    }

}
