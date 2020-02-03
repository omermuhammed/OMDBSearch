package com.omermuhammed.omdbsearch.utils

// Retrieve the secret key from the .so file
// Note that this is not by any means 100% secure, just makes it harder
// to hack and also shows how JNI/Native C++ can be done.
object Keys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}