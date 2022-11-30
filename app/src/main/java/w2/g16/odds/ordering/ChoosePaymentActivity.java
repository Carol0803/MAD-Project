package w2.g16.odds.ordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityChooseAddressBinding;
import w2.g16.odds.databinding.ActivityChoosePaymentBinding;

public class ChoosePaymentActivity extends AppCompatActivity {

    private ActivityChoosePaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoosePaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //set toolbar as actionbar
        //back button
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckoutActivity2.class));
            }
        });
    }

    public void fnNext(View view) {
        startActivity(new Intent(this, CheckoutActivity2.class));
    }
}