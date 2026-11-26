package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NohaLyricsEntity
import com.example.data.NohaVideoEntity
import com.example.ui.components.SectionHeader
import com.example.ui.components.VideoThumbnailImage
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.NastaliqUrduFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.util.PlatformHelpers

@Composable
fun VideosArchiveScreen(
    videos: List<NohaVideoEntity>,
    onOpenVideoDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedYear by remember { mutableStateOf<Int?>(null) }

    val categories = listOf("All", "Muharram", "Safar", "Ayyam-e-Fatima", "Majalis")
    val years = remember(videos) { videos.map { it.year }.distinct().sortedDescending() }

    val filtered = remember(videos, searchQuery, selectedCategory, selectedYear) {
        videos.filter { video ->
            val matchesQuery = searchQuery.isBlank() ||
                video.title.contains(searchQuery, ignoreCase = true) ||
                video.titleUrdu.contains(searchQuery, ignoreCase = true) ||
                video.reciter.contains(searchQuery, ignoreCase = true) ||
                video.poet.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All" || video.category.equals(selectedCategory, ignoreCase = true)
            val matchesYear = selectedYear == null || video.year == selectedYear
            matchesQuery && matchesCategory && matchesYear
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
                    title = "Latest Noha & Video Archive",
                    urduTitle = "نوحہ ویڈیو آرکائیو"
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by title, Urdu title, or reciter...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("video_search_input")
                )

                Spacer(modifier = Modifier.height(8.dp))
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

            if (filtered.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "No Noha videos match your search or filter. Add new videos in Admin CMS.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            } else {
                items(filtered, key = { it.id }) { video ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { onOpenVideoDetail(video.id) }
                            .testTag("video_card_${video.id}")
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                            ) {
                                VideoThumbnailImage(
                                    youtubeVideoId = video.youtubeVideoId,
                                    fallbackDrawable = video.fallbackDrawable,
                                    contentDescription = video.title,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .align(Alignment.Center)
                                        .clip(CircleShape)
                                        .background(ObsidianBlack.copy(alpha = 0.78f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Open video details",
                                        tint = ChampagneGold,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ObsidianBlack.copy(alpha = 0.85f),
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "${video.category} • ${video.year}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ChampagneGold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(14.dp)) {
                                if (video.titleUrdu.isNotBlank()) {
                                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                        Text(
                                            text = video.titleUrdu,
                                            style = TextStyle(
                                                fontFamily = NastaliqUrduFontFamily,
                                                fontSize = 19.sp,
                                                lineHeight = 36.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                Text(
                                    text = video.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Reciter: ${video.reciter} • Poet: ${video.poet}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoDetailScreen(
    video: NohaVideoEntity?,
    relatedLyrics: NohaLyricsEntity?,
    onBack: () -> Unit,
    onOpenLyrics: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (video == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Video not found")
        }
        return
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Noha Video Details", style = MaterialTheme.typography.titleLarge)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .aspectRatio(16f / 9f)
                    .clickable { PlatformHelpers.openExternalUrl(context, video.youtubeUrl) }
            ) {
                VideoThumbnailImage(
                    youtubeVideoId = video.youtubeVideoId,
                    fallbackDrawable = video.fallbackDrawable,
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(ObsidianBlack.copy(alpha = 0.82f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Watch on YouTube",
                        tint = ChampagneGold,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${video.category} • ${video.year}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (video.titleUrdu.isNotBlank()) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = video.titleUrdu,
                            style = TextStyle(
                                fontFamily = NastaliqUrduFontFamily,
                                fontSize = 24.sp,
                                lineHeight = 46.sp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Text(text = video.title, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reciter: ${video.reciter}\nPoet: ${video.poet}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(18.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { PlatformHelpers.openExternalUrl(context, video.youtubeUrl) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BurnishedGold,
                            contentColor = ObsidianBlack
                        ),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Watch on YouTube", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            PlatformHelpers.shareText(
                                context,
                                video.title,
                                "${video.title} — ${video.reciter}\nWatch: ${video.youtubeUrl}"
                            )
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share")
                    }
                }

                if (relatedLyrics != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = { onOpenLyrics(relatedLyrics.id) },
                        border = BorderStroke(1.dp, BurnishedGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                    ) {
                        Text("Read Noha Lyrics: ${relatedLyrics.title}")
                    }
                }
            }
        }
    }
}
