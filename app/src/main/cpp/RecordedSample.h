//
// Created by k.naydyuk on 05.11.2023.
//

#ifndef SAMPLERECORDER_RECORDEDSAMPLE_H
#define SAMPLERECORDER_RECORDEDSAMPLE_H

struct RecordedSample {

    std::vector<float_t> data;

    int channelCount;

    int32_t sampleRate;

};

#endif //SAMPLERECORDER_RECORDEDSAMPLE_H
