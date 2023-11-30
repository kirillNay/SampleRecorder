//
// Created by k.naydyuk on 26.11.2023.
//

#ifndef SAMPLERECORDER_FILESAVER_H
#define SAMPLERECORDER_FILESAVER_H

#include <fstream>
#include <iostream>

class FileSaver {
public:
    std::string saveWav(
            std::vector<float_t> data,
            int channelCount,
            int sampleRate,
            int bitRate,
            std::string fileDirectory
    );
private:
    void writeToFile(std::ofstream &file, int value, int size);

    std::string getFileName();
};

#endif //SAMPLERECORDER_FILESAVER_H