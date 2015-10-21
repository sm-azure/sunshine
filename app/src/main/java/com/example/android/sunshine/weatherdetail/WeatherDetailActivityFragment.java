package com.example.android.sunshine.weatherdetail;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.android.sunshine.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherDetailActivityFragment extends Fragment {

    private final String LOG_TAG = WeatherDetailActivity.class.getSimpleName();
    private String weatherDetail;
    private ShareActionProvider mShareActionProvider;

    public WeatherDetailActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        weatherDetail = intent.getStringExtra(Intent.EXTRA_TEXT);

        View rootView = inflater.inflate(R.layout.fragment_weather_detail, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.detailedWeather);
        textView.setText(weatherDetail);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider)shareItem.getActionProvider();
        setShareIntent();
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setShareIntent(){
        if(mShareActionProvider!= null){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, weatherDetail);
            intent.setType("text/plain");
            mShareActionProvider.setShareIntent(intent);
        }
    }

}
