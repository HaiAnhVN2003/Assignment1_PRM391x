package com.example.prm391x_asm1_anhnh07178funixeduvn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.*;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class MessageActivity extends AppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 1;
    EditText mobileNo, message, sTime;
    RadioButton radioHour, radioMinute, radioSecond;
    Button btnSMS;
    private static final int SMS_PERMISSION_CODE = 100;
    final Handler handler = new Handler(Looper.getMainLooper());

    public void onRadioButtonClicked(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        checkPermission(Manifest.permission.SEND_SMS, SMS_PERMISSION_CODE);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        final int REQUEST_READ_PHONE_STATE = 1;

        mobileNo = findViewById(R.id.etRecipient);
        message = findViewById(R.id.etMessage);
        sTime = findViewById(R.id.etTime);
        radioHour = findViewById(R.id.radioHour);
        radioMinute = findViewById(R.id.radioMinute);
        radioSecond = findViewById(R.id.radioSecond);
        btnSMS = findViewById(R.id.btnSMS);

        String regexStr = "^[+]?[0-9]{10,13}$";

        btnSMS.setOnClickListener(view -> {

            if (mobileNo.getText().toString().isEmpty() || message.getText().toString().isEmpty()) {
                Toast.makeText(MessageActivity.this, "Please check your information!", Toast.LENGTH_SHORT).show();
            } else if (!mobileNo.getText().toString().matches(regexStr)) {
                Toast.makeText(MessageActivity.this, "The phone number is not correct, please check!", Toast.LENGTH_SHORT).show();
            } else if (sTime.getText().toString().isEmpty()) {
                Toast.makeText(MessageActivity.this, "Please set time first.", Toast.LENGTH_SHORT).show();
            } else {

                int time = Integer.parseInt(sTime.getText().toString());
                boolean checked = ((MaterialButton) view).isChecked();
                
                if (view.getId() == R.id.radioHour) {
                    if (checked) {
                        time = time * 3600000;
                        Toast.makeText(getApplicationContext(), "Message will be sent after " + (time /= (3600 * 1000)) + " hours.", Toast.LENGTH_LONG).show();
                    }
                } else if (view.getId() == R.id.radioMinute) {
                    if (checked) {
                        time = time * 60000;
                        Toast.makeText(getApplicationContext(), "Message will be sent after " + (time /= (60 * 1000)) + " minutes.", Toast.LENGTH_LONG).show();
                    }
                } else if (view.getId() == R.id.radioMinute) {
                    if (checked) {
                        time *= 1000;
                        Toast.makeText(getApplicationContext(), "Message will be sent after " + (time / 1000) + " seconds.", Toast.LENGTH_LONG).show();
                    }
                }

                System.out.println(time);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    handler.postDelayed(() -> {
                        String no = mobileNo.getText().toString();
                        String msg = message.getText().toString();

                        //Getting intent and PendingIntent instance
                        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                        //Get the SmsManager instance and call the sendTextMessage method to send message
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(no, null, msg, pi,null);

                        Toast.makeText(this, "Message has been sent!", Toast.LENGTH_SHORT).show();
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
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Message Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Message Permission Denied", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}