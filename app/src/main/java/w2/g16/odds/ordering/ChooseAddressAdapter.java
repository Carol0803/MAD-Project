package w2.g16.odds.ordering;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.model.Address;

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.ChooseAddressViewHolder> {

    private Activity activity;
    private final Vector<Address> addresses;
    private int selectedPosition = -1;


    public ChooseAddressAdapter(Activity activity, Vector<Address> addresses) {
        this.activity = activity;
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public ChooseAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);

        return new ChooseAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseAddressViewHolder holder, int position) {
        Address address = addresses.get(position);

        holder.tvRecipientName.setText(address.getReceiver_name());
        holder.tvTel.setText(address.getReceiver_tel());
        holder.tvAddr1.setText(address.getAddr1());
        holder.tvAddr2.setText(address.getAddr2());
        holder.tvCity.setText(address.getCity());
        holder.tvPostcode.setText(address.getPostcode());
        holder.tvState.setText(address.getState());

        holder.checkBox.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        holder.checkBox.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

//        ArrayList<Double> subtotal = new ArrayList<>();

        if (selectedPosition==position){
            holder.checkBox.setChecked(true);

            /*db.collection("customer/username/cart/" + shop.getShopID() + "/cart_product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Double total = 0.00;
                                Double subtotal = 0.00;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    String name = document.get("product_name").toString();
                                    String variation = document.get("variation_name").toString();
                                    String price = document.get("product_price").toString();
                                    String quantity = document.get("quantity").toString();

                                    subtotal = Double.parseDouble(price) * Double.parseDouble(quantity);
                                    total += subtotal;
                                }
                                Intent intent = new Intent("custom-message");
                                intent.putExtra("total",""+df.format(total));
                                LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });*/
        }
        else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public static class ChooseAddressViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRecipientName, tvTel, tvAddr1, tvAddr2, tvCity, tvPostcode, tvState;
        private CheckBox checkBox;


        public ChooseAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipientName = itemView.findViewById(R.id.tvRecipientName);
            tvTel = itemView.findViewById(R.id.tvTel);
            tvAddr1 = itemView.findViewById(R.id.tvAddr1);
            tvAddr2 = itemView.findViewById(R.id.tvAddr2);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvPostcode = itemView.findViewById(R.id.tvPostcode);
            tvState = itemView.findViewById(R.id.tvState);
            checkBox = itemView.findViewById(R.id.checkBox_address);

        }

    }
}
