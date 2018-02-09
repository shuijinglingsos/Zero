package com.elf.zerodemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.elf.zerodemo.R;
import com.elf.zerodemo.widget.GesturePasswordPointView;
import com.elf.zerodemo.widget.GesturePasswordWidget;

import java.util.ArrayList;
import java.util.List;

public class GesturePasswordActivity extends AppBaseActivity {

    private GesturePasswordWidget mGesturePasswordWidget;
    private TextView mTxtNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_password);

        mGesturePasswordWidget = (GesturePasswordWidget) findViewById(R.id.gesturePasswordWidget);
        mTxtNumber = (TextView) findViewById(R.id.txtNumber);

        mGesturePasswordWidget.setOnGestureLockComplete(new GesturePasswordWidget.GestureLockComplete() {
            @Override
            public void complete() {
                List<GesturePasswordPointView> list = mGesturePasswordWidget.getSelectedPointViews();
                List<Integer> iii = new ArrayList<Integer>();
                for (GesturePasswordPointView pv : list) {
                    iii.add(pv.index + 1);
                }
                mTxtNumber.setText(iii.toString().replace("[", "").replace("]", "").replace(",", "  "));

                mGesturePasswordWidget.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGesturePasswordWidget.reset();
                    }
                }, 1000);
            }
        });
    }
}
