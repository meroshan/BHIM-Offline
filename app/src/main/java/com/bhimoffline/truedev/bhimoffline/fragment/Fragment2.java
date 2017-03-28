package com.bhimoffline.truedev.bhimoffline.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bhimoffline.truedev.bhimoffline.R;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import static com.bhimoffline.truedev.bhimoffline.login.LoginActivity.myPref;

/**
 * Created by rahul on 26-Mar-17.
 */

public class Fragment2 extends android.support.v4.app.Fragment {
    SharedPreferences sharedPreferences;
    BottomDialog services, send_money, send_to_mobile, my_account, upi_pin, request_money, send_to_aadhar,
            send_to_payment_address, send_to_ifsc, send_to_mmid;
    EditText bottomsheet_mobile_no, bottomsheet_amount;

    private BottomSheetLayout bottomSheet;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(myPref, 0);
//        bottomSheet = (BottomSheetLayout) view.findViewById(R.id.bottomsheet);
//        bottomSheet.setPeekOnDismiss(true);

        sampleCustomView(null);
        return view;
    }


    public void bottomSheetSendToMmidCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_send_to_mmid, null);

        if (send_to_mmid != null)
            send_to_mmid.dismiss();

        send_to_mmid = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();
    }

    public void bottomSheetSendToIfscCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_send_to_ifsc, null);

        Button send_continue = (Button) customView.findViewById(R.id.send_continue);

        if (send_to_ifsc != null)
            send_to_ifsc.dismiss();

        send_to_ifsc = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();
        send_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makeCall("*99*1*5*SBIN0013181*20209377563*1");
                //makeCall("*99*1*1*9646458388*1");
                //makeCall("*99*1*2*merahulroshan@ybl*1");
                //makeCall("*99*1*1*9872497873*1");
            }
        });
    }

    public void bottomSheetSendToPaymentAddressCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_send_to_payment_address, null);

        if (send_to_payment_address != null)
            send_to_payment_address.dismiss();

        send_to_payment_address = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();
    }

    public void bottomSheetSendToAadharCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_send_to_aadhar, null);

        if (send_to_aadhar != null)
            send_to_aadhar.dismiss();

        send_to_aadhar = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();
    }

    public void bottomSheetRequestMoneyCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_request_money, null);

        if (request_money != null)
            request_money.dismiss();

        request_money = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();

        bottomsheet_mobile_no = (EditText) customView.findViewById(R.id.bottomsheet_mobile_no);
        bottomsheet_amount = (EditText) customView.findViewById(R.id.bottomsheet_amount);
    }

    public void bottomSheetUpiPinCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_upi_pin, null);

        if (upi_pin != null)
            upi_pin.dismiss();

        upi_pin = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();

        Button reset_pin, change_pin;
        reset_pin = (Button) customView.findViewById(R.id.reset_pin);
        change_pin = (Button) customView.findViewById(R.id.change_pin);

        reset_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upi_pin != null) {
                    upi_pin.dismiss();
                }
                //makeCall("*99*7*1");
            }
        });

        change_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upi_pin != null) {
                    upi_pin.dismiss();
                }
                //makeCall("*99*7*2");
            }
        });
    }

    public void bottomSheetMyAccountCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_my_account, null);

        if (my_account != null)
            my_account.dismiss();

        my_account = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();

        Button change_bank, my_details, my_payment_address, manage_beneficiary;

        change_bank = (Button) customView.findViewById(R.id.change_bank);
        my_details = (Button) customView.findViewById(R.id.my_details);
        my_payment_address = (Button) customView.findViewById(R.id.my_payment_address);
        manage_beneficiary = (Button) customView.findViewById(R.id.manage_beneficiary);

        change_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_account != null) {
                    my_account.dismiss();
                }
                //makeCall("*99*4*1");
            }
        });
        my_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_account != null) {
                    my_account.dismiss();
                }
                //makeCall("*99*4*3");
            }
        });
        my_payment_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_account != null) {
                    my_account.dismiss();
                }
                //makeCall("*99*4*4");
            }
        });
        manage_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_account != null) {
                    my_account.dismiss();
                }
                //makeCall("*99*4*5");
            }
        });
    }

    public void bottomSheetSendToMobileCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_send_to_mobile, null);

        if (send_to_mobile != null)
            send_to_mobile.dismiss();

        send_to_mobile = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();
    }

    public void bottomSheetSendMoneyCustomView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_send_money, null);

        if (send_money != null)
            send_money.dismiss();

        send_money = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();

        Button send_to_mobile_no, send_to_aadhar, send_to_payment_address, send_to_saved_beneficiary, send_to_ifsc, send_to_mmid;
        send_to_mobile_no = (Button) customView.findViewById(R.id.send_to_mobile_no);
        send_to_aadhar = (Button) customView.findViewById(R.id.send_to_aadhar);
        send_to_payment_address = (Button) customView.findViewById(R.id.send_to_payment_address);
        send_to_saved_beneficiary = (Button) customView.findViewById(R.id.send_to_saved_beneficiary);
        send_to_ifsc = (Button) customView.findViewById(R.id.send_to_ifsc);
        send_to_mmid = (Button) customView.findViewById(R.id.send_to_mmid);

        send_to_mobile_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_money != null) {
                    send_money.dismiss();
                }
                bottomSheetSendToMobileCustomView();
            }
        });

        send_to_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_money != null) {
                    send_money.dismiss();
                }
                bottomSheetSendToAadharCustomView();
            }
        });

        send_to_payment_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_money != null) {
                    send_money.dismiss();
                }
                bottomSheetSendToPaymentAddressCustomView();
            }
        });

        send_to_saved_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_money != null) {
                    send_money.dismiss();
                }
            }
        });

        send_to_ifsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_money != null) {
                    send_money.dismiss();
                }
                bottomSheetSendToIfscCustomView();
            }
        });

        send_to_mmid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_money != null) {
                    send_money.dismiss();
                }
                bottomSheetSendToMmidCustomView();
            }
        });
    }

    public void sampleCustomView(View view) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.bottomsheet_services, null);

        services = new BottomDialog.Builder(getActivity())
                .setCustomView(customView)
                .show();
        //.setPositiveText("Google Play")
        //.setNegativeText("Close")
        //.setTitle("Awesome!")
        //.setContent("Glad to see you like BottomDialogs! If you're up for it, we would really appreciate you reviewing us.")

        Button send_money, request_money, my_profile, pending_requests, transactions, upi_pin;
        send_money = (Button) customView.findViewById(R.id.send_money);
        request_money = (Button) customView.findViewById(R.id.request_money);
        my_profile = (Button) customView.findViewById(R.id.my_profile);
        pending_requests = (Button) customView.findViewById(R.id.pending_requests);
        transactions = (Button) customView.findViewById(R.id.transactions);
        upi_pin = (Button) customView.findViewById(R.id.upi_pin);

        send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (services != null) {
                    services.dismiss();
                }
                //sampleCustomView(view);
                bottomSheetSendMoneyCustomView();
            }
        });
        request_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services != null) {
                    services.dismiss();
                }
                bottomSheetRequestMoneyCustomView();
            }
        });
        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services != null) {
                    services.dismiss();
                }
                bottomSheetMyAccountCustomView();
            }
        });
        pending_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services != null) {
                    services.dismiss();
                }
                //makeCall("*99*5");
            }
        });
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services != null) {
                    services.dismiss();
                }
                //makeCall("*99*6");
            }
        });
        upi_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services != null) {
                    services.dismiss();
                }
                bottomSheetUpiPinCustomView();
            }
        });
    }

    private void shareOnWhatsApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String message = "Don't have Internet connection and still want to check your bank balance and send money? Download BHIM Offline" +
                " app and experience offline banking on a click.\n https://play.google.com/store/apps/details?id=com.sohaari.bhimoffline";
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(shareIntent);
    }

    private Boolean askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                showMessage();
                return false;
            }
        }
        return true;
    }


    private void showMessage() {
        android.support.v4.app.DialogFragment df = new MyDialog();
        df.show(getFragmentManager(), "tag");
    }

    public void makeCall(final String ussdCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent fetchBalanceIntent = new Intent(Intent.ACTION_CALL);
                fetchBalanceIntent.setData(Uri.parse("tel:" + ussdCode + Uri.encode("#")));

                //To Do - for the first time after getting permission, fetchBalanceIntent is not starting. Fix this.
                startActivity(fetchBalanceIntent);

            }
        }).start();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.share_whatsApp:
//                shareOnWhatsApp();
//                break;
//            case R.id.rate_play_store:
//                Intent bhimOffline = new Intent(Intent.ACTION_VIEW);
//                bhimOffline.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sohaari.bhimoffline"));
//                startActivity(bhimOffline);
//                break;
//            case R.id.update_balance:
//                if (askForPermission()) {
//                    makeCall("*99*3");
//                }
//                break;
//            case R.id.update_mini:
//                if (askForPermission()) {
//                    //mbottomSheetListener.showMenuSheet(R.menu.update_mini, "Services");
//                    sampleCustomView(v);
//                }
//                break;
////            case R.id.get_started_install:
////                Toast.makeText(instance, "Opening Play Store", Toast.LENGTH_SHORT).show();
////                Intent phonePe = new Intent(Intent.ACTION_VIEW);
////                phonePe.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.phonepe.app"));
////                startActivity(phonePe);
////                break;
//        }
//    }
}
