LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := conversionLib
LOCAL_SRC_FILES := conversionLib.c

include $(BUILD_SHARED_LIBRARY)