package w2.g16.odds.ordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.internal.http2.Header;
import w2.g16.odds.databinding.ActivityChoosePaymentBinding;
import w2.g16.odds.databinding.ActivityConfirmPaymentBinding;

public class ConfirmPaymentActivity extends AppCompatActivity {

    private ActivityConfirmPaymentBinding binding;
    private String orderID, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get the orderID from the query parameter
        Uri redirectUri = getIntent().getData();
        orderID = redirectUri.getQueryParameter("token");

        //set the orderID string to the UI
        binding.tvPaymentAmount.setText("Amount: " + amount);
        binding.tvPaymentID.setText("Order ID: " +orderID);

        //add an onClick listener to the confirm button
        binding.btnConfirmPayment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                captureOrder(orderID); //function to finalize the payment
            }
        });
    }

    void captureOrder(String orderID){
        //get the accessToken from MainActivity
        String accessToken = CheckoutActivity2.getMyAccessToken();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer " + accessToken);

        client.post("https://api-m.sandbox.paypal.com/v2/checkout/orders/"+orderID+"/capture", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("RESPONSE", responseString);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                try {
                    JSONObject jobj = new JSONObject(responseString);
                    //redirect back to home page of app
                    Intent intent = new Intent(getApplicationContext(), OrderPlacedActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}