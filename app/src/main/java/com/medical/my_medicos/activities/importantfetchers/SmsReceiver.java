// SmsReceiver.java

package com.medical.my_medicos.activities.importantfetchers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.medical.my_medicos.activities.login.EnterOtp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get the SMS message
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Log.d(TAG, "Received SMS: " + message);
                    // Extract the OTP from the message and auto-fill
                    handleOtpReceived(context, message);
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // SMS retrieval timed out
                    Log.d(TAG, "SMS retrieval timed out");
                    break;
            }
        }
    }

    private void handleOtpReceived(Context context, String message) {
        // Implement your OTP extraction logic here
        String otp = extractOtp(message);

        if (otp != null && !otp.isEmpty()) {
            Log.d(TAG, "Extracted OTP: " + otp);
            // Notify the EnterOtp activity to auto-fill the OTP
            notifyEnterOtpActivity(context, otp);
        } else {
            Log.d(TAG, "Failed to extract OTP");
        }
    }


    private String extractOtp(String message) {
        // Implement your OTP extraction logic here
        // Currently, a simple regex pattern is used for demonstration
        // You should replace this with your actual OTP extraction logic
        Pattern pattern = Pattern.compile("(\\d{6})");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private void notifyEnterOtpActivity(Context context, String otp) {
        // Notify the EnterOtp activity to auto-fill the OTP
        Intent otpIntent = new Intent(context, EnterOtp.class);
        otpIntent.setAction(EnterOtp.AUTO_FILL_OTP_ACTION);
        otpIntent.putExtra(EnterOtp.EXTRA_AUTO_FILL_OTP, otp);
        context.startActivity(otpIntent);
    }
}
