/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_weatherforecast_ConversionNDK */

#ifndef _Included_com_example_weatherforecast_ConversionNDK
#define _Included_com_example_weatherforecast_ConversionNDK
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_weatherforecast_ConversionNDK
 * Method:    changeTempScale
 * Signature: (DZ)D
 */
JNIEXPORT jdouble JNICALL Java_com_example_weatherforecast_ConversionNDK_changeTempScale
  (JNIEnv *, jobject, jdouble, jboolean);

/*
 * Class:     com_example_weatherforecast_ConversionNDK
 * Method:    degreeToCardinal
 * Signature: (D)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_weatherforecast_ConversionNDK_degreeToCardinal
  (JNIEnv *, jobject, jdouble);

#ifdef __cplusplus
}
#endif
#endif
