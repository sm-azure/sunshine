package com.example.android.sunshine.com.example.android.sunshine.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fishy on 10/3/2015.
 */
public class ParseWeatherData {

    final String LOG_TAG = ParseWeatherData.class.getSimpleName();
    JSONObject weatherData;

    public ParseWeatherData(JSONObject weatherData){
        this.weatherData = weatherData;
    }

    public String getCity(){
        String cityName =  "";
        try {
            JSONObject city = weatherData.getJSONObject("city");
            cityName = city.getString("name");
            Log.i(LOG_TAG, cityName);
        }catch (JSONException e){
            Log.e(LOG_TAG, "JSON exception", e);
        }
        return cityName;
    }

    public List<String> getWeatherDetails(){
        DailyWeatherData[] dailyWeather = null;
        List<String> stringWeather = new ArrayList<String>();
        try{
            int numberOfDays = weatherData.getInt("cnt");
            dailyWeather = new DailyWeatherData[numberOfDays];

            JSONArray weather = weatherData.optJSONArray("list");
            if(weather==null)
                return stringWeather;
            for(int i=0;i< numberOfDays; i++){
                JSONObject day = weather.getJSONObject(i);
                DailyWeatherData data = new DailyWeatherData();
                data.setMax(day.getJSONObject("temp").getDouble("max"));
                data.setMin(day.getJSONObject("temp").getDouble("min"));
                data.setWeather(day.getJSONArray("weather").getJSONObject(0).getString("main"));
                data.setDate(day.getString("dt"));
                dailyWeather[i] = data;
                stringWeather.add(data.toString());
                Log.i(LOG_TAG, data.toString());
            }

        }catch (JSONException e){
            Log.e(LOG_TAG, "JSON Exception", e);
        }
        return stringWeather;
    }
}
