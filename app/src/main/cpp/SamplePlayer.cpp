//
// Created by k.naydyuk on 04.11.2023.
//
#include "SamplePlayer.h"
#include "wav/WavStreamReader.h"
#include "stream/MemInputStream.h"
#include "player/SampleBuffer.h"
#include "player/OneShotSampleSource.h"

#include <oboe/oboe.h>
#include <android/log.h>

using namespace oboe;
using namespace parselib;
using namespace iolib;

static const char* TAG = "SamplePlayer";
static const int CHANNEL_COUNT = 2;

bool SamplePlayer::initStream() {

    dataCallback = std::make_shared<DataCallback>(this);
    errorCallback = std::make_shared<ErrorCallback>(this);

    // Create an audio stream
    AudioStreamBuilder builder;
    builder.setChannelCount(CHANNEL_COUNT);
    // we will resample source data to device rate, so take default sample rate
    builder.setDataCallback(dataCallback);
    builder.setErrorCallback(errorCallback);
    builder.setPerformanceMode(PerformanceMode::LowLatency);
    builder.setSharingMode(SharingMode::Exclusive);
    builder.setSampleRateConversionQuality(SampleRateConversionQuality::Medium);

    Result result = builder.openStream(audioStream);
    if (result != Result::OK) {
        __android_log_print(
                ANDROID_LOG_ERROR,
                TAG,
                "initStream failed. Error: %s", convertToText(result));
        return false;
    }

    sampleRate = audioStream->getSampleRate();

    return true;
}

void SamplePlayer::loadFromSampleWab(unsigned char *buff, int length, int id) {
    __android_log_print(ANDROID_LOG_ERROR, TAG, "Start loading sample with id %d", id);

    MemInputStream stream(buff, length);

    WavStreamReader reader(&stream);
    reader.parse();

    reader.getNumChannels();

    SampleBuffer* sampleBuffer = new SampleBuffer();
    sampleBuffer->loadSampleData(&reader);

    OneShotSampleSource* source = new OneShotSampleSource(sampleBuffer, 1.0);

    sampleBuffer->resampleData(sampleRate);
    auto sample = new Sample(sampleBuffer, source, id);

    samplesMap[id] = sample;
}

void SamplePlayer::loadFromRecorded(RecordedSample sample, int id) {
    SampleBuffer* sampleBuffer = new SampleBuffer();
    sampleBuffer->loadSampleData(sample.data, sample.channelCount, sample.sampleRate);

    OneShotSampleSource* source = new OneShotSampleSource(sampleBuffer, 1);
    source->setGain(1);

    sampleBuffer->resampleData(sampleRate);
    samplesMap[id] = new Sample(sampleBuffer, source, id);
}

void SamplePlayer::startStream() {
    int tryCount = 0;
    while (tryCount < 3) {
        bool wasOpenSuccessful = true;
        // Assume that apenStream() was called successfully before startStream() call.
        if (tryCount > 0) {
            usleep(20 * 1000); // Sleep between tries to give the system time to settle.
            wasOpenSuccessful = initStream(); // Try to open the stream again after the first try.
        }
        if (wasOpenSuccessful) {
            Result result = audioStream->requestStart();
            if (result != Result::OK){
                __android_log_print(
                        ANDROID_LOG_ERROR,
                        TAG,
                        "requestStart failed. Error: %s", convertToText(result));
                audioStream->close();
                audioStream.reset();
            } else {
                break;
            }
        }
        tryCount++;
    }
}

void SamplePlayer::stopStream() {
    audioStream->requestStop();
}

DataCallbackResult SamplePlayer::DataCallback::onAudioReady(
        AudioStream *audioStream,
        void *audioData,
        int32_t numFrames
) {
    StreamState streamState = audioStream->getState();
    if (streamState != StreamState::Open && streamState != StreamState::Started) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "  streamState:%d", streamState);
    }
    if (streamState == StreamState::Disconnected) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "  streamState::Disconnected");
    }

    memset(audioData, 0, static_cast<size_t>(numFrames) * CHANNEL_COUNT * sizeof(float));

    for (auto const& [key, val] : parent->samplesMap) {
        if (val->source->isPlaying()) {
            val->source->mixAudio((float*)audioData, CHANNEL_COUNT, numFrames);
        }
    }

    return DataCallbackResult::Continue;
}

void SamplePlayer::ErrorCallback::onErrorAfterClose(AudioStream *oboeStream, Result error) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "==== onErrorAfterClose() error:%d", error);
}

void SamplePlayer::playSample(int id) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setPlayMode();
    }
}

void SamplePlayer::resumeSample(int id) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setResumeMode();
    }
}

void SamplePlayer::setIsLooping(int id, bool isLooping) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->isLooping = isLooping;
    }
}

void SamplePlayer::pauseSample(int id) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setPauseMode();
    }
}

void SamplePlayer::stopSample(int id) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setStopMode();
    }
}

bool SamplePlayer::isPlaying(int id) {
    if (samplesMap[id] != nullptr) {
        return samplesMap[id]->source->isPlaying();
    }

    return false;
}

int SamplePlayer::getDurationSec(int id) {
    if (samplesMap[id] != nullptr) {
        return samplesMap[id]->buffer->getNumSamples() / sampleRate;
    }

    return 0;
}

float SamplePlayer::getProgress(int id) {
    if (samplesMap[id] != nullptr) {
        return samplesMap[id]->source->getProgress();
    }

    return 0;
}

void SamplePlayer::seekTo(int id, float position) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setPosition(position);
    }
}

void SamplePlayer::setSpeed(int id, float scale) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->buffer->resampleData(sampleRate * scale);
    }
}

void SamplePlayer::setVolume(int id, float scale) {
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setGain(scale);
    }
}
