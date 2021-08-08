#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>

extern "C" JNIEXPORT jstring JNICALL
Java_com_hankyo_jeong_lachapelle_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hankyo_jeong_lachapelle_MainActivity_detectEdgeJNI(JNIEnv *env, jobject thiz,
                                                            jlong input_image, jlong output_image,
                                                            jint th1, jint th2) {
    cv::Mat &inputMat = *(cv::Mat *)input_image;
}