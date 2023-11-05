# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

#PROJECT(wavlib C CXX)

#message("CMAKE_CURRENT_LIST_DIR = " ${CMAKE_CURRENT_LIST_DIR})

#message("HOME is " ${HOME})

# SET(NDK "")
#message("NDK is " ${NDK})

# compiler flags
# -mhard-float -D_NDK_MATH_NO_SOFTFP=1
#SET( CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -mhard-float -D_NDK_MATH_NO_SOFTFP=1" )

# Find the Oboe package
find_package(oboe REQUIRED CONFIG)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        resampler

        # Sets the library as a static library.
        STATIC

        # Provides a relative path to your source file(s).
        # stream
        ${CMAKE_CURRENT_LIST_DIR}/resampler/IntegerRatio.cpp
        ${CMAKE_CURRENT_LIST_DIR}/resampler/LinearResampler.cpp
        ${CMAKE_CURRENT_LIST_DIR}/resampler/MultiChannelResampler.cpp
        ${CMAKE_CURRENT_LIST_DIR}/resampler/PolyphaseResampler.cpp
        ${CMAKE_CURRENT_LIST_DIR}/resampler/PolyphaseResamplerMono.cpp
        ${CMAKE_CURRENT_LIST_DIR}/resampler/PolyphaseResamplerStereo.cpp
        ${CMAKE_CURRENT_LIST_DIR}/resampler/SincResampler.cpp
        ${CMAKE_CURRENT_LIST_DIR}/resampler/SincResamplerStereo.cpp
        )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        resampler

        oboe::oboe
        # Links the target library to the log library
        # included in the NDK.
        log)

