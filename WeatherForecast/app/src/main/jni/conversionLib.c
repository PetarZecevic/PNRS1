//
// Created by Petar on 5/29/2019.
//
#include "conversionLib.h"

JNIEXPORT jdouble JNICALL Java_com_example_weatherforecast_ConversionNDK_changeTempScale
  (JNIEnv * env , jobject obj, jdouble tempValue, jboolean fOrC)
{
      jdouble result;
      if(fOrC)
          // Fahrenheit.
          result = (tempValue * 1.8 + 32);
      else
          // Celsius.
          result = ((tempValue - 32) / 1.8);
      return result;
}

JNIEXPORT jstring JNICALL Java_com_example_weatherforecast_ConversionNDK_degreeToCardinal
  (JNIEnv * env, jobject obj, jdouble degree)
{
    jstring direction;

    if(degree >= 348.75 || degree < 11.25)
        direction = (*env)->NewStringUTF(env, "N");

    else if(degree >= 11.25 && degree < 33.75)
        direction = (*env)->NewStringUTF(env, "NNE");

    else if(degree >= 33.75 && degree < 56.25)
        direction = (*env)->NewStringUTF(env, "NE");

    else if(degree >= 56.25 && degree < 78.75)
        direction = (*env)->NewStringUTF(env, "ENE");

    else if(degree >= 78.75 && degree < 101.25)
        direction = (*env)->NewStringUTF(env, "E");

    else if(degree >= 101.25 && degree < 123.75)
        direction = (*env)->NewStringUTF(env, "ESE");

    else if(degree >= 123.75 && degree < 146.25)
        direction = (*env)->NewStringUTF(env, "SE");

    else if(degree >= 146.25 && degree < 168.75)
        direction = (*env)->NewStringUTF(env, "SSE");

    else if(degree >= 168.75 && degree < 191.25)
        direction = (*env)->NewStringUTF(env, "S");

    else if(degree >= 191.25 && degree < 213.75)
        direction = (*env)->NewStringUTF(env, "SSW");

    else if(degree >= 213.75 && degree < 236.25)
        direction = (*env)->NewStringUTF(env, "SW");

    else if(degree >= 236.25 && degree < 258.75)
        direction = (*env)->NewStringUTF(env, "WSW");

    else if(degree >= 258.75 && degree < 281.25)
        direction = (*env)->NewStringUTF(env, "W");

    else if(degree >= 281.25 && degree < 303.75)
        direction = (*env)->NewStringUTF(env, "WNW");

    else if(degree >= 303.75 && degree < 326.25)
        direction = (*env)->NewStringUTF(env, "NW");

    else
        direction = (*env)->NewStringUTF(env, "NW");

    return direction;
}