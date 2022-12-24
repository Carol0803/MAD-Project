package w2.g16.odds.ordering;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.Timestamp;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckout2Binding;
import w2.g16.odds.databinding.ActivityCheckoutBinding;
import w2.g16.odds.model.Address;

public class CheckoutActivity2 extends AppCompatActivity {

    private ActivityCheckout2Binding binding;
    private String delivery_method;
    private Address address;
    private String payment_method;
    private Date deliveryTime;
    private Timestamp delivery_time;

    static String accessToken = "A21AAL3R4kUh6zhKc2csEPNiu_3uaUpo_aBGMRklMLTVHMB6TqHbwsiFMTAuQho0aJCFEAnVSRJ1ezDexLltXFBa_Oa_XRiEQ";
    String amount = "1.00";

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
                startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
            }
        });

        Intent intent = getIntent();
        delivery_method = intent.getStringExtra("delivery_method");
        address = (Address) intent.getSerializableExtra("address");
        deliveryTime = (Date) intent.getSerializableExtra("delivery_time");
        delivery_time = new Timestamp(deliveryTime);

        binding.tvRecipientName.setText(address.getReceiver_name());
        binding.tvTel.setText(address.getReceiver_tel());
        binding.tvAddr1.setText(address.getAddr1());
        binding.tvAddr2.setText(address.getAddr2());
        binding.tvCity.setText(address.getCity());
        binding.tvPostcode.setText(address.getPostcode());
        binding.tvState.setText(address.getState());

        binding.tvEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        binding.tvDeliveryTimeDate.setText(formatter.format(deliveryTime));

        binding.tvEditTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void fnGoChoosePayment(View view) {
        startActivityForResult(new Intent(this, ChoosePaymentActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                payment_method = data.getStringExtra("payment_method");
                binding.tvPaymentMethod.setText(payment_method);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void createOrder(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);

        String order = "{"
                + "\"intent\": \"CAPTURE\","
                + "\"purchase_units\": [\n" +
                "      {\n" +
                "        \"amount\": {\n" +
                "          \"currency_code\": \"MYR\",\n" +
                "          \"value\": \""+ amount + "\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\"application_context\": {\n" +
                "        \"brand_name\": \"TEST_STORE\",\n" +
                "        \"return_url\": \"payment://paypalpay\",\n" +
                "        \"cancel_url\": \"payment://paypalpay\"\n" +
                "    }}";
        HttpEntity entity = new StringEntity(order, "utf-8");

        client.post(this, "https://api-m.sandbox.paypal.com/v2/checkout/orders", entity, "application/json",new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("RESPONSE1", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.i("RESPONSE1", responseString);

                try {
                    JSONArray links = new JSONObject(responseString).getJSONArray("links");

                    //iterate the array to get the approval link
                    for (int i = 0; i < links.length(); ++i) {
                        String rel = links.getJSONObject(i).getString("rel");
                        if (rel.equals("approve")){
                            String link = links.getJSONObject(i).getString("href");
                            //redirect to this link via CCT
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(CheckoutActivity2.this, Uri.parse(link));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String getMyAccessToken(){
        return accessToken;
    }

    public void fnProceed(View view) {
        if(payment_method.equals("Cash")){
            Intent intent = new Intent(getApplicationContext(), OrderPlacedActivity.class);
            startActivity(intent);
        }
        if (payment_method.equals("PayPal"))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createOrder();
            }
    }
}