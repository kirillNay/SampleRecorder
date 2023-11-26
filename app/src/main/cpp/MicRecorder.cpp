//
// Created by k.naydyuk on 05.11.2023.
//

#include "MicRecorder.h"
#include <android/log.h>

using namespace oboe;
using namespace iolib;

static const char* TAG = "SampleRecorderNative";

bool MicRecorder::initStream() {
    sample = RecordedSample();

    dataCallback = std::make_shared<RecordingDataCallback>(this);
    errorCallback = std::make_shared<RecorderErrorCallback>();

    AudioStreamBuilder builder;
    builder.setDirection(Direction::Input);
    builder.setDeviceId(0);
    builder.setPerformanceMode(oboe::PerformanceMode::None);
    builder.setFormat(oboe::AudioFormat::Float);
    builder.setChannelCount(oboe::ChannelCount::Stereo);
    builder.setInputPreset(oboe::InputPreset::Generic);
    builder.setDataCallback(dataCallback);
    builder.setErrorCallback(errorCallback);

    Result r = builder.openStream(recordingStream);

    sample.sampleRate = recordingStream->getSampleRate();
    sample.channelCount = recordingStream->getChannelCount();

    if (r != Result::OK) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "Failed open recording stream");
        return false;
    }

    return true;
}

void MicRecorder::startRecord() {
    int tryCount = 0;
    while (tryCount < 3) {
        bool wasOpenSuccessful = true;
        // Assume that apenStream() was called successfully before startStream() call.
        if (tryCount > 0) {
            usleep(20 * 1000); // Sleep between tries to give the system time to settle.
            wasOpenSuccessful = initStream(); // Try to open the stream again after the first try.
        }
        if (wasOpenSuccessful) {
            Result result = recordingStream->requestStart();
            if (result != Result::OK){
                __android_log_print(
                        ANDROID_LOG_ERROR,
                        TAG,
                        "requestStart failed. Error: %s", convertToText(result));
                recordingStream->close();
                recordingStream.reset();
            } else {
                break;
            }
        }
        tryCount++;
    }
}

void MicRecorder::stopRecord() {
    recordingStream->requestStop();
    recordingStream->close();

    __android_log_print(ANDROID_LOG_INFO, TAG, "Sample recorded from mic:"
                                               "\n\tSample rate: %d"
                                               "\n\tSample channels: %d"
                                               "\n\tSample count: %d",
                                               sample.sampleRate,
                                               sample.channelCount,
                                               sample.data.size()
    );
}

RecordedSample MicRecorder::getRecord() {
    return sample;
}

oboe::DataCallbackResult MicRecorder::RecordingDataCallback::onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames) {
    StreamState streamState = audioStream->getState();
    if (streamState != StreamState::Open && streamState != StreamState::Started) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "  streamState:%d", streamState);
    }
    if (streamState == StreamState::Disconnected) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "  streamState::Disconnected");
    }

    auto data = static_cast<float_t*>(audioData);
    for (int index = 0; index < numFrames * parent->sample.channelCount; index++) {
        parent->sample.data.push_back(data[index]);
    }

    return DataCallbackResult::Continue;
}

bool MicRecorder::RecorderErrorCallback::onError(oboe::AudioStream *audioStream, oboe::Result error) {
    __android_log_print(ANDROID_LOG_ERROR, TAG, "==== onError() error:%d", error);
    return true;
}

void MicRecorder::RecorderErrorCallback::onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) {
    __android_log_print(ANDROID_LOG_ERROR, TAG, "==== onErrorAfterClose() error:%d", error);
}