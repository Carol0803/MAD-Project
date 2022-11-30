package w2.g16.odds.ordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckout2Binding;
import w2.g16.odds.databinding.ActivityCheckoutBinding;

public class CheckoutActivity2 extends AppCompatActivity {

    private ActivityCheckout2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckout2Binding.inflate(getLayoutInflater());
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

    public void fnGoChoosePayment(View view) {
        startActivity(new Intent(getApplicationContext(), ChoosePaymentActivity.class));
    }

    public void fnProceed(View view) {
        startActivity(new Intent(getApplicationContext(), OrderPlacedActivity.class));
    }
}