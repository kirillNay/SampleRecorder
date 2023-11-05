# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

#PROJECT(wavlib C CXX)

#message("CMAKE_CURRENT_LIST_DIR = " ${CMAKE_CURRENT_LIST_DIR})

#message("HOME is " ${HOME})

# SET(NDK "")
#message("NDK is " ${NDK})

# Find the Oboe package
find_package(oboe REQUIRED CONFIG)

set (RESAMPLER_DIR ../../../../resampler)

include(${RESAMPLER_DIR}/src/main/cpp/CMakeLists.txt)

include_directories(
        ${RESAMPLER_DIR}/src/main/cpp
)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        iolib

        # Sets the library as a static library.
        STATIC

        # source
        ${CMAKE_CURRENT_LIST_DIR}/player/SampleSource.cpp
        ${CMAKE_CURRENT_LIST_DIR}/player/SampleBuffer.cpp
        ${CMAKE_CURRENT_LIST_DIR}/player/OneShotSampleSource.cpp
        ${CMAKE_CURRENT_LIST_DIR}/player/SimpleMultiPlayer.cpp)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        iolib

        resampler
        oboe::oboe
        log)
