package com.example.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.example.data.AudioTrackEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.math.PI
import kotlin.math.sin

data class AudioPlaybackState(
    val currentTrack: AudioTrackEntity? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val positionMs: Int = 0,
    val durationMs: Int = 0,
    val errorMessage: String? = null
)

class AudioPlayerController(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressJob: Job? = null

    private val _state = MutableStateFlow(AudioPlaybackState())
    val state: StateFlow<AudioPlaybackState> = _state.asStateFlow()

    fun togglePlayPause(track: AudioTrackEntity) {
        val current = _state.value.currentTrack
        if (current?.id == track.id && mediaPlayer != null) {
            val player = mediaPlayer ?: return
            if (player.isPlaying) {
                player.pause()
                _state.update { it.copy(isPlaying = false) }
                stopProgressTicker()
            } else {
                player.start()
                _state.update { it.copy(isPlaying = true, errorMessage = null) }
                startProgressTicker()
            }
            return
        }
        playTrack(track)
    }

    fun playTrack(track: AudioTrackEntity) {
        releasePlayer()
        _state.value = AudioPlaybackState(
            currentTrack = track,
            isPlaying = false,
            isBuffering = true,
            positionMs = 0,
            durationMs = track.durationSeconds * 1000
        )

        scope.launch(Dispatchers.IO) {
            try {
                val player = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                }

                when {
                    track.audioUri.startsWith("tone://") -> {
                        val freq = track.audioUri.removePrefix("tone://").toDoubleOrNull() ?: 220.0
                        val wavFile = ensureReverentDroneWav(freq, track.id)
                        player.setDataSource(wavFile.absolutePath)
                    }
                    track.audioUri.startsWith("http://") || track.audioUri.startsWith("https://") -> {
                        player.setDataSource(track.audioUri)
                    }
                    track.audioUri.startsWith("file://") || track.audioUri.startsWith("/") -> {
                        val path = track.audioUri.removePrefix("file://")
                        player.setDataSource(path)
                    }
                    else -> {
                        player.setDataSource(context, Uri.parse(track.audioUri))
                    }
                }

                player.setOnPreparedListener { mp ->
                    mediaPlayer = mp
                    mp.start()
                    _state.update {
                        it.copy(
                            isPlaying = true,
                            isBuffering = false,
                            durationMs = mp.duration.takeIf { d -> d > 0 } ?: (track.durationSeconds * 1000),
                            errorMessage = null
                        )
                    }
                    startProgressTicker()
                }
                player.setOnCompletionListener {
                    _state.update { it.copy(isPlaying = false, positionMs = 0) }
                    stopProgressTicker()
                }
                player.setOnErrorListener { _, _, _ ->
                    _state.update {
                        it.copy(
                            isPlaying = false,
                            isBuffering = false,
                            errorMessage = "Unable to play this audio source. Verify file or URL in Admin CMS."
                        )
                    }
                    stopProgressTicker()
                    true
                }
                player.prepareAsync()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isPlaying = false,
                        isBuffering = false,
                        errorMessage = e.localizedMessage ?: "Audio playback error"
                    )
                }
            }
        }
    }

    fun seekTo(positionMs: Int) {
        val mp = mediaPlayer ?: return
        val safePos = positionMs.coerceIn(0, mp.duration.coerceAtLeast(1))
        mp.seekTo(safePos)
        _state.update { it.copy(positionMs = safePos) }
    }

    fun stop() {
        releasePlayer()
        _state.value = AudioPlaybackState()
    }

    private fun startProgressTicker() {
        stopProgressTicker()
        progressJob = scope.launch {
            while (isActive) {
                val mp = mediaPlayer
                if (mp != null && mp.isPlaying) {
                    _state.update { it.copy(positionMs = mp.currentPosition) }
                }
                delay(400L)
            }
        }
    }

    private fun stopProgressTicker() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun releasePlayer() {
        stopProgressTicker()
        mediaPlayer?.runCatching {
            if (isPlaying) stop()
            reset()
            release()
        }
        mediaPlayer = null
    }

    fun release() {
        releasePlayer()
    }

    /**
     * Generates a gentle, solemn harmonic drone WAV file in cacheDir for pre-seeded offline tracks
     * so built-in audio playback controls (Play/Pause/Seek/Download) work reliably without external network files.
     */
    fun ensureReverentDroneWav(baseFreq: Double, trackId: Long): File {
        val file = File(context.cacheDir, "noha_preview_${trackId}_${baseFreq.toInt()}.wav")
        if (file.exists() && file.length() > 44) return file

        val sampleRate = 22050
        val durationSec = 18
        val numSamples = sampleRate * durationSec
        val pcmData = ByteArray(numSamples * 2)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val envelope = when {
                t < 0.8 -> t / 0.8
                t > durationSec - 1.2 -> (durationSec - t) / 1.2
                else -> 1.0
            }.coerceIn(0.0, 1.0)

            val cadence = 0.7 + 0.3 * sin(2.0 * PI * 0.25 * t)
            val fundamental = sin(2.0 * PI * baseFreq * t)
            val fifth = 0.45 * sin(2.0 * PI * (baseFreq * 1.498) * t)
            val octave = 0.25 * sin(2.0 * PI * (baseFreq * 2.0) * t)
            val sample = ((fundamental + fifth + octave) * 0.18 * envelope * cadence * Short.MAX_VALUE)
                .toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())

            pcmData[i * 2] = (sample and 0xff).toByte()
            pcmData[i * 2 + 1] = ((sample shr 8) and 0xff).toByte()
        }

        FileOutputStream(file).use { out ->
            val byteRate = sampleRate * 2
            val totalAudioLen = pcmData.size
            val totalDataLen = totalAudioLen + 36
            val header = byteArrayOf(
                'R'.code.toByte(), 'I'.code.toByte(), 'F'.code.toByte(), 'F'.code.toByte(),
                (totalDataLen and 0xff).toByte(),
                ((totalDataLen shr 8) and 0xff).toByte(),
                ((totalDataLen shr 16) and 0xff).toByte(),
                ((totalDataLen shr 24) and 0xff).toByte(),
                'W'.code.toByte(), 'A'.code.toByte(), 'V'.code.toByte(), 'E'.code.toByte(),
                'f'.code.toByte(), 'm'.code.toByte(), 't'.code.toByte(), ' '.code.toByte(),
                16, 0, 0, 0, 1, 0, 1, 0,
                (sampleRate and 0xff).toByte(),
                ((sampleRate shr 8) and 0xff).toByte(),
                ((sampleRate shr 16) and 0xff).toByte(),
                ((sampleRate shr 24) and 0xff).toByte(),
                (byteRate and 0xff).toByte(),
                ((byteRate shr 8) and 0xff).toByte(),
                ((byteRate shr 16) and 0xff).toByte(),
                ((byteRate shr 24) and 0xff).toByte(),
                2, 0, 16, 0,
                'd'.code.toByte(), 'a'.code.toByte(), 't'.code.toByte(), 'a'.code.toByte(),
                (totalAudioLen and 0xff).toByte(),
                ((totalAudioLen shr 8) and 0xff).toByte(),
                ((totalAudioLen shr 16) and 0xff).toByte(),
                ((totalAudioLen shr 24) and 0xff).toByte()
            )
            out.write(header)
            out.write(pcmData)
        }
        return file
    }
}
