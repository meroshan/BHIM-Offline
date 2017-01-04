package com.bhimoffline.truedev.bhimoffline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.bhimoffline.truedev.bhimoffline.R;
import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rahul on 1/2/2017.
 */

public class UUSDBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        String words[] = message.toLowerCase().split("\\s");
        String balance = "Unable to fetch balance, try again";
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(words));
        if (keywords.contains("cr")) {
            int pos = keywords.indexOf("cr");
            balance = "Rs " + keywords.get(pos - 1);
        } else if (keywords.contains("rs")) {
            int pos = keywords.indexOf("rs");
            String[] s = (keywords.get(pos + 1)).split(",");
            balance = "Rs " + s[0];
        }

        TextView balance_balance_card = (TextView) MainActivity.getInstance().findViewById(R.id.balance_balance_card);
        if (balance.equals("Unable to fetch balance, try again")) {
            balance_balance_card.setTextSize(24);
        } else {
            balance_balance_card.setTextSize(48);
        }
        balance_balance_card.setText(balance);

        TextView balance_user_detail_card = (TextView) MainActivity.getInstance().findViewById(R.id.balance_user_detail_card);
        balance_user_detail_card.setText(balance);

        Toast.makeText(MainActivity.getInstance(), "message :" + balance, Toast.LENGTH_SHORT).show();
    }
}
