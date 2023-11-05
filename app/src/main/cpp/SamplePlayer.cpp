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
#include <fstream>
#include <iostream>

using namespace oboe;
using namespace parselib;
using namespace iolib;
using namespace std;

static const char *TAG = "SamplePlayer";
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
        auto data = static_cast<float *>(audioData);
        for (int index = 0; index < numFrames; index++) {
            parent->finalRecord.push_back(data[index]);
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

void SamplePlayer::setRecording() {
    isRecording = true;
    finalRecord.clear();
}

void writeToFile(ofstream &file, int value, int size) {
    file.write(reinterpret_cast<const char*> (&value), size);
}

void SamplePlayer::stopRecording() {
    isRecording = false;

    ofstream audioFile;
    remove("/storage/emulated/0/Music/final.wav");
    audioFile.open("/storage/emulated/0/Music/final.wav");

    //Header chunk
    audioFile << "RIFF";
    audioFile << "----";
    audioFile << "WAVE";

    // Format chunk
    audioFile << "fmt ";
    writeToFile(audioFile, 16, 4); // Size
    writeToFile(audioFile, 1, 2); // Compression code
    writeToFile(audioFile, CHANNEL_COUNT, 2); // Number of channels
    writeToFile(audioFile, sampleRate, 4); // Sample rate
    writeToFile(audioFile, sampleRate * CHANNEL_COUNT * audioStream->getBytesPerSample() / 8, 4 ); // Byte rate
    writeToFile(audioFile, CHANNEL_COUNT * audioStream->getBytesPerSample() / 8, 2); // Block align
    writeToFile(audioFile, CHANNEL_COUNT * audioStream->getBytesPerSample(), 2); // Bit depth

    //Data chunk
    audioFile << "data";
    audioFile << "----";

    int preAudioPosition = audioFile.tellp();

    auto maxAmplitude = pow(2, 16 - 1) - 1;
    for(int i = 0; i < finalRecord.size(); i++ ) {
        int intSample = static_cast<int> (finalRecord[i]);
        writeToFile(audioFile, intSample, 2);
    }
    int postAudioPosition = audioFile.tellp();

    audioFile.seekp(preAudioPosition - 4);
    writeToFile(audioFile, postAudioPosition - preAudioPosition, 4);

    audioFile.seekp(4, ios::beg);
    writeToFile(audioFile, postAudioPosition - 8, 4);

    audioFile.close();
}

