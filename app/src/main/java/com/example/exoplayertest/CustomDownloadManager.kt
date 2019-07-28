package com.example.exoplayertest

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.*
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import java.io.File
import java.io.IOException

object CustomDownloadManager {
    private const val DOWNLOAD_ACTION_FILE = "actions"
    private const val DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions"
    lateinit var context: Application

    private val userAgent by lazy {
        Util.getUserAgent(context, "ExoPlayerTest")
    }

    private val exoDatabaseProvider by lazy {
        ExoDatabaseProvider(context)
    }

    private val downloadDirectory by lazy {
        val tmp = context.getExternalFilesDir(null);
        tmp ?: context.filesDir
    }

    private val downloadCache by lazy {
        SimpleCache(downloadDirectory, NoOpCacheEvictor(), exoDatabaseProvider)
    }

    private val downloadManager by lazy {
        val downloadIndex = DefaultDownloadIndex(exoDatabaseProvider)
        upgradeActionFile(
            DOWNLOAD_ACTION_FILE, downloadIndex, /* addNewDownloadsAsCompleted= */ false
        )
        upgradeActionFile(
            DOWNLOAD_TRACKER_ACTION_FILE, downloadIndex, /* addNewDownloadsAsCompleted= */ true
        )
        val downloaderConstructorHelper = DownloaderConstructorHelper(downloadCache, buildHttpDataSourceFactory())
        DownloadManager(context, downloadIndex, DefaultDownloaderFactory(downloaderConstructorHelper))
    }

    init {
        downloadManager
    }

    fun buildHttpDataSourceFactory(): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent)
    }

    private fun upgradeActionFile(
        fileName: String, downloadIndex: DefaultDownloadIndex, addNewDownloadsAsCompleted: Boolean
    ) {
        try {
            ActionFileUpgradeUtil.upgradeAndDelete(
                File(downloadDirectory, fileName),
                /* downloadIdProvider= */ null,
                downloadIndex,
                /* deleteOnFailure= */ true,
                addNewDownloadsAsCompleted
            )
        } catch (e: IOException) {
            Log.e("huangchen", "Failed to upgrade action file: $fileName", e)
        }

    }
}