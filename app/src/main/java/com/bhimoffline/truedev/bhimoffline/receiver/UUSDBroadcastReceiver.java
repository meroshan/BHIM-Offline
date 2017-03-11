package com.bhimoffline.truedev.bhimoffline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.bhimoffline.truedev.bhimoffline.R;
import com.bhimoffline.truedev.bhimoffline.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.bhimoffline.truedev.bhimoffline.login.LoginActivity1.myPref;

/**
 * Created by rahul on 1/2/2017.
 */

public class UUSDBroadcastReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        String words[] = message.toLowerCase().split("\\s");
        String balance = "Unable to fetch balance";
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(words));
        if (keywords.contains("your") && keywords.contains("balance") && (keywords.contains("rs") || keywords.contains("rs."))) {
            int pos = 0;
            if (keywords.contains("rs")) {
                pos = keywords.indexOf("rs");
            } else {
                if (keywords.contains("rs.")) {
                    pos = keywords.indexOf("rs.");
                }
            }
            //Toast.makeText(context, "pos=" + pos + " " + keywords.get(pos + 1), Toast.LENGTH_SHORT).show();
            if (pos + 1 <= keywords.size()) {
                String[] s = (keywords.get(pos + 1)).split(",");
                balance = "Rs " + s[0];
            }
        }
//        else if (keywords.contains("rs") || keywords.contains("spl")) {
//            int pos = keywords.indexOf("rs");
//            String[] s = (keywords.get(pos + 1)).split(",");
//            balance = "Rs " + s[0];
//        }

        TextView balance_balance_card = (TextView) MainActivity.getInstance().findViewById(R.id.balance_card_balance);
        //TextView ussd_message = (TextView) MainActivity.getInstance().findViewById(R.id.ussd_message);
        //ussd_message.setText(message);
        if (balance.equals("Unable to fetch balance")) {
            balance_balance_card.setTextSize(24);
        } else {
            balance_balance_card.setTextSize(48);
        }
        balance_balance_card.setText(balance);

        TextView balance_user_detail_card = (TextView) MainActivity.getInstance().findViewById(R.id.user_detail_card_balance);
        balance_user_detail_card.setText(balance);

        TextView balance_card_last_updated = (TextView) MainActivity.getInstance().findViewById(R.id.balance_card_last_updated);
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        balance_card_last_updated.setText(date);

        sharedPreferences = MainActivity.getInstance().getSharedPreferences(myPref, 0);
        sharedPreferences.edit().putString("balance", balance).commit();
        sharedPreferences.edit().putString("last_updated", date).commit();

        //Toast.makeText(MainActivity.getInstance(), "message :" + balance, Toast.LENGTH_SHORT).show();
    }
}
