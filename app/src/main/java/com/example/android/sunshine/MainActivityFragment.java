package com.example.android.sunshine;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.sunshine.com.example.android.sunshine.helpers.ParseWeatherData;
import com.example.android.sunshine.com.example.android.sunshine.weatherdetail.WeatherDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private ArrayAdapter<String> adapter;
    private List<String> items;

    public MainActivityFragment() {
        items = new ArrayList<String>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.i(LOG_TAG, "Called Refresh!");
                return refreshData();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean refreshData() {
        FetchData fetchData = new FetchData();
        fetchData.execute("560076");
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast_textview,
                R.id.list_item_forecast_textview,
                items
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView view = (ListView) rootView.findViewById(R.id.listView);
        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), WeatherDetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

        return rootView;
    }


    class FetchData extends AsyncTask<String, Void, JSONObject> {

        private final String LOG_TAG = FetchData.class.getSimpleName();

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if(jsonObject == null){
                Log.i(LOG_TAG, "Null json object received!");
                return;
            }
            ParseWeatherData weatherData = new ParseWeatherData(jsonObject);
            weatherData.getCity();
            List<String> weather = weatherData.getWeatherDetails();

            if(weather!=null){
                //items.clear();
                //for (String dailyWeather : weather){
                //    adapter.insert(dailyWeather, adapter.getCount());
                //}
                //items.addAll(weather);
                //adapter.notifyDataSetChanged();
                adapter.clear();
                adapter.addAll(weather);
            }



            Log.i(LOG_TAG, jsonObject.toString());
            return;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("forecast")
                    .appendPath("daily")
                    .appendQueryParameter("q", params[0])
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("cnt", "7");

            Log.i(LOG_TAG, builder.build().toString());
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                URL url = new URL(builder.build().toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            JSONObject jsonResult = null;
            try{
                jsonResult = new JSONObject(forecastJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG, "JSON Parsing Exception", e);
            }

            return jsonResult;
        }
    }

}
