//
// Created by k.naydyuk on 04.11.2023.
//

#ifndef SAMPLERECORDER_SAMPLEPLAYER_H
#define SAMPLERECORDER_SAMPLEPLAYER_H

#include <vector>

#include <oboe/Oboe.h>
#include "player/SampleBuffer.h"
#include "player/OneShotSampleSource.h"
#include "MicRecorder.h"
#include "RecordedSample.h"
#include "FileSaver.h"

struct Sample {

    Sample(iolib::SampleBuffer *pBuffer, iolib::OneShotSampleSource *pSource, int id) : buffer(pBuffer), source(pSource), id(id) {}

    iolib::SampleBuffer* buffer;
    iolib::OneShotSampleSource* source;
    int id;

};

class SamplePlayer {

public:
    bool initStream();

    void loadFromSampleWab(unsigned char *buff, int length, int id);

    void loadFromRecorded(const RecordedSample& sample, int id);

    void startStream();

    void stopStream();

    void playSample(int id);

    void stopSample(int id);

    void releasePlayer();

    void setIsLooping(int id, bool isLooping);

    void pauseSample(int id);

    void resumeSample(int id);

    bool isPlaying(int id);

    int getDurationSec(int id);

    float getProgress(int id);

    void seekTo(int id, float position);

    void setSpeed(int id, float scale);

    void setVolume(int id, float scale);

    void setRecording();

    std::string stopRecording(std::string directory);

    void clearFinalRecording();

    float getFinalrecordDuration();

    void createFinalRecordSample(int id);

    std::vector<float_t> getFinalRecord();

private:
    std::map<int, Sample*> samplesMap;

    class DataCallback : public oboe::AudioStreamDataCallback {
    public:
        DataCallback(SamplePlayer *parent) : parent(parent) {}

        oboe::DataCallbackResult onAudioReady(
                oboe::AudioStream *audioStream,
                void *audioData,
                int32_t numFrames
        ) override;

    private:
        SamplePlayer *parent;
    };

    class ErrorCallback : public oboe::AudioStreamErrorCallback {
    public:
        ErrorCallback(SamplePlayer *parent) : parent(parent) {}

        virtual ~ErrorCallback() {}

        void onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) override;
    private:
        SamplePlayer *parent;
    };

    std::shared_ptr<DataCallback> dataCallback;
    std::shared_ptr<ErrorCallback> errorCallback;

    // Oboe Audio Stream
    std::shared_ptr<oboe::AudioStream> audioStream;

    int sampleRate;
    int bitDepth;

    bool isRecording;
    std::vector<float_t> finalRecord;

    FileSaver *fileSaver = new FileSaver();

};

#endif //SAMPLERECORDER_SAMPLEPLAYER_H
