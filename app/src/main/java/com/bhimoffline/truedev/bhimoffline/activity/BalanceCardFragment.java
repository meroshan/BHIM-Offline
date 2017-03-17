package com.bhimoffline.truedev.bhimoffline.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bhimoffline.truedev.bhimoffline.R;

import static com.bhimoffline.truedev.bhimoffline.activity.MainActivity.BALANCE;
import static com.bhimoffline.truedev.bhimoffline.login.LoginActivity.myPref;

/**
 * Created by rahul on 17-Mar-17.
 */

public class BalanceCardFragment extends Fragment implements View.OnClickListener {
    TextView balance_card_balance;
    TextView balance_card_last_updated;
    Button update_balance, other_services, get_started_install;
    SharedPreferences sharedPreferences;
    private BottomSheetListener mbottomSheetListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mbottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            // TODO: 18-Mar-17 report to fabric
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balance_card, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(myPref, 0);
        balance_card_balance = (TextView) view.findViewById(R.id.balance_card_balance);
        balance_card_last_updated = (TextView) view.findViewById(R.id.balance_card_last_updated);

        String balance = sharedPreferences.getString(BALANCE, "Balance");
        balance_card_balance.setText(balance);
        balance_card_last_updated.setText(sharedPreferences.getString("last_updated ", " never"));

        balance_card_balance.setOnClickListener(this);
        update_balance = (Button) view.findViewById(R.id.update_balance);
        update_balance.setOnClickListener(this);
        other_services = (Button) view.findViewById(R.id.other_services);
        other_services.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_balance:
                askForPermission();
                //makeCall("*123");
                break;
            case R.id.other_services:
                askForPermission();
                mbottomSheetListener.showMenuSheet(R.menu.other_services, "Services");
                break;
        }
    }

    private void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                showMessage();
            }
        }
    }

    private void showMessage() {
        DialogFragment df = new MyDialog();
        df.show(getFragmentManager(), "tag");
    }

    public interface BottomSheetListener {
        void showMenuSheet(int id, String title);
    }
}

/*
public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
 */