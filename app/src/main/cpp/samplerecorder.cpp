//
// Created by k.naydyuk on 04.11.2023.
//
#include <jni.h>
#include <string>
#include <oboe/Oboe.h>
#include "SamplePlayer.h"
#include "MicRecorder.h"

static SamplePlayer player;

static MicRecorder recorder;

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_playerInitNative(JNIEnv *env, jobject thiz) {
    player.initStream();
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_loadWavNative(JNIEnv *env, jobject thiz, jbyteArray bytearray, jint id) {
    int len = env->GetArrayLength(bytearray);

    unsigned char *buf = new unsigned char[len];
    env->GetByteArrayRegion(bytearray, 0, len, reinterpret_cast<jbyte *>(buf));

    player.loadFromSampleWab(buf, len, id);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_startStreamNative(JNIEnv* env, jobject thiz) {
    player.startStream();
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_playNative(JNIEnv *env, jobject thiz, jint id) {
    player.playSample(id);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_resumeNative(JNIEnv *env, jobject thiz, jint id) {
    player.resumeSample(id);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_pauseNative(JNIEnv *env, jobject thiz, jint id) {
    player.pauseSample(id);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_stopNative(JNIEnv *env, jobject thiz, jint id) {
    player.stopSample(id);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_releasePlayerNative(JNIEnv *env, jobject thiz) {
    player.releasePlayer();
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_setLooping(JNIEnv *env, jobject thiz, jint id, jboolean is_looping) {
    player.setIsLooping(id, is_looping);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_isPlayingNative(JNIEnv *env, jobject thiz, jint id) {
    return player.isPlaying(id);
}

extern "C"
JNIEXPORT jint JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_getDurationNative(JNIEnv *env, jobject thiz, jint id) {
    return player.getDurationSec(id);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_getProgressNative(JNIEnv *env, jobject thiz, jint id) {
    return player.getProgress(id);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_seekToNative(JNIEnv *env, jobject thiz, jint id, jfloat position) {
    player.seekTo(id, position);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_setSpeedNative(JNIEnv *env, jobject thiz, jint id, jfloat scale) {
    player.setSpeed(id, scale);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_setVolumeNative(JNIEnv *env, jobject thiz, jint id, jfloat scale) {
    player.setVolume(id, scale);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_startVoiceRecordingNative(JNIEnv *env, jobject thiz) {
    recorder.initStream();
    recorder.startRecord();
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_stopVoiceRecordingNative(JNIEnv *env, jobject thiz, jint id) {
    recorder.stopRecord();
    auto result = recorder.getRecord();
    player.loadFromRecorded(result, id);
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_startRecordingNative(JNIEnv *env, jobject thiz) {
    player.setRecording();
}

extern "C"
JNIEXPORT void JNICALL
Java_nay_kirill_samplerecorder_player_PlayerImpl_stopRecordingNative(JNIEnv *env, jobject thiz) {
    player.stopRecording();
}