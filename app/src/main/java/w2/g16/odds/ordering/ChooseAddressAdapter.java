package w2.g16.odds.ordering;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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

        String recipient_name = address.getReceiver_name();
        String receipient_tel = address.getReceiver_tel();
        String addr1 = address.getAddr1();
        String addr2 = address.getAddr2();
        String city = address.getCity();
        String postcode = address.getPostcode();
        String state = address.getState();
        String isDefault = address.getIsDefault();

        holder.tvRecipientName.setText(recipient_name);
        holder.tvTel.setText(receipient_tel);
        holder.tvAddr1.setText(addr1);
        holder.tvAddr2.setText(addr2);
        holder.tvCity.setText(city);
        holder.tvPostcode.setText(postcode);
        holder.tvState.setText(state);

        /*if(isDefault.equals("true")){
            holder.checkBox.setChecked(true);
        }*/

        holder.checkBox.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectedPosition==position){
            holder.checkBox.setChecked(true);

            Address address_choosen = new Address(recipient_name, receipient_tel, addr1, addr2, city, postcode, state, isDefault);

            Intent intent = new Intent("return_address");
            intent.putExtra("objAddress", address_choosen);
            LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);
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
