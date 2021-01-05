package com.smzh.piano;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

public class MagicPlayer {

    private AudioTrack audioTrack;

    public void start(int sampleRate) {
        if (Build.VERSION.SDK_INT < 23) {
            audioTrack = createAudioTrackBelow23(sampleRate);
        } else {
            try {
                audioTrack = createAudioTrackAbove23(sampleRate);
            } catch (Exception e) {
                audioTrack = createAudioTrackBelow23(sampleRate);
            }
        }
        audioTrack.setPlaybackRate(sampleRate);
        audioTrack.play();
    }

    public void playData(@NotNull byte[] data, int size) {
        if (audioTrack != null && size > 0) {
            audioTrack.write(data, 0, size);
        }
    }

    public void playData(@NotNull short[] data, int size) {
        if (audioTrack != null && size > 0) {
            audioTrack.write(data, 0, size);
        }
    }


    public void stop() {
        try {
            audioTrack.pause();
            audioTrack.flush();
            audioTrack.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AudioTrack createAudioTrackBelow23(int sampleRate) {
        return new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.getMinBufferSize(
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT),
                AudioTrack.MODE_STREAM);
    }

    @RequiresApi(23)
    private AudioTrack createAudioTrackAbove23(int sampleRate) {
        return new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(AudioTrack.getMinBufferSize(
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT
                ))
                .build();
    }
}
