package w2.g16.odds.ordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckoutBinding;
import w2.g16.odds.model.Address;
import w2.g16.odds.model.Order;

public class CheckoutActivity extends AppCompatActivity {

    private static ActivityCheckoutBinding binding;
    private DatePickerDialog datePicker;
    private String delivery_method;
    private Address address;
    private String selected_order_shopname;
    private ArrayList<Order> orders;
    private static Date delivery_time;
    private String total;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //set toolbar as actionbar
        //back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });

        dbGetDefaultAddress();

        binding.rbDelivery.setChecked(true);
        binding.rbDelivery.setBackgroundResource(R.drawable.ic_delivery_checked);
        delivery_method = "Home Delivery";

        binding.btnChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        Intent intent = getIntent();
        selected_order_shopname = intent.getStringExtra("shopname");
        orders = (ArrayList<Order>) intent.getSerializableExtra("order");
        total = intent.getStringExtra("total");

    }

    public void fnChangeAddress(View view) {
        startActivityForResult(new Intent(this, ChooseAddressActivity.class), 1);
    }

    public void fnNext(View view) {
        Intent intent = new Intent(this, CheckoutActivity2.class);
        intent.putExtra("delivery_method", delivery_method);
        intent.putExtra("address", address);
        intent.putExtra("delivery_time", delivery_time);
        intent.putExtra("shopname", selected_order_shopname);
        intent.putExtra("order", orders);
        intent.putExtra("total", total);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbDelivery:
                if (checked)
                    binding.rbDelivery.setBackgroundResource(R.drawable.ic_delivery_checked);
                    binding.rbSelfCollection.setBackgroundResource(R.drawable.ic_self_collection);
                    delivery_method = "Home Delivery";
                break;
            case R.id.rbSelfCollection:
                if (checked)
                    binding.rbSelfCollection.setBackgroundResource(R.drawable.ic_self_collection_checked);
                    binding.rbDelivery.setBackgroundResource(R.drawable.ic_delivery);
                    delivery_method = "Self Collection";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                address = (Address) data.getSerializableExtra("objAddress");

                binding.tvRecipientName.setText(address.getReceiver_name());
                binding.tvTel.setText(address.getReceiver_tel());
                binding.tvAddr1.setText(address.getAddr1());
                binding.tvAddr2.setText(address.getAddr2());
                binding.tvCity.setText(address.getCity());
                binding.tvPostcode.setText(address.getPostcode());
                binding.tvState.setText(address.getState());
            }
        }

    }

    public void dbGetDefaultAddress(){
        final String TAG = "Read Data Activity";
        db.collection("customer/username/address")
                .whereEqualTo("default", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String receiver_name = null;
                            String receiver_tel = null;
                            String addr1 = null;
                            String addr2 = null;
                            String city = null;
                            String postcode = null;
                            String state = null;
                            String location = null;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String addrID = document.getId();
                                addr1 = document.get("addr1").toString();
                                addr2 = document.get("addr2").toString();
                                city = document.get("city").toString();
                                postcode = document.get("postcode").toString();
                                state = document.get("state").toString();
                                location = document.get("location").toString();
                                receiver_name = document.get("receiver_name").toString();
                                receiver_tel = document.get("receiver_tel").toString();
                            }

                            binding.tvRecipientName.setText(receiver_name);
                            binding.tvTel.setText(receiver_tel);
                            binding.tvAddr1.setText(addr1);
                            binding.tvAddr2.setText(addr2);
                            binding.tvCity.setText(city);
                            binding.tvPostcode.setText(postcode);
                            binding.tvState.setText(state);

                            address = new Address(receiver_name, receiver_tel, addr1, addr2, city, postcode, state, null);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis() + (1000*60*60*24*10));
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            binding.tvDTime.setText("Date: " + day + " " + Month.of(month) + ", " + year);
            delivery_time = new Date(year-1900, month, day);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


}
