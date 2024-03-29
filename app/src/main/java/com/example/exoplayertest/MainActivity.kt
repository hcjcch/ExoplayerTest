package com.example.exoplayertest

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector


class MainActivity : AppCompatActivity() {

    val dataSourceFactory by lazy {
        DefaultDataSourceFactory(this, "Test")
    }

    val trackSelector = DefaultTrackSelector()
    val player: SimpleExoPlayer by lazy {
        //        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSize(100, 100))
        val exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        exoPlayerPlayerView.player = exoPlayer
        exoPlayer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val videoResource = ProgressiveMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(Uri.parse("https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_20mb.mp4"))

        Thread(Runnable {
            while (true) {
                Thread.sleep(2000)
                Handler(Looper.getMainLooper()).post {
                    play()
                }
            }
        }).start()
    }

    private fun play() {
        val audioResource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse("asset:///_chaos_0703_A0511.mp3"))
        //        val seq = ConcatenatingMediaSource(audioResource, videoResource)
        player.addListener(object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                super.onPlaybackParametersChanged(playbackParameters)
            }

            override fun onSeekProcessed() {
                super.onSeekProcessed()
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray?,
                trackSelections: TrackSelectionArray?
            ) {
                super.onTracksChanged(trackGroups, trackSelections)
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                super.onPlayerError(error)
                Log.d("huangchen", "onPlayerError  ${error?.message}")
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)
            }

            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                super.onShuffleModeEnabledChanged(shuffleModeEnabled)
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
                super.onTimelineChanged(timeline, manifest, reason)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                Log.d(
                    "huangchen",
                    "onPlayerStateChanged  playWhenReady: $playWhenReady，playbackState：$playbackState"
                )
            }
        })
        player.prepare(audioResource)
        player.playWhenReady = true
    }
}
