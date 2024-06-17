package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName() + "My";
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catchData();
    }

    private void catchData() {
        String catchDataUrl = "https://api.jsonserve.com/oBVdUt"; // 改成你的飛行資料API URL
        ProgressDialog dialog = ProgressDialog.show(this, "讀取中", "請稍候", true);
        arrayList = new ArrayList<>();  // 確保arrayList已經初始化

        new Thread(() -> {
            try {
                URL url = new URL(catchDataUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    json.append(line);
                }

                JSONArray flightArray = new JSONArray(json.toString());

                for (int i = 0; i < flightArray.length(); i++) {
                    JSONObject flight = flightArray.getJSONObject(i);
                    String flightDate = flight.getString("FlightDate");
                    int flightNumber = flight.getInt("FlightNumber");
                    String departureAirportID = flight.getString("DepartureAirportID");
                    String arrivalAirportID = flight.getString("ArrivalAirportID");

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("FlightDate", flightDate);
                    hashMap.put("FlightNumber", String.valueOf(flightNumber));
                    hashMap.put("DepartureAirportID", departureAirportID);
                    hashMap.put("ArrivalAirportID", arrivalAirportID);

                    arrayList.add(hashMap);
                }

                Log.d(TAG, "catchData: " + arrayList);

                runOnUiThread(() -> {
                    dialog.dismiss();
                    RecyclerView recyclerView;
                    MyAdapter myAdapter;
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                    myAdapter = new MyAdapter(); // 將arrayList傳遞給Adapter
                    recyclerView.setAdapter(myAdapter);
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvFlightDate, tvFlightNumber, tvDepartureAirportID, tvArrivalAirportID;

            ViewHolder(View itemView) {
                super(itemView);
                tvFlightDate = itemView.findViewById(R.id.date);
                tvFlightNumber = itemView.findViewById(R.id.number);
                tvDepartureAirportID = itemView.findViewById(R.id.departure);
                tvArrivalAirportID = itemView.findViewById(R.id.arrival);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_data_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HashMap<String, String> item = arrayList.get(position);

            holder.tvFlightDate.setText("Flight Date: " + item.get("FlightDate"));
            holder.tvFlightNumber.setText("Flight Number: " + item.get("FlightNumber"));
            holder.tvDepartureAirportID.setText("Departure Airport: " + item.get("DepartureAirportID"));
            holder.tvArrivalAirportID.setText("Arrival Airport: " + item.get("ArrivalAirportID"));

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


}









