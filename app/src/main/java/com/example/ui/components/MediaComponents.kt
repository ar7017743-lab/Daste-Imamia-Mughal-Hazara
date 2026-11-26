package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.AudioTrackEntity
import com.example.data.YouTubeHelper
import com.example.media.AudioPlaybackState
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.IvoryWhite
import com.example.ui.theme.ObsidianBlack
import com.example.util.PlatformHelpers
import java.io.File

@Composable
fun VideoThumbnailImage(
    youtubeVideoId: String,
    fallbackDrawable: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val fallbackRes = PlatformHelpers.resolveDrawableRes(fallbackDrawable)
    if (youtubeVideoId.isNotBlank()) {
        val url = YouTubeHelper.thumbnailUrl(youtubeVideoId)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            placeholder = painterResource(fallbackRes),
            error = painterResource(fallbackRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Image(
            painter = painterResource(id = fallbackRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

@Composable
fun GalleryMediaImage(
    imageUri: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (imageUri.startsWith("drawable://")) {
        val resName = imageUri.removePrefix("drawable://")
        Image(
            painter = painterResource(id = PlatformHelpers.resolveDrawableRes(resName)),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        val data: Any = if (imageUri.startsWith("/")) File(imageUri) else imageUri
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data)
                .crossfade(true)
                .build(),
            placeholder = painterResource(PlatformHelpers.resolveDrawableRes("img_gallery_alam")),
            error = painterResource(PlatformHelpers.resolveDrawableRes("img_gallery_alam")),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

@Composable
fun MiniAudioPlayerBar(
    playbackState: AudioPlaybackState,
    onTogglePlayPause: (AudioTrackEntity) -> Unit,
    onStop: () -> Unit,
    onOpenAudioScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val track = playbackState.currentTrack ?: return
    val progress = if (playbackState.durationMs > 0) {
        (playbackState.positionMs.toFloat() / playbackState.durationMs.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Surface(
        color = ObsidianBlack,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BurnishedGold.copy(alpha = 0.65f)),
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onOpenAudioScreen() }
            .testTag("mini_audio_player")
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BurnishedGold.copy(alpha = 0.16f)),
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
                        text = track.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = IvoryWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${track.reciter} • ${PlatformHelpers.formatMillis(playbackState.positionMs)} / ${PlatformHelpers.formatMillis(playbackState.durationMs)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ChampagneGold.copy(alpha = 0.85f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(
                    onClick = { onTogglePlayPause(track) },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("mini_player_toggle")
                ) {
                    Icon(
                        imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (playbackState.isPlaying) "Pause audio" else "Play audio",
                        tint = ChampagneGold
                    )
                }
                IconButton(
                    onClick = onStop,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close audio player",
                        tint = IvoryWhite.copy(alpha = 0.7f)
                    )
                }
            }
            LinearProgressIndicator(
                progress = { progress },
                color = BurnishedGold,
                trackColor = BurnishedGold.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            )
        }
    }
}
