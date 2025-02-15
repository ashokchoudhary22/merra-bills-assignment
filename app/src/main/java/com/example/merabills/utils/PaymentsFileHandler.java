package com.example.merabills.utils;

import android.content.Context;
import android.util.Log;

import com.example.merabills.enums.PaymentMode;
import com.example.merabills.models.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentsFileHandler {
    private static final String FILE_NAME = "LastPayment.txt";

    public static void savePaymentsToFile(Context context, List<Payment> payments) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (Payment payment : payments) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("amount", payment.amount);
                jsonObject.put("mode", payment.mode);
                jsonObject.put("provider", payment.provider);
                jsonObject.put("transactionReference", payment.transactionReference);

                jsonArray.put(jsonObject);
            }

            try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                 OutputStreamWriter osw = new OutputStreamWriter(fos);
                 BufferedWriter writer = new BufferedWriter(osw)) {
                 writer.write(jsonArray.toString());
            }
        } catch (JSONException | IOException e) {
            Log.e("PaymentFileHandler", "Error saving payments file", e);
        }
    }

    public static List<Payment> readPaymentsFromFile(Context context) {
        List<Payment> payments = new ArrayList<>();
        StringBuilder jsonString = new StringBuilder();

        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonString.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                float amount = Float.parseFloat(jsonObject.getString("amount"));
                PaymentMode mode = PaymentMode.valueOf(jsonObject.getString("mode"));
                String provider = null;
                String transactionReference = null;
                if(jsonObject.has("provider"))
                    provider = jsonObject.getString("provider");
                if(jsonObject.has("transactionReference"))
                    transactionReference = jsonObject.getString("transactionReference");

                payments.add(new Payment(amount, mode, provider, transactionReference));
            }
        } catch (IOException | JSONException e) {
            Log.e("PaymentFileHandler", "Error reading file", e);
        }
        return payments;
    }
}
