plugins {
    id("com.android.library")
}

android {
    namespace = "nay.kirill.resampler"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

        externalNativeBuild {
            cmake {
                cppFlags("")
                arguments("-DANDROID_STL=c++_shared")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isJniDebuggable = true
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
    buildFeatures {
        prefab = true
    }
    ndkVersion = "26.1.10909125"
}

dependencies {
    implementation("com.google.oboe:oboe:1.8.0")
}