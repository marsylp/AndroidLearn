LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := HelloNDK1
LOCAL_SRC_FILES := com_mars_ndksimple_01_HelloNDK.c
include $(BUILD_SHARED_LIBRARY)