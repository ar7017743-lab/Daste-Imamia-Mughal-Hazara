package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AudioTrackEntity
import com.example.media.AudioPlaybackState
import com.example.media.AudioPlayerController
import com.example.ui.components.SectionHeader
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IvoryWhite
import com.example.ui.theme.NastaliqUrduFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.util.PlatformHelpers

@Composable
fun AudioScreen(
    tracks: List<AudioTrackEntity>,
    playbackState: AudioPlaybackState,
    audioController: AudioPlayerController,
    onTogglePlay: (AudioTrackEntity) -> Unit,
    onSeek: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var pendingDownloadTrack by remember { mutableStateOf<AudioTrackEntity?>(null) }

    val downloadLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("audio/mpeg")
    ) { destUri ->
        val track = pendingDownloadTrack
        if (destUri != null && track != null) {
            val ok = PlatformHelpers.exportAudioTrackToUri(context, track, destUri, audioController)
            Toast.makeText(
                context,
                if (ok) "Saved ${track.title}" else "Unable to save audio file",
                Toast.LENGTH_SHORT
            ).show()
        }
        pendingDownloadTrack = null
    }

    val categories = listOf("All", "Muharram", "Safar", "Ayyam-e-Fatima")
    val years = remember(tracks) { tracks.map { it.year }.distinct().sortedDescending() }

    val filtered = remember(tracks, selectedCategory, selectedYear) {
        tracks.filter { track ->
            val matchesCat = selectedCategory == "All" || track.category.equals(selectedCategory, ignoreCase = true)
            val matchesYear = selectedYear == null || track.year == selectedYear
            matchesCat && matchesYear
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionHeader(
                    title = "Audio / MP3 Noha Collection",
                    urduTitle = "آڈیو ایم پی تھری نوحے"
                )

                // Active Player Card
                val activeTrack = playbackState.currentTrack
                if (activeTrack != null) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = ObsidianBlack,
                        border = BorderStroke(1.5.dp, BurnishedGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .testTag("active_audio_player_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(BurnishedGold.copy(alpha = 0.18f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = ChampagneGold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = activeTrack.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = IvoryWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = activeTrack.reciter,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = ChampagneGold
                                    )
                                }
                                IconButton(
                                    onClick = { onTogglePlay(activeTrack) },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(BurnishedGold)
                                ) {
                                    Icon(
                                        imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                                        tint = ObsidianBlack
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            val maxDur = playbackState.durationMs.coerceAtLeast(1).toFloat()
                            Slider(
                                value = playbackState.positionMs.toFloat().coerceIn(0f, maxDur),
                                onValueChange = { onSeek(it.toInt()) },
                                valueRange = 0f..maxDur,
                                colors = SliderDefaults.colors(
                                    thumbColor = ChampagneGold,
                                    activeTrackColor = BurnishedGold
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = PlatformHelpers.formatMillis(playbackState.positionMs),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChampagneGold
                                )
                                Text(
                                    text = PlatformHelpers.formatMillis(playbackState.durationMs),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ChampagneGold
                                )
                            }
                            playbackState.errorMessage?.let { err ->
                                Text(
                                    text = err,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Filters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BurnishedGold,
                                selectedLabelColor = ObsidianBlack
                            )
                        )
                    }
                }
                if (years.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedYear == null,
                            onClick = { selectedYear = null },
                            label = { Text("All Years") }
                        )
                        years.forEach { yr ->
                            FilterChip(
                                selected = selectedYear == yr,
                                onClick = { selectedYear = yr },
                                label = { Text(yr.toString()) }
                            )
                        }
                    }
                }
            }

            items(filtered, key = { it.id }) { track ->
                val isCurrent = playbackState.currentTrack?.id == track.id
                val isPlaying = isCurrent && playbackState.isPlaying

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isCurrent) BurnishedGold else MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("audio_track_${track.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onTogglePlay(track) },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BurnishedGold)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause ${track.title}" else "Play ${track.title}",
                                tint = ObsidianBlack
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            if (track.titleUrdu.isNotBlank()) {
                                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                    Text(
                                        text = track.titleUrdu,
                                        style = TextStyle(
                                            fontFamily = NastaliqUrduFontFamily,
                                            fontSize = 16.sp,
                                            lineHeight = 32.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Text(
                                text = track.title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${track.reciter} • ${track.category} • ${track.year} • ${PlatformHelpers.formatDuration(track.durationSeconds)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (track.downloadable) {
                            IconButton(
                                onClick = {
                                    if (track.audioUri.startsWith("http://") || track.audioUri.startsWith("https://")) {
                                        PlatformHelpers.openExternalUrl(context, track.audioUri)
                                    } else {
                                        pendingDownloadTrack = track
                                        val safeName = track.title.replace(Regex("[^A-Za-z0-9_-]"), "_")
                                        downloadLauncher.launch("${safeName}.mp3")
                                    }
                                },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("download_audio_${track.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download MP3",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
