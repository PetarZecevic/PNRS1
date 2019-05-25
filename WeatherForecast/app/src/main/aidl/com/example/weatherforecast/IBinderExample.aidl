// IBinderExample.aidl
package com.example.weatherforecast;
import com.example.weatherforecast.ICallbackExample;

// Declare any non-default types here with import statements

interface IBinderExample {
    void setCallback(in ICallbackExample callback);
}
