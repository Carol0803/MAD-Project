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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import w2.g16.odds.R;
import w2.g16.odds.browsing.ViewShopActivity;
import w2.g16.odds.model.Shop;

public class MyAdapterHRating extends RecyclerView.Adapter<MyAdapterHRating.MyViewHolder> {

    Context context;
    ArrayList<Shop> shopArrayList2;
    private int selectedPosition = -1;

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

        holder.tv_rating_shopname.setText(shop.getShop_name());
        holder.tv_rating_value.setText(""+shop.getShop_rating());
        holder.operation_time.setText(shop.getShop_open() + " - " + shop.getShop_close());

        holder.rating.setOnClickListener(view -> {
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
        if(shopArrayList2 == null)
            return 0;
        else
            return shopArrayList2.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_rating_shopname, operation_time, tv_rating_value;
        CardView rating;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_rating_shopname = itemView.findViewById(R.id.tv_rating_shopname);
            tv_rating_value = itemView.findViewById(R.id.tv_rating_value);
            operation_time = itemView.findViewById(R.id.tvoperationtime);
            rating = itemView.findViewById(R.id.rating);
        }
    }

}
