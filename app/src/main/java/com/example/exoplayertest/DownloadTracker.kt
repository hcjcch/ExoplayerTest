package com.example.exoplayertest

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Log
import java.io.IOException

class DownloadTracker(
    val context: Context,
    val dataSourceFactory: DataSource.Factory,
    val downloadManager: DownloadManager
) {
    private val downloads = mutableMapOf<Uri, Download>()
    private val downloadIndex = downloadManager.downloadIndex

    init {
        loadDownloads()
    }


    private fun loadDownloads() {
        try {
            downloadIndex.getDownloads().use { loadedDownloads ->
                while (loadedDownloads.moveToNext()) {
                    val download = loadedDownloads.download
                    downloads[download.request.uri] = download
                }
            }
        } catch (e: IOException) {
            Log.w("huangchen", "Failed to query downloads", e)
        }
    }
}