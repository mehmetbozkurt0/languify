package com.example.proje

import android.content.Context
import android.media.MediaPlayer

object SoundManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    fun startMusic(context: Context, soundResId: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.applicationContext, soundResId)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(1.0f, 1.0f)
        }
        if (!isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
        }
    }

    fun stopMusic() {
        if (isPlaying) {
            mediaPlayer?.pause()
            isPlaying = false
        }
    }

    fun toggleMusic(context: Context, soundResId: Int) {
        if (isPlaying) {
            stopMusic()
        } else {
            startMusic(context, soundResId)
        }
    }

    fun isPlaying(): Boolean {
        return isPlaying
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }
}