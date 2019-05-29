package com.example.weatherforecast;

public class ConversionNDK {
    // Load native library
    static{
        System.loadLibrary("conversionLib");
    }
    // Native methods.
    public native double changeTempScale(double tempValue, boolean FC);
    public native String degreeToCardinal(double degree);
}
