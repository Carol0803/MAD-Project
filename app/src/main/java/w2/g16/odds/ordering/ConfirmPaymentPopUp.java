package w2.g16.odds.ordering;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import w2.g16.odds.R;

public class ConfirmPaymentPopUp {

    /*public void showPopupWindow(final View view) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_confirm_payment_pop_up, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView tvPaymentAmount = popupView.findViewById(R.id.tvPaymentAmount);
        tvPaymentAmount.setText("");

        TextView tvPaymentID = popupView.findViewById(R.id.tvPaymentID);
        tvPaymentID.setText("");

        Button buttonEdit = popupView.findViewById(R.id.btnConfirmPayment);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //As an example, display the message
                Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        });

    }*/
}