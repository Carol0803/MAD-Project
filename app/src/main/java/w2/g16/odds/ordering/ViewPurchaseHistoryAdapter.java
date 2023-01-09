package w2.g16.odds.ordering;

import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import w2.g16.odds.R;
import w2.g16.odds.model.Order;

public class ViewPurchaseHistoryAdapter extends RecyclerView.Adapter<ViewPurchaseHistoryAdapter.ViewPurchaseHistoryViewHolder> {

    private final LayoutInflater layoutInflater;
    private final ArrayList<Order> orders;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final DecimalFormat df2 = new DecimalFormat("0.0");

    public ViewPurchaseHistoryAdapter(LayoutInflater layoutInflater, ArrayList<Order> orders) {
        this.layoutInflater = layoutInflater;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewPurchaseHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPurchaseHistoryViewHolder(layoutInflater.inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPurchaseHistoryViewHolder holder, int position) {
//        holder.setOrderProduct(orders.get(position));

        Order order = orders.get(position);

        holder.tvName.setText(order.getProduct_name());
        holder.tvPrice.setText("RM " + df.format(parseDouble(order.getProduct_price())));
        holder.tvQuantity.setText("Quantity: " + order.getQuantity());
        Picasso.get()
                .load(order.getProduct_img())
                .into(holder.imgProduct);

        holder.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = String.valueOf(holder.ratingBar.getRating());

                if(rating.equals("0.0")) {
                    Toast.makeText(v.getContext(), "Rating not saved. Select star(s) to rate the item.", Toast.LENGTH_SHORT).show();
                }

                if(!rating.equals("0.0")) {
                    DocumentReference docRef = db.collection("products").document(order.getSKU());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                    int product_rating_times = Integer.parseInt(document.get("product_rating_times").toString());
                                    int new_product_rating_times = product_rating_times + 1;
                                    double product_rating = Double.parseDouble(document.get("product_rating").toString());

                                    double new_rating = ((product_rating_times * product_rating) + Double.parseDouble(rating)) / new_product_rating_times;

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("product_rating", "" + df2.format(parseDouble(String.valueOf(new_rating))));
                                    data.put("product_rating_times", "" + new_product_rating_times);

                                    db.collection("products").document(order.getSKU())
                                            .set(data, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                    Toast.makeText(v.getContext(), "Rating saved.", Toast.LENGTH_SHORT).show();
                                                    holder.btnRate.setText("Rated");
                                                    holder.btnRate.setEnabled(false);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(orders == null)
            return 0;
        else
            return orders.size();
    }

    public class ViewPurchaseHistoryViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvName, tvPrice, tvQuantity;
        private final ImageView imgProduct;
        private final RatingBar ratingBar;
        private final Button btnRate;

        public ViewPurchaseHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_product_name);
            this.tvPrice = itemView.findViewById(R.id.tv_price);
            this.tvQuantity = itemView.findViewById(R.id.tv_quantity);
            this.imgProduct = itemView.findViewById(R.id.img_product);
            this.ratingBar = itemView.findViewById(R.id.ratingBar);
            this.btnRate = itemView.findViewById(R.id.btnRate);
        }
    }
}
