package com.bhimoffline.truedev.bhimoffline.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhimoffline.truedev.bhimoffline.R;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;

/**
 * Created by rahul on 18-Mar-17.
 */

public class SendToBottomSheetFragment extends BottomSheetFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_send_to_mobile_no, container, false);
    }
}
