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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.example.data.AudioTrackEntity
import com.example.data.GalleryPhotoEntity
import com.example.data.IslamicArticleEntity
import com.example.data.MajlisProgramEntity
import com.example.data.NohaLyricsEntity
import com.example.data.NohaVideoEntity
import com.example.data.SiteSettingsEntity
import com.example.media.AudioPlaybackState
import com.example.ui.AppLanguage
import com.example.ui.components.GalleryMediaImage
import com.example.ui.components.IslamicGeometricDivider
import com.example.ui.components.SectionHeader
import com.example.ui.components.VideoThumbnailImage
import com.example.ui.components.WebsiteFooter
import com.example.ui.theme.BurnishedGold
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IvoryWhite
import com.example.ui.theme.LightMutedText
import com.example.ui.theme.NastaliqUrduFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.util.PlatformHelpers

@Composable
fun HomeScreen(
    settings: SiteSettingsEntity,
    language: AppLanguage,
    videos: List<NohaVideoEntity>,
    audioTracks: List<AudioTrackEntity>,
    lyrics: List<NohaLyricsEntity>,
    majalis: List<MajlisProgramEntity>,
    galleryPhotos: List<GalleryPhotoEntity>,
    articles: List<IslamicArticleEntity>,
    playbackState: AudioPlaybackState,
    onNavigate: (String) -> Unit,
    onOpenVideoDetail: (Long) -> Unit,
    onOpenLyricsDetail: (Long) -> Unit,
    onOpenArticleDetail: (Long) -> Unit,
    onToggleAudio: (AudioTrackEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val heroVideo = videos.firstOrNull { it.isHeroFeatured } ?: videos.firstOrNull()
    val featuredVideos = videos.filter { it.isFeatured }.ifEmpty { videos.take(4) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .testTag("home_scroll_list"),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 2. Navigation Menu Strip immediately below Calligraphy Header
            item {
                QuickNavigationStrip(
                    settings = settings,
                    language = language,
                    onNavigate = onNavigate
                )
            }

            // 3. Full-width Featured YouTube Video immediately below navigation
            if (heroVideo != null) {
                item {
                    HeroFeaturedVideoBanner(
                        video = heroVideo,
                        onWatchClick = { onOpenVideoDetail(heroVideo.id) },
                        onYouTubeClick = {
                            PlatformHelpers.openExternalUrl(context, heroVideo.youtubeUrl)
                        }
                    )
                }
            }

            // 4. Latest Noha Videos
            item {
                SectionHeader(
                    title = if (language == AppLanguage.URDU) "تازہ ترین نوحے" else "Latest Noha Videos",
                    urduTitle = if (language == AppLanguage.ENGLISH) "تازہ ترین نوحے" else null,
                    actionLabel = "View All",
                    onActionClick = { onNavigate("videos") }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(videos.take(6), key = { it.id }) { video ->
                        HomeVideoCard(
                            video = video,
                            onClick = { onOpenVideoDetail(video.id) }
                        )
                    }
                }
            }

            // 5. Featured Noha Collection
            item {
                SectionHeader(
                    title = "Featured Noha Collection",
                    urduTitle = "منتخب نوحہ کلیکشن",
                    actionLabel = "Archive",
                    onActionClick = { onNavigate("videos") }
                )
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    featuredVideos.take(3).forEach { video ->
                        FeaturedVideoRowCard(
                            video = video,
                            onClick = { onOpenVideoDetail(video.id) }
                        )
                    }
                }
            }

            // 6. Majalis and Religious Programs
            if (settings.navShowMajalis && majalis.isNotEmpty()) {
                item {
                    IslamicGeometricDivider(modifier = Modifier.padding(vertical = 4.dp))
                    SectionHeader(
                        title = "Majalis & Religious Programs",
                        urduTitle = "مجالسِ عزا اور دینی پروگرام",
                        actionLabel = "Schedule",
                        onActionClick = { onNavigate("majalis") }
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        majalis.take(2).forEach { majlis ->
                            HomeMajlisCard(
                                majlis = majlis,
                                onClick = { onNavigate("majalis") }
                            )
                        }
                    }
                }
            }

            // 7. MP3 Audio Collection
            if (audioTracks.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "MP3 Audio Collection",
                        urduTitle = "آڈیو نوحہ لائبریری",
                        actionLabel = "All MP3s",
                        onActionClick = { onNavigate("audio") }
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        audioTracks.take(3).forEach { track ->
                            val isCurrent = playbackState.currentTrack?.id == track.id
                            val isPlaying = isCurrent && playbackState.isPlaying
                            HomeAudioRow(
                                track = track,
                                isPlaying = isPlaying,
                                onTogglePlay = { onToggleAudio(track) },
                                onOpenAudio = { onNavigate("audio") }
                            )
                        }
                    }
                }
            }

            // 8. Noha Lyrics
            if (lyrics.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Noha Lyrics",
                        urduTitle = "نوحہ کلام و شاعری",
                        actionLabel = "All Lyrics",
                        onActionClick = { onNavigate("lyrics") }
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(lyrics.take(5), key = { it.id }) { item ->
                            HomeLyricsCard(
                                item = item,
                                onClick = { onOpenLyricsDetail(item.id) }
                            )
                        }
                    }
                }
            }

            // 9. Islamic Articles
            if (settings.navShowArticles && articles.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Islamic Articles",
                        urduTitle = "اسلامی مضامین",
                        actionLabel = "Read All",
                        onActionClick = { onNavigate("articles") }
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        articles.take(2).forEach { article ->
                            HomeArticleCard(
                                article = article,
                                onClick = { onOpenArticleDetail(article.id) }
                            )
                        }
                    }
                }
            }

            // 10. Photo Gallery
            if (settings.navShowGallery && galleryPhotos.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Photo Gallery",
                        urduTitle = "تصویری گیلری",
                        actionLabel = "Gallery",
                        onActionClick = { onNavigate("gallery") }
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(galleryPhotos.take(6), key = { it.id }) { photo ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, BurnishedGold.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .width(176.dp)
                                    .clickable { onNavigate("gallery") }
                            ) {
                                Column {
                                    GalleryMediaImage(
                                        imageUri = photo.imageUri,
                                        contentDescription = photo.altText,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(118.dp)
                                    )
                                    Text(
                                        text = photo.title,
                                        style = MaterialTheme.typography.labelLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 11. YouTube Subscription Section & 12. WhatsApp Contact Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    YouTubeSubscribeBanner(
                        settings = settings,
                        onSubscribeClick = {
                            PlatformHelpers.openExternalUrl(context, settings.youtubeChannelUrl)
                        }
                    )
                    WhatsAppContactBanner(
                        settings = settings,
                        onWhatsAppClick = {
                            PlatformHelpers.openWhatsApp(
                                context,
                                settings.whatsappNumber,
                                "Assalam-o-Alaikum, contacting Daste Imamia Mughal Hazara."
                            )
                        },
                        onContactPageClick = { onNavigate("contact") }
                    )
                }
            }

            // 13. Professional Footer
            item {
                WebsiteFooter(
                    settings = settings,
                    onNavigate = onNavigate,
                    onOpenYouTube = {
                        PlatformHelpers.openExternalUrl(context, settings.youtubeChannelUrl)
                    },
                    onOpenWhatsApp = {
                        PlatformHelpers.openWhatsApp(
                            context,
                            settings.whatsappNumber,
                            "Assalam-o-Alaikum, contacting Daste Imamia Mughal Hazara."
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun QuickNavigationStrip(
    settings: SiteSettingsEntity,
    language: AppLanguage,
    onNavigate: (String) -> Unit
) {
    val navItems = buildList {
        add(Triple("Latest Noha", "videos", Icons.Default.PlayCircle))
        add(Triple("Audio MP3", "audio", Icons.Default.LibraryMusic))
        add(Triple("Noha Lyrics", "lyrics", Icons.AutoMirrored.Filled.MenuBook))
        if (settings.navShowMajalis) add(Triple("Majalis", "majalis", Icons.Default.CalendarMonth))
        if (settings.navShowGallery) add(Triple("Gallery", "gallery", Icons.Default.PhotoLibrary))
        if (settings.navShowArticles) add(Triple("Articles", "articles", Icons.AutoMirrored.Filled.MenuBook))
        add(Triple("About Us", "about", Icons.Default.Info))
        add(Triple("Contact", "contact", Icons.Default.Chat))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        navItems.forEach { (label, route, icon) ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .clickable { onNavigate(route) }
                    .testTag("quick_nav_$route")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroFeaturedVideoBanner(
    video: NohaVideoEntity,
    onWatchClick: () -> Unit,
    onYouTubeClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianBlack),
        border = BorderStroke(1.5.dp, BurnishedGold.copy(alpha = 0.75f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onWatchClick() }
            .testTag("hero_featured_video")
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
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.25f),
                                    Color.Transparent,
                                    ObsidianBlack.copy(alpha = 0.92f)
                                )
                            )
                        )
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BurnishedGold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "FEATURED • ${video.category.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ObsidianBlack,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(ObsidianBlack.copy(alpha = 0.78f))
                        .clickable { onWatchClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play featured Noha",
                        tint = ChampagneGold,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                if (video.titleUrdu.isNotBlank()) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = video.titleUrdu,
                            style = TextStyle(
                                fontFamily = NastaliqUrduFontFamily,
                                fontSize = 20.sp,
                                lineHeight = 38.sp,
                                color = ChampagneGold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = IvoryWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reciter: ${video.reciter} • Poet: ${video.poet} • ${video.year}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightMutedText
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onWatchClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BurnishedGold,
                            contentColor = ObsidianBlack
                        ),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Video Details", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = onYouTubeClick,
                        border = BorderStroke(1.dp, BurnishedGold),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneGold),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text("Open YouTube")
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeVideoCard(
    video: NohaVideoEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .width(248.dp)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(138.dp)
            ) {
                VideoThumbnailImage(
                    youtubeVideoId = video.youtubeVideoId,
                    fallbackDrawable = video.fallbackDrawable,
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ObsidianBlack.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "${video.category} • ${video.year}",
                        style = MaterialTheme.typography.labelSmall,
                        color = ChampagneGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = video.reciter,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FeaturedVideoRowCard(
    video: NohaVideoEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VideoThumbnailImage(
                youtubeVideoId = video.youtubeVideoId,
                fallbackDrawable = video.fallbackDrawable,
                contentDescription = video.title,
                modifier = Modifier
                    .width(108.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${video.reciter} • ${video.poet}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${video.category} • ${video.year}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun HomeMajlisCard(
    majlis: MajlisProgramEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = majlis.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "${majlis.dateText} • ${majlis.timeText}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = majlis.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${majlis.speaker} — ${majlis.venue}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HomeAudioRow(
    track: AudioTrackEntity,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onOpenAudio: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenAudio() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onTogglePlay,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(BurnishedGold.copy(alpha = 0.18f))
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause ${track.title}" else "Play ${track.title}",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${track.reciter} • ${track.category} (${track.year})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = PlatformHelpers.formatDuration(track.durationSeconds),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun HomeLyricsCard(
    item: NohaLyricsEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Text(
                    text = item.titleUrdu.ifBlank { item.title },
                    style = TextStyle(
                        fontFamily = NastaliqUrduFontFamily,
                        fontSize = 18.sp,
                        lineHeight = 34.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.lyricsUrdu.lines().take(2).joinToString("\n"),
                    style = TextStyle(
                        fontFamily = NastaliqUrduFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${item.title} • ${item.primaryLanguage}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HomeArticleCard(
    article: IslamicArticleEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "${article.category} • ${article.publishedDate}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = article.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun YouTubeSubscribeBanner(
    settings: SiteSettingsEntity,
    onSubscribeClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ObsidianBlack,
        border = BorderStroke(1.dp, BurnishedGold.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB71C1C)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Subscriptions,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Official YouTube Channel",
                    style = MaterialTheme.typography.titleMedium,
                    color = ChampagneGold
                )
                Text(
                    text = "@Hussainaliofficial465 • TikTok: ${settings.tiktokHandle}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightMutedText
                )
            }
            Button(
                onClick = onSubscribeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BurnishedGold,
                    contentColor = ObsidianBlack
                ),
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text("Subscribe", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun WhatsAppContactBanner(
    settings: SiteSettingsEntity,
    onWhatsAppClick: () -> Unit,
    onContactPageClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Connect With Daste Imamia",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "For Majalis bookings, Noha recordings, or inquiries, message us directly on WhatsApp (${settings.whatsappNumber}).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onWhatsAppClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldSuccess,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp Us")
                }
                OutlinedButton(
                    onClick = onContactPageClick,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Contact Form")
                }
            }
        }
    }
}
