package com.example.prm391x_asm1_anhnh07178funixeduvn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;

import java.util.Objects;

public class PhoneActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 1;
    EditText etPhone, sTime;
    RadioButton radioHour, radioMinute, radioSecond;
    Button btnPhone;
    private static final int PHONE_PERMISSION_CODE = 100;
    final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        final int REQUEST_READ_PHONE_STATE = 1;

        etPhone = findViewById(R.id.etPhone);
        sTime = findViewById(R.id.etTime);
        radioHour = findViewById(R.id.radioHour);
        radioMinute = findViewById(R.id.radioMinute);
        radioSecond = findViewById(R.id.radioSecond);
        btnPhone = findViewById(R.id.btnPhone);

        checkPermission(Manifest.permission.CALL_PHONE, PHONE_PERMISSION_CODE);

        String regexStr = "^[+]?[0-9]{10,13}$";

        btnPhone.setOnClickListener(view -> {
            if (etPhone.getText().toString().isEmpty()) {
                Toast.makeText(PhoneActivity.this, "Please input phone number first.", Toast.LENGTH_SHORT).show();
            } else if (!etPhone.getText().toString().matches(regexStr)) {
                Toast.makeText(PhoneActivity.this, "The phone number is not correct, please check!", Toast.LENGTH_SHORT).show();
            } else if (sTime.getText().toString().isEmpty()) {
                Toast.makeText(PhoneActivity.this, "Please set time first.", Toast.LENGTH_SHORT).show();
            } else {
                int time = Integer.parseInt(sTime.getText().toString());

                if (radioHour.isChecked()) {
                    time *= 3600000;
                    Toast.makeText(getApplicationContext(), "A call will be done after " + (time /= (3600 * 1000)) + " hours.", Toast.LENGTH_LONG).show();
                } else if (radioMinute.isChecked()) {
                    time *= 60000;
                    Toast.makeText(getApplicationContext(), "A call will be done after " + (time /= (60 * 1000)) + " minutes.", Toast.LENGTH_LONG).show();
                } else if (radioSecond.isChecked()) {
                    time *= 1000;
                    Toast.makeText(getApplicationContext(), "A call will be done after " + (time / 1000) + " seconds.", Toast.LENGTH_LONG).show();
                }

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PhoneActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    handler.postDelayed(() -> {
                        String number = etPhone.getText().toString();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        startActivity(callIntent);
                    }, (long) time);
                }
            }
        });
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Phone Call Permission Granted", Toast.LENGTH_SHORT) .show();
            } else {
                Toast.makeText(this, "Phone Call Permission Denied", Toast.LENGTH_SHORT) .show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}