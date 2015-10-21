package com.example.android.sunshine.helpers;

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
    private JSONObject weatherData;
    private boolean isMetric;

    public ParseWeatherData(JSONObject weatherData, boolean isMetric){
        this.weatherData = weatherData;
        this.isMetric = isMetric;
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
                double max = day.getJSONObject("temp").getDouble("max");
                double min = day.getJSONObject("temp").getDouble("min");
                if(!isMetric){
                    max = covertMetricToImperialTemperature(max);
                    min = covertMetricToImperialTemperature(min);
                }

                data.setMax(max);
                data.setMin(min);
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

    private double covertMetricToImperialTemperature(double metricValue){
        return  metricValue * 9/5.0 + 32;
    }
}
