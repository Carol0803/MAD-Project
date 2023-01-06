package w2.g16.odds.ordering;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityChooseAddressBinding;
import w2.g16.odds.databinding.ActivityChoosePaymentBinding;

public class ChoosePaymentActivity extends AppCompatActivity {

    private ActivityChoosePaymentBinding binding;
    private String payment_method;

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    String encodeStringToBase64(){
        String input = "AUATZ-aD33tA9YF3z69as1CFD3ybbLCHEdWjkTEMG5Gh6LF8rsMYTTB7PgVFFDPVWCO60o6xj6Zb2hxH:EFhxoTKZZkgGTqYIAtmDaYszeC2Pt8hi29b4khPQM_Jh_ltfbYJ3XYsOcnIFymT0VGSQNba8VgapApg0";
        String encodedString = Base64.getEncoder().encodeToString(input.getBytes());
        return encodedString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void getAccessToken(){
        String AUTH = encodeStringToBase64();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/x-www-form-urlencoded");
        client.addHeader("Authorization", "Basic "+ AUTH);
        String jsonString = "grant_type=client_credentials";

        HttpEntity entity = new StringEntity(jsonString, "utf-8");

        client.post(this, "https://api-m.sandbox.paypal.com/v1/oauth2/token", entity, "application/x-www-form-urlencoded",new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("RESPONSE", response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    accessToken = jobj.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

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

        binding.rbCash.setChecked(true);
        payment_method = "Cash";
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbCash:
                if (checked)
                    payment_method = "Cash";
                break;
            case R.id.rbCreditCard:
                if (checked)
                    payment_method = "PayPal";
                break;
        }
    }

    public void fnNext(View view) {
        Intent intent = new Intent();
        intent.putExtra("payment_method", payment_method);
        setResult(RESULT_OK, intent);
        finish();
    }
}