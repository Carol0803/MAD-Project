package w2.g16.odds.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import w2.g16.odds.R;

public class VerifyCodeActivity extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifycode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        next = (Button) findViewById(R.id.nextPageV);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetPass();
            }
        });
    }

    public void openForgotPass(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void openResetPass(){
        Intent intent = new Intent(this, ResetPassActivity.class);
        startActivity(intent);
    }
}