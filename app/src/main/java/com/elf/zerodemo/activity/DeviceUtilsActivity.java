package com.elf.zerodemo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.elf.zero.utils.DeviceUtils;
import com.elf.zerodemo.R;

public class DeviceUtilsActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_utils);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permission = {Manifest.permission.READ_PHONE_STATE};
            ActivityCompat.requestPermissions(this, permission, 1);
            return;
        }

        showData();
    }

    private void showData(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"没有权限",Toast.LENGTH_SHORT).show();
        }


        StringBuilder sb = new StringBuilder();

        sb.append("设备型号：").append(DeviceUtils.getDeviceModel());
        sb.append("\n设备ID：").append(DeviceUtils.getDeviceId());
        sb.append("\nMAC地址：").append(DeviceUtils.getMacAddress());
        sb.append("\n电话号码：").append(DeviceUtils.getTelephoneNumber());
        sb.append("\n运营商名称：").append(DeviceUtils.getNetworkOperatorName());

        ((TextView) findViewById(R.id.textView)).setText(sb.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        showData();
    }
}
