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

static const char *TAG = "SampleRecorderNative";
static const int CHANNEL_COUNT = 2;

namespace little_endian_io {
    template<typename Word>
    std::ostream &write_word(std::ostream &outs, Word value, unsigned size = sizeof(Word)) {
        for (; size; --size, value >>= 8)
            outs.put(static_cast <char> (value & 0xFF));
        return outs;
    }
}
using namespace little_endian_io;

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
    builder.setSharingMode(SharingMode::Shared);
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
    bitDepth = audioStream->getBytesPerSample() * 8;

    return true;
}

void SamplePlayer::loadFromSampleWab(unsigned char *buff, int length, int id) {
    MemInputStream stream(buff, length);

    WavStreamReader reader(&stream);
    reader.parse();

    reader.getNumChannels();

    SampleBuffer *sampleBuffer = new SampleBuffer();
    sampleBuffer->loadSampleData(&reader);

    OneShotSampleSource *source = new OneShotSampleSource(sampleBuffer, 1.0);

    sampleBuffer->resampleData(sampleRate);
    auto sample = new Sample(sampleBuffer, source, id);

    samplesMap[id] = sample;
}

void SamplePlayer::loadFromRecorded(const RecordedSample& sample, int id) {
    SampleBuffer *sampleBuffer = new SampleBuffer();
    sampleBuffer->loadSampleData(sample.data, sample.channelCount, sample.sampleRate);

    OneShotSampleSource *source = new OneShotSampleSource(sampleBuffer, 1);
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
            if (result != Result::OK) {
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

    for (auto const &[key, val]: parent->samplesMap) {
        if (val->source->isPlaying()) {
            val->source->mixAudio((float *) audioData, CHANNEL_COUNT, numFrames);
        }
    }

    if (parent->isRecording) {
        for (int index = 0; index < numFrames * CHANNEL_COUNT; index++) {
            parent->finalRecord.push_back(((float *) audioData)[index]);
        }
    }

    return DataCallbackResult::Continue;
}

void SamplePlayer::ErrorCallback::onErrorAfterClose(AudioStream *oboeStream, Result error) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "==== onErrorAfterClose() error:%d", error);
}

void SamplePlayer::playSample(int id) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Play audio sample with id %d", id);

    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setPlayMode();
        __android_log_print(ANDROID_LOG_INFO, TAG, "Number of samples: %d", samplesMap[id]->source->mSampleBuffer->getNumSamples());
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Sample with id %d not found", id);
    }
}

void SamplePlayer::resumeSample(int id) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Resume audio sample with id %d", id);

    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setResumeMode();
        __android_log_print(ANDROID_LOG_INFO, TAG, "Number of samples: %d", samplesMap[id]->source->mSampleBuffer->getNumSamples());
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Sample with id %d not found", id);
    }
}

void SamplePlayer::setIsLooping(int id, bool isLooping) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Set looping = %b sample with id %d", isLooping, id);
    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->isLooping = isLooping;
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Sample with id %d not found", id);
    }
}

void SamplePlayer::pauseSample(int id) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Pause sample with id %d", id);

    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setPauseMode();
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Sample with id %d not found", id);
    }
}

void SamplePlayer::stopSample(int id) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Stop sample with id %d", id);

    if (samplesMap[id] != nullptr) {
        samplesMap[id]->source->setStopMode();
    } else {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Sample with id %d not found", id);
    }
}

void SamplePlayer::releasePlayer() {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Release player!");

    samplesMap.clear();
    audioStream->requestStop();
    audioStream->close();
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

void SamplePlayer::setRecording() {
    isRecording = true;
    finalRecord.clear();
}

void SamplePlayer::stopRecording() {
    isRecording = false;
    fileSaver->saveWav(finalRecord, CHANNEL_COUNT, sampleRate, bitDepth);
    finalRecord.clear();
}

