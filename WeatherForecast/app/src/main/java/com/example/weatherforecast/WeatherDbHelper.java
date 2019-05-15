package com.example.weatherforecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WeatherDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weather";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "weather";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_DAY = "Day";
    public static final String COLUMN_CITY_NAME = "City";
    public static final String COLUMN_TEMPERATURE = "Temperature";
    public static final String COLUMN_PRESSURE = "Pressure";
    public static final String COLUMN_HUMIDITY = "Humidity";
    public static final String COLUMN_SUNRISE = "Sunrise";
    public static final String COLUMN_SUNSET = "Sunset";
    public static final String COLUMN_WIND_SPEED = "WindSpeed";
    public static final String COLUMN_WIND_DIRECTION = "WindDirection";

    public WeatherDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_DATE + " TEXT," +
                COLUMN_DAY + " TEXT," +
                COLUMN_CITY_NAME + " TEXT," +
                COLUMN_TEMPERATURE + " INTEGER," +
                COLUMN_PRESSURE + " INTEGER," +
                COLUMN_HUMIDITY + " REAL," +
                COLUMN_SUNRISE + " TEXT," +
                COLUMN_SUNSET + " TEXT," +
                COLUMN_WIND_SPEED + " REAL," +
                COLUMN_WIND_DIRECTION + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(WeatherData wdata, String cityName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, wdata.date);
        values.put(COLUMN_DAY, wdata.day);
        values.put(COLUMN_CITY_NAME, cityName);
        values.put(COLUMN_TEMPERATURE, wdata.temperature);
        values.put(COLUMN_HUMIDITY, wdata.humidity);
        values.put(COLUMN_PRESSURE, wdata.pressure);
        values.put(COLUMN_SUNRISE, wdata.sunUp);
        values.put(COLUMN_SUNSET, wdata.sunDown);
        values.put(COLUMN_WIND_SPEED, wdata.windSpeed);
        values.put(COLUMN_WIND_DIRECTION, wdata.windDirection);
        db.insert(TABLE_NAME, null, values);
        close();
    }

    /**
     * Calculates difference between two dates in days.
     * @param date1 current date
     * @param date2 previous date
     * @return number of days between two dates
     */
    private long dateDaysDifference(Date date1, Date date2){
        long diffDays = 0;
        long diffMilies = Math.abs(date1.getTime() - date2.getTime());
        diffDays = TimeUnit.DAYS.convert(diffMilies, TimeUnit.MILLISECONDS);
        return diffDays;
    }

    // Find closest data to current date.
    public WeatherData readApproximationData(String cityName)
    {
        WeatherData wdata;
        SQLiteDatabase db = getReadableDatabase();
        // If data is not found try to find closest date to current date for given city.
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date currentDate = new Date();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY_NAME + "=?",
                new String[]{cityName}, null, null, null);
        cursor.moveToFirst();

        if (cursor.isAfterLast())
        {
            // If city is not found return null.
            return null;
        }
        else
        {
            long minDifference = Long.MAX_VALUE;
            long tmpDifference = 0;
            int position = 0;
            while (!cursor.isAfterLast())
            {
                try {
                    Date databaseDate = formatter.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                    tmpDifference = dateDaysDifference(currentDate, databaseDate);
                    if (tmpDifference < minDifference) {
                        minDifference = tmpDifference;
                        position = cursor.getPosition();
                    }
                } catch (ParseException e) {
                    Log.d("WeatherDB", "Parse error: " + e.getMessage());
                }
                cursor.moveToNext();
            }
            cursor.moveToPosition(position);
            wdata = createWeatherData(cursor);
        }
        return wdata;
    }

    public WeatherData readWeatherData(String day, String cityName) {
        SQLiteDatabase db = getReadableDatabase();
        WeatherData wdata = null;
        Cursor cursor;
        cursor = db.query(TABLE_NAME, null,
                    COLUMN_DAY + "=? AND " + COLUMN_CITY_NAME + "=?",
                    new String[] {day ,cityName}, null, null, null);
        cursor.moveToFirst();
        wdata = createWeatherData(cursor);
        close();
        return wdata;
    }

    private WeatherData createWeatherData(Cursor cursor) {
        if(cursor.isAfterLast())
            return null;
        WeatherData wdata = new WeatherData();
        wdata.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        wdata.day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
        wdata.humidity = cursor.getDouble(cursor.getColumnIndex(COLUMN_HUMIDITY));
        wdata.temperature = cursor.getInt(cursor.getColumnIndex(COLUMN_TEMPERATURE));
        wdata.pressure = cursor.getInt(cursor.getColumnIndex(COLUMN_PRESSURE));
        wdata.sunUp = cursor.getString(cursor.getColumnIndex(COLUMN_SUNRISE));
        wdata.sunDown = cursor.getString(cursor.getColumnIndex(COLUMN_SUNSET));
        wdata.windSpeed = cursor.getDouble(cursor.getColumnIndex(COLUMN_WIND_SPEED));
        wdata.windDirection = cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIRECTION));
        return wdata;
    }
}
