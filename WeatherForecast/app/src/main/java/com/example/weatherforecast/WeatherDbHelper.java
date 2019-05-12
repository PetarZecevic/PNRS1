package com.example.weatherforecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private SQLiteDatabase mDb = null;

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

    public WeatherData readWeatherData(String day, String cityName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_NAME, null,
                    COLUMN_DAY + "=? AND " + COLUMN_CITY_NAME + "=?",
                    new String[] {day ,cityName}, null, null, null);
        cursor.moveToFirst();
        WeatherData wdata;
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
