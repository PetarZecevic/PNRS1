package com.example.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mLocation, mDay;
    private Button mTemperatureButton, mSunriseButton, mWindButton;
    private LinearLayout mTemperatureDisplay, mWindDisplay, mSunriseDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get data sent from Main activity.
        String locationName = getIntent().getStringExtra(MainActivity.CITY_INDEX);
        mLocation = findViewById(R.id.location);
        mDay = findViewById(R.id.day);

        mLocation.setText((CharSequence)locationName);
        mDay.setText(parseDay((Calendar.DAY_OF_WEEK+1)%7));
        // Set displays.
        mTemperatureDisplay = findViewById(R.id.temp_display);
        mWindDisplay = findViewById(R.id.wind_display);
        mSunriseDisplay = findViewById(R.id.sunrise_display);

        mTemperatureDisplay.setVisibility(View.INVISIBLE);
        mWindDisplay.setVisibility(View.INVISIBLE);
        mSunriseDisplay.setVisibility(View.INVISIBLE);

        // Set buttons.
        mTemperatureButton = findViewById(R.id.temp_button);
        mSunriseButton = findViewById(R.id.sunrise_button);
        mWindButton = findViewById(R.id.wind_button);

        mTemperatureButton.setOnClickListener(this);
        mSunriseButton.setOnClickListener(this);
        mWindButton.setOnClickListener(this);
    }

    private CharSequence parseDay(int d){
        CharSequence day;
        switch(d){
            case Calendar.MONDAY:
                day = "Ponedeljak";
                break;
            case Calendar.TUESDAY:
                day = "Utorak";
                break;
            case Calendar.WEDNESDAY:
                day = "Sreda";
                break;
            case Calendar.THURSDAY:
                day = "Cetvrtak";
                break;
            case Calendar.FRIDAY:
                day = "Petak";
                break;
            case Calendar.SATURDAY:
                day = "Subota";
                break;
            case Calendar.SUNDAY:
                day = "Nedelja";
                break;
            default:
                day = "NONE";
                break;
        }
        return day;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.temp_button:
                displayTemperatureScreen();
                break;
            case R.id.sunrise_button:
                displaySunriseScreen();
                break;
            case R.id.wind_button:
                displayWindScreen();
                break;
            default:
                break;
        }
    }

    private void displayTemperatureScreen()
    {
        mTemperatureDisplay.setVisibility(View.VISIBLE);
        mWindDisplay.setVisibility(View.INVISIBLE);
        mSunriseDisplay.setVisibility(View.INVISIBLE);
    }

    private void displayWindScreen()
    {
        mTemperatureDisplay.setVisibility(View.INVISIBLE);
        mWindDisplay.setVisibility(View.VISIBLE);
        mSunriseDisplay.setVisibility(View.INVISIBLE);
    }

    private void displaySunriseScreen()
    {
        mTemperatureDisplay.setVisibility(View.INVISIBLE);
        mWindDisplay.setVisibility(View.INVISIBLE);
        mSunriseDisplay.setVisibility(View.VISIBLE);
    }
}
