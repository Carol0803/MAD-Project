package w2.g16.odds.browsing;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Vector<Category> categories;
    private final Activity activity;
    private int selectedPosition = -1;

    public CategoryAdapter(Activity activity, LayoutInflater layoutInflater, Vector<Category> categories) {
        this.activity = activity;
        this.layoutInflater = layoutInflater;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(layoutInflater.inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        Picasso.get()
                .load(category.getImg())
                .into(holder.imgCategory);
        holder.tvCategory.setText(category.getCategoryName());

        holder.lytCategory.setOnClickListener(view -> {
                    selectedPosition = holder.getAdapterPosition();
                    if(selectedPosition==position){
                        String categoryID = category.getCategoryID();

                        Intent intent = new Intent("category_ID");
                        intent.putExtra("category_ID", categoryID);
                        LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgCategory;
        private final TextView tvCategory;
        private final CardView lytCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgCategory = itemView.findViewById(R.id.img_category);
            this.tvCategory = itemView.findViewById(R.id.tv_category);
            this.lytCategory = itemView.findViewById(R.id.lytCategory);
        }
    }
}
