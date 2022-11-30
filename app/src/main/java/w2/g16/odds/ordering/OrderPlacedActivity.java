package w2.g16.odds.ordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityChoosePaymentBinding;
import w2.g16.odds.databinding.ActivityOrderPlacedBinding;

public class OrderPlacedActivity extends AppCompatActivity {

    private ActivityOrderPlacedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    public void fnGoOrder(View view) {
        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
    }
}