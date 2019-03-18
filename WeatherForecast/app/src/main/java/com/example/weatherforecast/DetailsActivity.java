package com.example.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {
    private TextView location;
    private TextView day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get data sent from Main activity.
        String locationName = getIntent().getStringExtra(MainActivity.CITY_INDEX);
        location = findViewById(R.id.location);
        day = findViewById(R.id.day);

        location.setText((CharSequence)locationName);
        day.setText(parseDay((Calendar.DAY_OF_WEEK+1)%7));
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
}
