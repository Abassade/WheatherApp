package com.example.user.wheatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    TextView main, des, city;
    Button button;
    EditText editText;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.cityname);
        button = findViewById(R.id.process);
        main = findViewById(R.id.main);
        des = findViewById(R.id.des);
        city = findViewById(R.id.city);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findWeather();
            }
        });

    }


    public void findWeather()

    {
        cityName =editText.getText().toString();

        try {

            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName +"&appid=fdfcba1097622977925d52619997f5fb");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find city", Toast.LENGTH_LONG);

        }

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("weathercontent", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weatherrrrrrrrrrrrr", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for (int i=0; i<jsonArray.length(); i++){

                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    String mainme = jsonPart.getString("main");
                    String desme = jsonPart.getString("description");

                    Log.i("main", mainme);
                    Log.i("description", desme);

                    main.setText(mainme);
                    des.setText(desme);
                    city.setText(cityName);

                }

        } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}