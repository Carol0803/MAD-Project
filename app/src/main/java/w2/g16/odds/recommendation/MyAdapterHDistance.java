package w2.g16.odds.recommendation;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import w2.g16.odds.R;
import w2.g16.odds.browsing.ViewShopActivity;
import w2.g16.odds.model.Shop;

public class MyAdapterHDistance extends RecyclerView.Adapter<MyAdapterHDistance.MyViewHolder> {

    Context context;
    ArrayList <Shop> shopArrayList;
    private int selectedPosition = -1;

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

        holder.shop_name.setText(shop.getShop_name());
        holder.operation_time.setText(shop.getShop_open() + " - " + shop.getShop_close());
        holder.distance.setText(""+shop.getShop_rating()+" km");

        holder.nearby.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();

            if(selectedPosition==position){
                String shopID = shop.getShopID();

                Intent intent = new Intent(context, ViewShopActivity.class);
                intent.putExtra("shopID", shopID);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(shopArrayList == null)
            return 0;
        else
            return shopArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shop_name, operation_time, distance;
        CardView nearby;

        public MyViewHolder(View itemView) {
            super(itemView);

            shop_name = itemView.findViewById(R.id.tvshopname);
            operation_time = itemView.findViewById(R.id.tvoperationtime);
            distance = itemView.findViewById(R.id.tvdistance);
            nearby = itemView.findViewById(R.id.nearby);
        }
    }

}
