//
// Created by k.naydyuk on 26.11.2023.
//

#include "FileSaver.h"
#include <fstream>
#include <iostream>
#include <android/log.h>

using namespace std;

static const char *TAG = "SampleRecorderNative";

string FileSaver::saveWav(std::vector<float_t> data, int channelCount, int sampleRate, int bitDepth, string fileDirectory) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Saving wav file..."
                                               "\nFile info:"
                                               "\n\tsamples: %d"
                                               "\n\tduration: %f"
                                               "\n\tchannels: %d"
                                               "\n\tsample rate: %d"
                                               "\n\tbitDepth: %d",
                                               data.size(),
                                               data.size() / (float) sampleRate,
                                               channelCount,
                                               sampleRate,
                                               bitDepth);

    ofstream audioFile;
    string fileOriginalName = getFileName();
    string fileName = fileDirectory + fileOriginalName + ".wav";
    audioFile.open(fileName, ios::binary);

    //Header chunk
    audioFile << "RIFF";
    audioFile << "----";
    audioFile << "WAVE";

    // Format chunk
    audioFile << "fmt ";
    writeToFile(audioFile, 16, 4); // Size
    writeToFile(audioFile, 1, 2); // Compression code
    writeToFile(audioFile, channelCount, 2); // Number of channels
    writeToFile(audioFile, sampleRate, 4); // Sample rate
    writeToFile(audioFile, sampleRate * channelCount * bitDepth / 8, 4 ); // Byte rate
    writeToFile(audioFile, channelCount * bitDepth / 8, 2); // Block align
    writeToFile(audioFile, bitDepth, 2); // Bit depth

    //Data chunk
    audioFile << "data";
    audioFile << "----";

    int preAudioPosition = audioFile.tellp();

    auto maxAmplitude = pow(2, bitDepth - 1) - 1;
    for(int i = 0; i < data.size(); i++ ) {
        int intSample = static_cast<int32_t> (data[i] * pow(2, bitDepth - 1) - 1);
        writeToFile(audioFile, intSample, bitDepth / 8);
    }
    int postAudioPosition = audioFile.tellp();

    audioFile.seekp(preAudioPosition - 4);
    writeToFile(audioFile, postAudioPosition - preAudioPosition, 4);

    audioFile.seekp(4, ios::beg);
    writeToFile(audioFile, postAudioPosition - 8, 4);

    audioFile.close();

    string output = "File Saved to " + fileName + "!";
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", output.c_str());

    return fileOriginalName;
}

void FileSaver::writeToFile(ofstream &file, int value, int size) {
    file.write(reinterpret_cast<const char*> (&value), size);
}

string FileSaver::getFileName() {
    time_t now = time(0);
    const char* date_time = strtok(ctime(&now), "\n");
    string time(date_time);
    replace(time.begin(), time.end(), ' ', '_');
    replace(time.begin(), time.end(), ':', '-');
    return time;
}