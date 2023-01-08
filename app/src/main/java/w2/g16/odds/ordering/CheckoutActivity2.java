package w2.g16.odds.ordering;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import w2.g16.odds.R;
import w2.g16.odds.databinding.ActivityCheckout2Binding;
import w2.g16.odds.databinding.ActivityCheckoutBinding;
import w2.g16.odds.model.Address;
import w2.g16.odds.model.Order;

public class CheckoutActivity2 extends AppCompatActivity {

    private ActivityCheckout2Binding binding;
    private String delivery_method;
    private Address address;
    private String payment_method;
    private Date deliveryTime;
    private Timestamp delivery_time;
    private String shopname, shopID;
    private ArrayList<Order> order_lists;
    private Checkout2Adapter adapter;
    private String total;
    static String accessToken;
    private String amount;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        delivery_method = intent.getStringExtra("delivery_method");
        address = (Address) intent.getSerializableExtra("address");
        deliveryTime = (Date) intent.getSerializableExtra("delivery_time");
        shopname = intent.getStringExtra("shopname");
        shopID = intent.getStringExtra("shopID");
        order_lists = (ArrayList<Order>) intent.getSerializableExtra("order");
        adapter = new Checkout2Adapter(getLayoutInflater(), order_lists);

        binding.recProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recProducts.setAdapter(adapter);

        if(delivery_method.equals("Home Delivery")) {
            binding.tvRecipientName.setText(address.getReceiver_name());
            binding.tvTel.setText(address.getReceiver_tel());
            binding.tvAddr1.setText(address.getAddr1());
            binding.tvAddr2.setText(address.getAddr2());
            binding.tvCity.setText(address.getCity());
            binding.tvPostcode.setText(address.getPostcode());
            binding.tvState.setText(address.getState());
        }

        if(delivery_method.equals("Self Collection")){
            binding.tvAddress.setText("Pick Up Address");
            binding.tvEditAddress.setVisibility(View.INVISIBLE);
            dbGetShopAddress();
        }

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

        binding.tvShopName.setText(shopname);

        total = intent.getStringExtra("total");
        binding.tvTotalAmount.setText("RM: " + total);
        amount = total;

        getAccessToken();

    }

    public void dbGetShopAddress(){
        DocumentReference docRef = db.collection("shop").document(shopID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        binding.tvRecipientName.setText(document.get("shop_name").toString());
                        binding.tvTel.setText(document.get("shop_tel").toString());
                        binding.tvAddr1.setText(document.get("shop_addr1").toString());
                        binding.tvAddr2.setText(document.get("shop_addr2").toString());
                        binding.tvCity.setText(document.get("shop_city").toString());
                        binding.tvPostcode.setText(document.get("shop_postcode").toString());
                        binding.tvState.setText(document.get("shop_state").toString());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
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
                binding.btnProceed.setEnabled(true);

                Gson gson = new Gson();
                String json_address = gson.toJson(address);
                String json_order_lists = gson.toJson(order_lists);
                String json_deliveryTime = gson.toJson(deliveryTime);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("delivery_method", delivery_method);
                editor.putString("payment_method", payment_method);
                editor.putString("address", json_address);
                editor.putString("delivery_time", json_deliveryTime);
                editor.putString("shopname", shopname);
                editor.putString("shopID", shopID);
                editor.putString("order", json_order_lists);
                editor.putString("total", total);
                editor.commit();
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
        if(payment_method == null) {
            Toast.makeText(this,"Please choose payment method.",Toast.LENGTH_SHORT).show();
            binding.btnProceed.setEnabled(false);
        }
        else {
            if (payment_method.equals("Cash")) {
                Intent intent = new Intent(getApplicationContext(), OrderPlacedActivity.class);
            /*intent.putExtra("delivery_method", delivery_method);
            intent.putExtra("address", address);
            intent.putExtra("delivery_time", deliveryTime);
            intent.putExtra("shopname", shopname);
            intent.putExtra("order", order_lists);
            intent.putExtra("total", total);*/
                startActivity(intent);
            }
            if (payment_method.equals("PayPal")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createOrder();
                }
            }
        }
    }

    void getAccessToken(){
        String AUTH = "QVVBVFotYUQzM3RBOVlGM3o2OWFzMUNGRDN5YmJMQ0hFZFdqa1RFTUc1R2g2TEY4cnNNWVRUQjdQZ1ZGRkRQVldDTzYwbzZ4ajZaYjJoeEg6RUZoeG9US1paa2dHVHFZSUF0bURhWXN6ZUMyUHQ4aGkyOWI0a2hQUU1fSmhfbHRmYllKM1hZc09jbklGeW1UMFZHU1FOYmE4VmdhcEFwZzA=";
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
    }
}