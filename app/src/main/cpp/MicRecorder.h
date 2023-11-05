//
// Created by k.naydyuk on 05.11.2023.
//

#ifndef SAMPLERECORDER_MICRECORDER_H
#define SAMPLERECORDER_MICRECORDER_H

#include "SamplePlayer.h"
#include "RecordedSample.h"

class MicRecorder {
public:
    bool initStream();

    void startRecord();

    void stopRecord();

    RecordedSample getRecord();

private:

    class RecordingDataCallback : public oboe::AudioStreamDataCallback {
    public:
        RecordingDataCallback(MicRecorder *parent) : parent(parent) {}

        oboe::DataCallbackResult onAudioReady(
                oboe::AudioStream *audioStream,
                void *audioData,
                int32_t numFrames
        ) override;

    private:
        MicRecorder *parent;
    };

    class RecorderErrorCallback : public oboe::AudioStreamErrorCallback {
    public:
        bool onError(oboe::AudioStream* audioStream, oboe::Result error ) override;

        void onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) override;

    };

    RecordedSample sample;

    std::shared_ptr<oboe::AudioStream> recordingStream;

    std::shared_ptr<RecordingDataCallback> dataCallback;
    std::shared_ptr<RecorderErrorCallback> errorCallback;

};

#endif //SAMPLERECORDER_MICRECORDER_H
