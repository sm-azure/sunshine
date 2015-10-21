package com.example.android.sunshine.helpers;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by Fishy on 10/3/2015.
 */
public class DailyWeatherData {

    private final String LOG_TAG = DailyWeatherData.class.getSimpleName();

    public DailyWeatherData(){
        formatter = new DecimalFormat("#0.0");
    }

    public String getDate() {
        return new Date(date*1000).toString();
    }

    public void setDate(String dateString) {
        Log.i(LOG_TAG, dateString);
        this.date = Long.parseLong(dateString);
    }

    private long date;
    private double min;
    private double max;
    String weather;
    private NumberFormat formatter ;

    public String getWeather() {
        return weather;

    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    private String formatDouble(double temp){
        return formatter.format(temp).toString();
    }

    @Override
    public String toString() {
        return new String(getDate().substring(0,10) + "  "+ weather+ "  "+ formatDouble(max)+"/"+ formatDouble(min));
    }
}
