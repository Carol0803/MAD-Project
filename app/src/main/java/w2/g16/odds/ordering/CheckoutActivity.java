package w2.g16.odds.ordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import w2.g16.odds.MainActivity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckoutBinding;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;

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



    }

    public void fnChangeAddress(View view) {
        startActivity(new Intent(this, ChooseAddressActivity.class));
    }

    public void fnNext(View view) {
        startActivity(new Intent(this, CheckoutActivity2.class));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbDelivery:
                if (checked)
                    binding.rbDelivery.setBackgroundResource(R.drawable.ic_delivery_checked);
                    binding.rbSelfCollection.setBackgroundResource(R.drawable.ic_self_collection);
                    break;
            case R.id.rbSelfCollection:
                if (checked)
                    binding.rbSelfCollection.setBackgroundResource(R.drawable.ic_self_collection_checked);
                    binding.rbDelivery.setBackgroundResource(R.drawable.ic_delivery);
                    break;
        }
    }
}