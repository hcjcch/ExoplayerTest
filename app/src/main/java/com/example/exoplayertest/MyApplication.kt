package com.example.exoplayertest

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CustomDownloadManager.context = this
    }
}