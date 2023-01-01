package w2.g16.odds.ordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toolbar;

import com.google.firebase.Timestamp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityOrderPlacedBinding;
import w2.g16.odds.model.Address;
import w2.g16.odds.model.Order;

public class OrderPlacedActivity extends AppCompatActivity {

    private ActivityOrderPlacedBinding binding;
    private String delivery_method;
    private Address address;
    private String payment_method;
    private Date deliveryTime;
    private Timestamp delivery_time;
    private String shopname;
    private ArrayList<Order> order_lists;
    private Checkout2Adapter adapter;
    private String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String data = prefs.getString("string_id", "no id"); //no id: default value
        delivery_method = prefs.getString("delivery_method", "");
        String json_address = prefs.getString("address", "");
        address = gson.fromJson(json_address, Address.class);
        String json_delivery_time = prefs.getString("delivery_time", "");
        deliveryTime = gson.fromJson(json_delivery_time, Date.class);
        shopname = prefs.getString("shopname", "");
        total = prefs.getString("total", "");
        String json_order_lists = prefs.getString("order", "");
        Type type = new TypeToken< ArrayList < Order >>() {}.getType();
        order_lists = gson.fromJson(json_order_lists, type);

        /*Intent intent = getIntent();
        delivery_method = intent.getStringExtra("delivery_method");
        address = (Address) intent.getSerializableExtra("address");
        deliveryTime = (Date) intent.getSerializableExtra("delivery_time");
        delivery_time = new Timestamp(deliveryTime);
        shopname = intent.getStringExtra("shopname");
        total = intent.getStringExtra("total");*/

        binding.tvShopName.setText(shopname);
        binding.tvDeliveryMethod.setText(delivery_method);
        binding.tvRecipientName.setText(address.getReceiver_name());
        binding.tvTel.setText(address.getReceiver_tel());
        binding.tvAddr1.setText(address.getAddr1());
        binding.tvAddr2.setText(address.getAddr2());
        binding.tvCity.setText(address.getCity());
        binding.tvPostcode.setText(address.getPostcode());
        binding.tvState.setText(address.getState());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        binding.tvDeliveryTimeDate.setText(formatter.format(deliveryTime));

//        order_lists = (ArrayList<Order>) intent.getSerializableExtra("order");
        adapter = new Checkout2Adapter(getLayoutInflater(), order_lists);
        binding.recItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recItems.setAdapter(adapter);

        binding.tvTotal.setText(total);

    }

    public void fnGoOrder(View view) {
        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
    }
}