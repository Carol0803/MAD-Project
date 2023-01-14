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

public class MyAdapterHDistance extends RecyclerView.Adapter<MyAdapterHDistance.MyViewHolder> {

    Context context;
    ArrayList <Shop> shopArrayList;


    public MyAdapterHDistance(Context context, ArrayList<Shop> shopArrayList) {
        this.context = context;
        this.shopArrayList = shopArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemdistance,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Shop shop = shopArrayList.get(position);

        holder.shop_name.setText(shop.getShopname());
        holder.operation_time.setText(shop.getShop_open() + " - " + shop.getShop_close());
        holder.distance.setText(shop.getRating());
    }

    @Override
    public int getItemCount() {
        return shopArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shop_name, operation_time, distance;

        public MyViewHolder(View itemView) {
            super(itemView);

            shop_name = itemView.findViewById(R.id.tvshopname);
            operation_time = itemView.findViewById(R.id.tvoperationtime);
            distance = itemView.findViewById(R.id.tvsales);
        }
    }

}
