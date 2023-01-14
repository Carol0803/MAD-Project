package w2.g16.odds.recommendation;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import w2.g16.odds.R;
import w2.g16.odds.model.Shop;

public class MyAdapterHPopular extends RecyclerView.Adapter<MyAdapterHPopular.MyViewHolder> {

    Context context;
    ArrayList <Shop> shopArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int quantity = 0;

    public MyAdapterHPopular(Context context, ArrayList<Shop> shopArrayList) {
        this.context = context;
        this.shopArrayList = shopArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemhorizontal,parent,false);

        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Shop shop = shopArrayList.get(position);

        holder.shop_name.setText(shop.getShopname());
        holder.operation_time.setText(shop.getShop_open() + " - " + shop.getShop_close());
        holder.sales.setText(shop.getRating());
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
